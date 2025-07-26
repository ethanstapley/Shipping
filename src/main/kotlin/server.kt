import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.json.Json
import io.ktor.http.ContentType
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.request.receiveParameters
import io.ktor.server.response.respondRedirect
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import shipment.Shipment
import shipment.TrackingManager
import kotlin.text.isNullOrBlank
import kotlin.text.trim
import shipment.shipmentType.*


private fun Shipment.toDto(): ShipmentDto = ShipmentDto(
    type = when (this) {
        is StandardShipment  -> "standard"
        is OvernightShipment -> "overnight"
        is ExpressShipment   -> "express"
        is BulkShipment      -> "bulk"
        else -> "standard"
    },
    id = id,
    status = status,
    notes = notes,
    updateHistory = updateHistory,
    expectedDeliveryDateTimeStamp = expectedDeliveryDateTimeStamp,
    currentLocation = currentLocation,
    createdTime = createdTime
)

fun startServer() {
    embeddedServer(Netty, port = 8080) {
        install(CallLogging)
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                }
            )
        }
        install(StatusPages) {
            exception<Throwable> { call, cause ->
                call.application.environment.log.error("Unhandled server exception", cause)
                call.respond(HttpStatusCode.InternalServerError, "Server error: ${cause.message}")
            }
        }

        val manager = TrackingManager()

        routing {
            get("/") {
                call.respondText(
                    """
                    <html>
                    <body style="font-family: sans-serif;">
                        <h1>Add Shipment</h1>
                        <form method="post" action="/">
                            <label for="line">Enter Shipment Line:</label><br/>
                            <input type="text" id="line" name="line" style="width:400px"/><br/><br/>
                            <input type="submit" value="Submit"/>
                        </form>
                    </body>
                    </html>
                    """.trimIndent(),
                    ContentType.Text.Html
                )
            }

            post("/") {
                val params = call.receiveParameters()
                val line = params["line"]?.trim()

                if (line.isNullOrBlank()) {
                    call.respond(HttpStatusCode.BadRequest, "Missing input")
                    return@post
                }

                runCatching { manager.processLine(line) }
                    .onSuccess { call.respondRedirect("/") }
                    .onFailure { t ->
                        call.application.environment.log.warn("Invalid shipment line: $line", t)
                        call.respond(HttpStatusCode.BadRequest, "Invalid shipment line: ${t.message ?: "parse error"}")
                    }
            }

            get("/shipment/{id}") {
                val id = call.parameters["id"]
                    ?: return@get call.respond(HttpStatusCode.BadRequest, "Missing ID")
                val s = manager.findShipment(id)
                    ?: return@get call.respond(HttpStatusCode.NotFound, "Shipment not found")
                call.respond(s.toDto())
            }

        }
    }.start(wait = true)
}