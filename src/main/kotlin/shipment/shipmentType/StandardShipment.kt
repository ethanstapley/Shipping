package shipment.shipmentType

import shipment.Shipment
import shipment.ShippingUpdate

class StandardShipment(
    status: String,
    id: String,
    notes: ArrayList<String>,
    updateHistory: ArrayList<ShippingUpdate>,
    expectedDeliveryDateTimeStamp: Long,
    currentLocation: String,
    createdTime: Long
) : Shipment(status, id, notes, updateHistory, expectedDeliveryDateTimeStamp, currentLocation, createdTime) {

}