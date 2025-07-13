package shipment.updateStrategy

import shipment.Shipment
import shipment.ShippingUpdate

class ShippedUpdateStrategy: ShipmentUpdateStrategy {
    override fun applyUpdate(shipment: Shipment, timestamp: Long, otherinfo: String) {
        val update = ShippingUpdate(shipment.status, "shipped", timestamp)
        shipment.addUpdate(update)
        shipment.status = "shipped"
        shipment.expectedDeliveryDateTimeStamp = otherinfo.toLong()
    }
}
