package shipment

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TrackerViewHelper: shipment.ShipmentObserver{
    private val _shipmentId = MutableStateFlow("")
    val shipmentId: StateFlow<String> = _shipmentId

    private val _shipmentNotes = MutableStateFlow<ArrayList<String>>(arrayListOf())
    val shipmentNotes: StateFlow<ArrayList<String>> = _shipmentNotes

    private val _shipmentUpdateHistory = MutableStateFlow<ArrayList<String>>(arrayListOf())
    val shipmentUpdateHistory: StateFlow<ArrayList<String>> = _shipmentUpdateHistory

    private val _expectedShipmentDeliveryDate = MutableStateFlow("")
    val expectedShipmentDeliveryDate: StateFlow<String> = _expectedShipmentDeliveryDate

    private val _shipmentStatus = MutableStateFlow("")
    val shipmentStatus: StateFlow<String> = _shipmentStatus

    override fun onShipmentUpdated(shipment: Shipment) {

    }
}