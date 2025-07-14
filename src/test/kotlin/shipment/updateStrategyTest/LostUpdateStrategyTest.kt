package shipment.updateStrategyTest

import shipment.*
import shipment.updateStrategy.LostUpdateStrategy
import kotlin.test.Test
import kotlin.test.assertEquals

class LostUpdateStrategyTest {
    val strategy = LostUpdateStrategy()

    @Test
    fun applyUpdateStatusVerification() {
        val shipment = createTestShipment()
        strategy.applyUpdate(shipment, 123L, "")
        assertEquals("lost", shipment.status)
    }

    @Test
    fun applyUpdateHistoryCheck() {
        val shipment = createTestShipment()
        strategy.applyUpdate(shipment, 123L, "")
        val update = shipment.updateHistory.first()
        assertEquals("created", update.previousStatus)
        assertEquals("lost", update.newStatus)
        assertEquals(123L, update.timestamp)
    }
}
