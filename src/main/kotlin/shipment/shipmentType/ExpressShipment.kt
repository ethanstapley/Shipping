package shipment.shipmentType

import shipment.Shipment
import shipment.ShippingUpdate

class ExpressShipment (
    status: String,
    id: String,
    notes: ArrayList<String>,
    updateHistory: ArrayList<ShippingUpdate>,
    expectedDeliveryDateTimeStamp: Long,
    currentLocation: String,
    createdTime: Long
) : Shipment(status, id, notes, updateHistory, expectedDeliveryDateTimeStamp, currentLocation, createdTime) {

    override fun validateExpectedDeliveryDate() {
        val threeDays = 259200000L
        if (expectedDeliveryDateTimeStamp > createdTime + threeDays && status != "delayed") {
            addNote("An express shipment was updated to include a delivery date later than 72 hours after it was created")
        }
    }
}