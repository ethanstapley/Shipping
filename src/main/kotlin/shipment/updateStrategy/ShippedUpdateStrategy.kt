package shipment.updateStrategy

import shipment.Shipment
import shipment.ShippingUpdate

class ShippedUpdateStrategy: ShipmentUpdateStrategy {
    override fun applyUpdate(shipment: Shipment, timestamp: Long, otherinfo: String) {
        val update = ShippingUpdate(shipment.status, "shipped", timestamp)
        shipment.status = "shipped"
        shipment.expectedDeliveryDateTimeStamp = otherinfo.trim().toLong()
        shipment.validateExpectedDeliveryDate()
        shipment.addUpdate(update)
    }
}
