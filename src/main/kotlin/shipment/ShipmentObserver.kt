package shipment

interface ShipmentObserver {
    fun onShipmentUpdated(shipment: Shipment)
}