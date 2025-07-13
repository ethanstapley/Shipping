package shipment

class ShippingUpdate (
    var previousStatus: String,
    var newStatus: String,
    var timestamp: Long,
    var shipmentID: String
) {
}