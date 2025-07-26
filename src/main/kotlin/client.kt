import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json
import shipment.Shipment
import shipment.TrackerViewHelper
import shipment.shipmentType.BulkShipment
import shipment.shipmentType.ExpressShipment
import shipment.shipmentType.OvernightShipment
import shipment.shipmentType.StandardShipment

val httpClient = HttpClient(CIO) {
    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true })
    }
}

private fun ShipmentDto.toDomain(): Shipment =
    when (type) {
        "standard"  -> StandardShipment(status, id, ArrayList(notes), ArrayList(updateHistory), expectedDeliveryDateTimeStamp, currentLocation, createdTime)
        "overnight" -> OvernightShipment(status, id, ArrayList(notes), ArrayList(updateHistory), expectedDeliveryDateTimeStamp, currentLocation, createdTime)
        "express"   -> ExpressShipment(status, id, ArrayList(notes), ArrayList(updateHistory), expectedDeliveryDateTimeStamp, currentLocation, createdTime)
        "bulk"      -> BulkShipment(status, id, ArrayList(notes), ArrayList(updateHistory), expectedDeliveryDateTimeStamp, currentLocation, createdTime)
        else        -> throw IllegalArgumentException("Invalid Shipment Type: $type")
    }

suspend fun fetchShipmentFromServer(id: String): Shipment? = try {
    httpClient.get("http://localhost:8080/shipment/$id").body<ShipmentDto>().toDomain()
} catch (_: Exception) { null }

fun startClient() = application {
    val scope = rememberCoroutineScope()
    val trackedHelpers = remember { mutableStateListOf<TrackerViewHelper>() }
    val pollJobs = remember { mutableMapOf<TrackerViewHelper, Job>() }
    var userInput by remember { mutableStateOf("") }
    var errorText by remember { mutableStateOf("") }

    Window(onCloseRequest = ::exitApplication, title = "Shipping Tracker") {
        MaterialTheme {
            val scrollState = rememberScrollState()
            Column(modifier = Modifier.padding(16.dp).verticalScroll(scrollState)) {
                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                    TextField(
                        value = userInput,
                        onValueChange = { userInput = it },
                        label = { Text("Shipment ID") },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(Modifier.width(8.dp))
                    Button(onClick = {
                        scope.launch {
                            val id = userInput.trim()
                            val first = fetchShipmentFromServer(id)
                            if (first == null) {
                                errorText = "Shipment ID '$id' not found"
                            } else {
                                val helper = TrackerViewHelper()
                                helper.trackShipment(first)
                                trackedHelpers.add(helper)
                                errorText = ""
                                userInput = ""
                                // NOTE: Observer pattern wasn't fully realized here, as the setup would need WebSockets, so I used polling instead. Which defeats the point of the observer Pattern
                                val job = scope.launch {
                                    while (isActive && trackedHelpers.contains(helper)) {
                                        try {
                                            val dto: ShipmentDto = httpClient.get("http://localhost:8080/shipment/$id").body()
                                            val updated = dto.toDomain()
                                            helper.trackShipment(updated)
                                        } catch (_: Exception) { }
                                        delay(500)
                                    }
                                }
                                pollJobs[helper] = job
                            }
                        }
                    }) { Text("Track") }
                }

                if (errorText.isNotEmpty()) {
                    Spacer(Modifier.height(8.dp))
                    Text(errorText, color = MaterialTheme.colors.error)
                }

                Spacer(Modifier.height(16.dp))

                for (helper in trackedHelpers) {
                    Card(elevation = 4.dp, modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("ID: ${helper.shipmentId.collectAsState().value}")
                            Text("Status: ${helper.shipmentStatus.collectAsState().value}")
                            Text("Current Location: ${helper.shipmentLocation.collectAsState().value}")
                            Text("Delivery ETA: ${helper.expectedShipmentDeliveryDate.collectAsState().value}")
                            Text("Notes:")
                            helper.shipmentNotes.collectAsState().value.forEach { Text("- $it") }

                            Text("Update History:")
                            helper.shipmentUpdateHistory.collectAsState().value.forEach { Text("- $it") }

                            Spacer(Modifier.height(8.dp))
                            Button(onClick = {
                                helper.stopTracking()
                                trackedHelpers.remove(helper)
                                pollJobs.remove(helper)?.cancel()
                            }) { Text("Stop Tracking") }
                        }
                    }
                }
            }
        }
    }
}
