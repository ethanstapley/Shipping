package shipment

import kotlinx.coroutines.*

import shipment.updateStrategy.CanceledUpdateStrategy
import shipment.updateStrategy.DelayedUpdateStrategy
import shipment.updateStrategy.DeliveredUpdateStrategy
import shipment.updateStrategy.LocationUpdateStrategy
import shipment.updateStrategy.LostUpdateStrategy
import shipment.updateStrategy.NoteAddedUpdateStrategy
import shipment.updateStrategy.ShipmentUpdateStrategy
import shipment.updateStrategy.ShippedUpdateStrategy

class TrackingSimulator(
    val shipments: MutableMap<String, Shipment> = mutableMapOf(),
    val strategyMap: Map<String, ShipmentUpdateStrategy> = mapOf<String, ShipmentUpdateStrategy>(
        "shipped" to ShippedUpdateStrategy(),
        "location" to LocationUpdateStrategy(),
        "delayed" to DelayedUpdateStrategy(),
        "noteadded" to NoteAddedUpdateStrategy(),
        "lost" to LostUpdateStrategy(),
        "canceled" to CanceledUpdateStrategy(),
        "delivered" to DeliveredUpdateStrategy()
    )
) {
    fun addShipment(shipment: Shipment) {
        shipments.put(shipment.id, shipment)
    }

    fun findShipment(id: String) = shipments[id]

    fun runSimulation(filepath: String) = runBlocking {
        val file = java.io.File(filepath)
        if (!file.exists()) {
            println("Input file not found with path: $filepath")
            return@runBlocking
        }

        file.useLines { lines ->
            launch {
                for (line in lines) {
                    processLine(line)
                    delay(1000)
                }
            }
        }
    }

    private fun processLine(line: String) {

    }
}