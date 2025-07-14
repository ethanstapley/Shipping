package shipment

fun createTestShipment(): Shipment {
    return Shipment(
        status = "created",
        id = "abc",
        notes = arrayListOf(),
        updateHistory = arrayListOf(),
        expectedDeliveryDateTimeStamp = 0L,
        currentLocation = "",
        observers = mutableSetOf()
    )
}
