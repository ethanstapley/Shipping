package shipment.updateStrategyTest
import shipment.*
import shipment.updateStrategy.CanceledUpdateStrategy
import kotlin.test.Test
import kotlin.test.assertEquals


class CanceledUpdateStrategyTest {
    val strategy = CanceledUpdateStrategy()

    @Test
    fun applyUpdateStatusVerification() {
        val shipment = createTestShipment()
        strategy.applyUpdate(shipment, 123L, "")

        assertEquals("canceled", shipment.status)
        assertEquals(1, shipment.updateHistory.size)
    }

    @Test
    fun applyUpdateAddsCorrectShippingUpdateToHistory() {
        val shipment = createTestShipment()
        strategy.applyUpdate(shipment, 123L, "")

        val update = shipment.updateHistory.first()
        assertEquals("created", update.previousStatus)
        assertEquals("canceled", update.newStatus)
        assertEquals(123L, update.timestamp)
    }

}