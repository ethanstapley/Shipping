import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import shipment.TrackingSimulator
import shipment.TrackerViewHelper

fun main() = application {
    val simulator = TrackingSimulator()
    val trackedHelpers = remember { mutableStateListOf<TrackerViewHelper>() }
    var userInput by remember { mutableStateOf("") }
    var errorText by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        simulator.runSimulation("src/main/kotlin/resources/test.txt")
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
                    Button(onClick = {
                        val shipment = simulator.findShipment(userInput.trim())
                        if (shipment == null) {
                            errorText = "Shipment ID '$userInput' not found"
                        } else {
                            val helper = TrackerViewHelper()
                            helper.trackShipment(shipment)
                            trackedHelpers.add(helper)
                            errorText = ""
                            userInput = ""
                        }
                    }) {
                        Text("Track")
                    }
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