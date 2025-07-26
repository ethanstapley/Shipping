package shipment

import shipment.updateStrategy.CanceledUpdateStrategy
import shipment.updateStrategy.DelayedUpdateStrategy
import shipment.updateStrategy.DeliveredUpdateStrategy
import shipment.updateStrategy.LocationUpdateStrategy
import shipment.updateStrategy.LostUpdateStrategy
import shipment.updateStrategy.NoteAddedUpdateStrategy
import shipment.updateStrategy.ShipmentUpdateStrategy
import shipment.updateStrategy.ShippedUpdateStrategy

class TrackingManager(
    private val shipments: MutableMap<String, Shipment> = mutableMapOf(),
    private val strategyMap: Map<String, ShipmentUpdateStrategy> = mapOf<String, ShipmentUpdateStrategy>(
        "shipped" to ShippedUpdateStrategy(),
        "location" to LocationUpdateStrategy(),
        "delayed" to DelayedUpdateStrategy(),
        "noteadded" to NoteAddedUpdateStrategy(),
        "lost" to LostUpdateStrategy(),
        "canceled" to CanceledUpdateStrategy(),
        "delivered" to DeliveredUpdateStrategy()
    )
) {
    private val shipmentFactory = ShipmentFactory

    fun addShipment(shipment: Shipment) = shipments.put(shipment.id, shipment)

    fun findShipment(id: String) = shipments[id]

    fun processLine(line: String) {
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
                val shipment = shipmentFactory.createShipment(otherInfo, shipmentId, timestampOfUpdate)
                addShipment(shipment)
            }
            println("CREATED!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
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