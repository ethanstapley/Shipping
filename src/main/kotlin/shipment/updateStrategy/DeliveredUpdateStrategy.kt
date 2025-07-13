package shipment.updateStrategy

import shipment.Shipment
import shipment.ShippingUpdate

class DeliveredUpdateStrategy: ShipmentUpdateStrategy {
    override fun applyUpdate(shipment: Shipment, timestamp: Long, otherinfo: String) {
        val update = ShippingUpdate(shipment.status, "delivered", timestamp)
        shipment.addUpdate(update)
        shipment.status = "delivered"
        shipment.currentLocation = otherinfo
    }
}