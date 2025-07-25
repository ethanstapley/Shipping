package shipment

import shipment.shipmentType.BulkShipment
import shipment.shipmentType.ExpressShipment
import shipment.shipmentType.OvernightShipment
import shipment.shipmentType.StandardShipment

object ShipmentFactory {
    fun createShipment(type: String, id: String, createdTime: Long): Shipment {
        return when(type) {
            "standard" -> StandardShipment(
                status = "created",
                id = id,
                notes = arrayListOf(),
                updateHistory = arrayListOf(),
                expectedDeliveryDateTimeStamp = 0L,
                currentLocation = "",
                createdTime = createdTime
                )

            "bulk" -> BulkShipment(
                status = "created",
                id = id,
                notes = arrayListOf(),
                updateHistory = arrayListOf(),
                expectedDeliveryDateTimeStamp = 0L,
                currentLocation = "",
                createdTime = createdTime
                )

            "overnight" -> OvernightShipment(
                status = "created",
                id = id,
                notes = arrayListOf(),
                updateHistory = arrayListOf(),
                expectedDeliveryDateTimeStamp = 0L,
                currentLocation = "",
                createdTime = createdTime
                )
            "express" -> ExpressShipment(
                status = "created",
                id = id,
                notes = arrayListOf(),
                updateHistory = arrayListOf(),
                expectedDeliveryDateTimeStamp = 0L,
                currentLocation = "",
                createdTime = createdTime
                )
            else -> throw IllegalArgumentException("Invalid Shipment Type: $type")
        }
    }
}