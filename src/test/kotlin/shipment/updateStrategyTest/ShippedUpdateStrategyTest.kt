package shipment.updateStrategyTest

import shipment.*
import shipment.updateStrategy.ShippedUpdateStrategy
import kotlin.test.Test
import kotlin.test.assertEquals

class ShippedUpdateStrategyTest {
    val strategy = ShippedUpdateStrategy()

    @Test
    fun applyUpdateStatusVerification() {
        val shipment = createTestShipment()
        strategy.applyUpdate(shipment, 123L, "8888888888")
        assertEquals("shipped", shipment.status)
        assertEquals(8888888888, shipment.expectedDeliveryDateTimeStamp)
    }

    @Test
    fun applyUpdateHistoryCheck() {
        val shipment = createTestShipment()
        strategy.applyUpdate(shipment, 123L, "8888888888")
        val update = shipment.updateHistory.first()
        assertEquals("created", update.previousStatus)
        assertEquals("shipped", update.newStatus)
        assertEquals(123L, update.timestamp)
    }
}
