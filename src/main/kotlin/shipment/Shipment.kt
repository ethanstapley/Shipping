package shipment
import kotlinx.serialization.Serializable

@Serializable
abstract class Shipment (
    open var status: String,
    open var id: String,
    open val notes: ArrayList<String>,
    open val updateHistory: ArrayList<ShippingUpdate>,
    open var expectedDeliveryDateTimeStamp: Long,
    open var currentLocation: String,
    open var createdTime: Long
){
    private var observers: MutableSet<ShipmentObserver> = mutableSetOf()

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

    open fun validateExpectedDeliveryDate() {}
}