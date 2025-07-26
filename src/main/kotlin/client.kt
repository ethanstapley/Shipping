import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import shipment.Shipment
import shipment.TrackerViewHelper

val httpClient = HttpClient(CIO) {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
        })
    }
}

fun startClient() = application {

    val trackedHelpers = remember { mutableStateListOf<TrackerViewHelper>() }
    var userInput by remember { mutableStateOf("") }
    var errorText by remember { mutableStateOf("") }

    suspend fun fetchShipmentFromServer(id: String): Shipment? {
        return try {
            httpClient.get("http://localhost:8080/shipment/$id").body()
        } catch (e: Exception) {
            null
        }
    }

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
                    val scope = rememberCoroutineScope()
                    Button(onClick = {
                        scope.launch {
                            val shipment = withContext(Dispatchers.IO) { fetchShipmentFromServer(userInput.trim()) }
                            if (shipment == null) {
                                errorText = "Shipment ID '$userInput' not found"
                            } else {
                                val helper = TrackerViewHelper()
                                helper.trackShipment(shipment)
                                trackedHelpers.add(helper)
                                errorText = ""
                                userInput = ""
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
                    Card(
                        elevation = 4.dp,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("ID: ${helper.shipmentId.collectAsState().value}")
                            Text("Status: ${helper.shipmentStatus.collectAsState().value}")
                            Text("Current Location: ${helper.shipmentLocation.collectAsState().value}")
                            Text("Delivery ETA: ${helper.expectedShipmentDeliveryDate.collectAsState().value}")
                            Text("Notes:")

                            helper.shipmentNotes.collectAsState().value.forEach {
                                Text("- $it")
                            }

                            Text("Update History:")
                            helper.shipmentUpdateHistory.collectAsState().value.forEach {
                                Text("- $it")
                            }

                            Spacer(Modifier.height(8.dp))
                            Button(onClick = {
                                helper.stopTracking()
                                trackedHelpers.remove(helper)
                            }) {
                                Text("Stop Tracking")
                            }
                        }
                    }
                }
            }
        }
    }
}