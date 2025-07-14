package shipment

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.text.SimpleDateFormat
import java.util.*

class TrackerViewHelper : ShipmentObserver {
    private val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    private var currentShipment: Shipment? = null

    private val _shipmentId = MutableStateFlow("")
    val shipmentId: StateFlow<String> = _shipmentId

    private val _shipmentNotes = MutableStateFlow<List<String>>(emptyList())
    val shipmentNotes: StateFlow<List<String>> = _shipmentNotes

    private val _shipmentUpdateHistory = MutableStateFlow<List<String>>(emptyList())
    val shipmentUpdateHistory: StateFlow<List<String>> = _shipmentUpdateHistory

    private val _expectedShipmentDeliveryDate = MutableStateFlow("")
    val expectedShipmentDeliveryDate: StateFlow<String> = _expectedShipmentDeliveryDate

    private val _shipmentStatus = MutableStateFlow("")
    val shipmentStatus: StateFlow<String> = _shipmentStatus

    private val _shipmentLocation = MutableStateFlow("")
    val shipmentLocation: StateFlow<String> = _shipmentLocation

    override fun onShipmentUpdated(shipment: Shipment) {
        _shipmentId.value = shipment.id
        _shipmentNotes.value = shipment.notes.toList()
        _shipmentUpdateHistory.value = shipment.updateHistory.map {
            val formattedTime = formatter.format(Date(it.timestamp))
            "Shipment went from ${it.previousStatus} to ${it.newStatus} at $formattedTime"
        }
        _expectedShipmentDeliveryDate.value = if (shipment.expectedDeliveryDateTimeStamp >0L) {
            formatter.format(Date(shipment.expectedDeliveryDateTimeStamp))
        } else {
            "unknown"
        }
        _shipmentStatus.value = shipment.status
        _shipmentLocation.value = shipment.currentLocation
    }

    fun trackShipment(shipment: Shipment) {
        currentShipment?.deleteObserver(this)
        currentShipment = shipment
        shipment.addObserver(this)
        onShipmentUpdated(shipment)
    }

    fun stopTracking() {
        currentShipment?.deleteObserver(this)
        currentShipment = null
    }
}
