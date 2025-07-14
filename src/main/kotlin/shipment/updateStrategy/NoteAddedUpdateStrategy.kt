package shipment.updateStrategy

import shipment.Shipment
import shipment.ShippingUpdate

class NoteAddedUpdateStrategy: ShipmentUpdateStrategy {
    override fun applyUpdate(shipment: Shipment, timestamp: Long, otherinfo: String) {
        val update = ShippingUpdate(shipment.status, shipment.status, timestamp)
        shipment.addUpdate(update)
        shipment.addNote(otherinfo)
    }
}