package shipment

class Shipment (
    status: String,
    id: String,
    notes: ArrayList<String>,
    updateHistory: ArrayList<ShippingUpdate>,
    expectedDeliveryDateTimeStamp: Long,
    currentLocation: String,
    private var observers: ArrayList<ShipmentObserver>
){
    var notes: ArrayList<String> = notes
        private set

    var updateHistory: ArrayList<ShippingUpdate> = updateHistory
        private set


}