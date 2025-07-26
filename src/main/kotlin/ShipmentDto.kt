import kotlinx.serialization.Serializable

@Serializable
data class ShipmentDto(
    val type: String,
    val id: String,
    val status: String,
    val notes: List<String>,
    val updateHistory: List<shipment.ShippingUpdate>,
    val expectedDeliveryDateTimeStamp: Long,
    val currentLocation: String,
    val createdTime: Long
)