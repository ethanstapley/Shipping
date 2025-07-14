package shipment.updateStrategyTest

import shipment.*
import shipment.updateStrategy.DeliveredUpdateStrategy
import kotlin.test.Test
import kotlin.test.assertEquals

class DeliveredUpdateStrategyTest {
    val strategy = DeliveredUpdateStrategy()

    @Test
    fun applyUpdateStatusVerification() {
        val shipment = createTestShipment()
        strategy.applyUpdate(shipment, 123L, "")
        assertEquals("delivered", shipment.status)
        assertEquals(1, shipment.updateHistory.size)
    }

    @Test
    fun applyUpdateHistoryCheck() {
        val shipment = createTestShipment()
        strategy.applyUpdate(shipment, 123L, "")
        val update = shipment.updateHistory.first()
        assertEquals("created", update.previousStatus)
        assertEquals(123L, update.timestamp)
    }
}
