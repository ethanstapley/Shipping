package shipment.updateStrategy

import shipment.Shipment
import shipment.ShippingUpdate

class NoteAddedUpdateStrategy: ShipmentUpdateStrategy {
    override fun applyUpdate(shipment: Shipment, timestamp: Long, otherinfo: String) {
        val update = ShippingUpdate(shipment.status, "noteadded", timestamp)
        shipment.status = "noteadded"
        shipment.addUpdate(update)
        shipment.addNote(otherinfo)
    }
}