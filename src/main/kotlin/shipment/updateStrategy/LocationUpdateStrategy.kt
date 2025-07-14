package shipment.updateStrategy

import shipment.Shipment
import shipment.ShippingUpdate

class LocationUpdateStrategy: ShipmentUpdateStrategy {
    override fun applyUpdate(shipment: Shipment, timestamp: Long, otherinfo: String) {
        val update = ShippingUpdate(shipment.status, "location", timestamp)
        shipment.status = "location"
        shipment.currentLocation = otherinfo
        shipment.addUpdate(update)
    }
}