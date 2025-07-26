package shipment

import kotlinx.serialization.Serializable

@Serializable
class ShippingUpdate (
    var previousStatus: String,
    var newStatus: String,
    var timestamp: Long,
) {
}