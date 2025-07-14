package shipment.updateStrategy

import shipment.Shipment
import shipment.ShippingUpdate

class CanceledUpdateStrategy: ShipmentUpdateStrategy {
    override fun applyUpdate(shipment: Shipment, timestamp: Long, otherinfo: String) {
        val update = ShippingUpdate(shipment.status, "canceled", timestamp)
        shipment.status = "canceled"
        shipment.addUpdate(update)
    }
}