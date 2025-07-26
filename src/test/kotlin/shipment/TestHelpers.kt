package shipment

import shipment.shipmentType.StandardShipment

fun createTestShipment(): Shipment {
    return StandardShipment(
        status = "created",
        id = "abc",
        notes = arrayListOf(),
        updateHistory = arrayListOf(),
        expectedDeliveryDateTimeStamp = 0L,
        currentLocation = "",
        createdTime = 0L
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