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

    fun runSimulation(filepath: String) = runBlocking(Dispatchers.IO) {
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
        val sections = line.split(",")
        if (sections.size < 3 || sections.size > 4) {
            println("Invalid line: $line")
            return
        }

        val updateType = sections[0]
        val shipmentId = sections[1]
        val timestampOfUpdate = sections[2].trim().toLong()
        val otherInfo = if (sections.size == 4) sections[3] else ""

        if (updateType == "created") {
            if (shipments.containsKey(shipmentId)) {
                println("Shipment with ID: '$shipmentId' already exists")
                return
            } else {
                val shipment = Shipment(
                    status = "created",
                    id = shipmentId,
                    notes = arrayListOf(),
                    updateHistory = arrayListOf(),
                    expectedDeliveryDateTimeStamp = 0L,
                    currentLocation = "",
                    observers = mutableSetOf())
                addShipment(shipment)
            }
            return
        }

        val shipment = findShipment(shipmentId)
        if (shipment == null) {
            println("Shipment ID: '$shipmentId' not found")
            return
        }

        val strategy = strategyMap[updateType]
        if (strategy == null) {
            println("Unknown update type: $updateType")
            return
        }

        strategy.applyUpdate(shipment, timestampOfUpdate, otherInfo)
    }
}