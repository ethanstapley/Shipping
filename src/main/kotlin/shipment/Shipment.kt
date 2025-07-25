package shipment

class Shipment (
    var status: String,
    var id: String,
    notes: ArrayList<String>,
    updateHistory: ArrayList<ShippingUpdate>,
    var expectedDeliveryDateTimeStamp: Long,
    var currentLocation: String,
){
    private var observers: MutableSet<ShipmentObserver> = mutableSetOf()

    var notes: ArrayList<String> = notes
        private set

    var updateHistory: ArrayList<ShippingUpdate> = updateHistory
        private set

    fun addNote(note: String) {
        notes.add(note)
        notifyObserver()
    }

    fun addUpdate(update: ShippingUpdate) {
        updateHistory.add(update)
        notifyObserver()
    }

    fun addObserver(observer: ShipmentObserver) = observers.add(observer)

    fun deleteObserver(observer: ShipmentObserver) = observers.remove(observer)

    private fun notifyObserver() {
        for (observer in observers) observer.onShipmentUpdated(this)
    }
}