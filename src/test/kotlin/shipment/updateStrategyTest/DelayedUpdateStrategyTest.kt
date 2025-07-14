package shipment.updateStrategyTest

import shipment.*
import shipment.updateStrategy.DelayedUpdateStrategy
import kotlin.test.Test
import kotlin.test.assertEquals

class DelayedUpdateStrategyTest {
    val strategy = DelayedUpdateStrategy()

    @Test
    fun applyUpdateStatusVerification() {
        val shipment = createTestShipment()
        strategy.applyUpdate(shipment, 123L, "9999999999")
        assertEquals("delayed", shipment.status)
        assertEquals(9999999999, shipment.expectedDeliveryDateTimeStamp)
    }

    @Test
    fun applyUpdateHistoryCheck() {
        val shipment = createTestShipment()
        strategy.applyUpdate(shipment, 123L, "9999999999")
        val update = shipment.updateHistory.first()
        assertEquals("created", update.previousStatus)
        assertEquals("delayed", update.newStatus)
        assertEquals(123L, update.timestamp)
    }
}
