package shipment.shipmentType

import shipment.Shipment
import shipment.ShippingUpdate

class OvernightShipment (
    status: String,
    id: String,
    notes: ArrayList<String>,
    updateHistory: ArrayList<ShippingUpdate>,
    expectedDeliveryDateTimeStamp: Long,
    currentLocation: String,
    createdTime: Long
) : Shipment(status, id, notes, updateHistory, expectedDeliveryDateTimeStamp, currentLocation, createdTime) {

    override fun validateExpectedDeliveryDate() {
        val oneDay = 86400000
        if (expectedDeliveryDateTimeStamp > createdTime + oneDay && status != "delayed") {
            addNote("An overnight shipment was updated to include a delivery date later than 24 hours after it was created.")
        }
    }
}