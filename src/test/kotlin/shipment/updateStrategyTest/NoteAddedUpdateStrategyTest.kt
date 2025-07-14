package shipment.updateStrategyTest

import shipment.*
import shipment.updateStrategy.NoteAddedUpdateStrategy
import kotlin.test.Test
import kotlin.test.assertEquals

class NoteAddedUpdateStrategyTest {
    val strategy = NoteAddedUpdateStrategy()

    @Test
    fun applyUpdateStatusVerification() {
        val shipment = createTestShipment()
        strategy.applyUpdate(shipment, 123L, "Extra fragile")
        assertEquals(listOf("Extra fragile"), shipment.notes)
    }

    @Test
    fun applyUpdateHistoryCheck() {
        val shipment = createTestShipment()
        strategy.applyUpdate(shipment, 123L, "Extra fragile")
        val update = shipment.updateHistory.first()
        assertEquals("created", update.previousStatus)
        assertEquals("created", update.newStatus)
        assertEquals(123L, update.timestamp)
    }
}
