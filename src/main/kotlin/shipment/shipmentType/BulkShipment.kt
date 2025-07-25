package shipment.shipmentType

import shipment.Shipment
import shipment.ShippingUpdate

class BulkShipment (
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
        if (expectedDeliveryDateTimeStamp < createdTime + threeDays) {
            addNote("A bulk shipment expected sooner than the required 72 hours waiting time")
        }
    }

}