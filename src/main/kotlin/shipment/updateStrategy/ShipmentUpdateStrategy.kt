package shipment.updateStrategy

import shipment.Shipment

interface ShipmentUpdateStrategy {
    fun applyUpdate(shipment: Shipment, timestamp: Long, otherinfo: String)
}