package shipment

fun createTestShipment(): Shipment {
    return Shipment(
        status = "created",
        id = "abc",
        notes = arrayListOf(),
        updateHistory = arrayListOf(),
        expectedDeliveryDateTimeStamp = 0L,
        currentLocation = ""
    )
}

class DummyObserver : ShipmentObserver {
    var notified = false
    var testShipment: Shipment? = null

    override fun onShipmentUpdated(shipment: Shipment) {
        notified = true
        testShipment = shipment
    }
}