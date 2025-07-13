package shipment.updateStrategy

import shipment.Shipment
import shipment.ShippingUpdate

class LostUpdateStrategy: ShipmentUpdateStrategy {
    override fun applyUpdate(shipment: Shipment, timestamp: Long, otherinfo: String) {
        val update = ShippingUpdate(shipment.status, "lost", timestamp)
        shipment.addUpdate(update)
        shipment.status = "lost"
    }

}