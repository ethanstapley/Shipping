package shipment.updateStrategy

import shipment.Shipment
import shipment.ShippingUpdate

class DelayedUpdateStrategy: ShipmentUpdateStrategy {
    override fun applyUpdate(shipment: Shipment, timestamp: Long, otherinfo: String) {
        val update = ShippingUpdate(shipment.status, "delayed", timestamp)
        shipment.status = "delayed"
        shipment.expectedDeliveryDateTimeStamp = otherinfo.toLong()
        shipment.addUpdate(update)
    }
}