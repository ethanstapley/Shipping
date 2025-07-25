package shipment.shipmentType

import shipment.Shipment
import shipment.ShippingUpdate

class BulkShipment (
    status: String,
    id: String,
    notes: ArrayList<String>,
    updateHistory: ArrayList<ShippingUpdate>,
    expectedDeliveryDateTimeStamp: Long,
    currentLocation: String
) : Shipment(status, id, notes, updateHistory, expectedDeliveryDateTimeStamp, currentLocation) {

    override fun validateExpectedDeliveryDate() {

    }

}