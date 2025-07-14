package shipment.updateStrategyTest

import shipment.*
import shipment.updateStrategy.LocationUpdateStrategy
import kotlin.test.Test
import kotlin.test.assertEquals

class LocationUpdateStrategyTest {
    val strategy = LocationUpdateStrategy()

    @Test
    fun applyUpdateStatusVerification() {
        val shipment = createTestShipment()
        strategy.applyUpdate(shipment, 123L, "Seattle")
        assertEquals("location", shipment.status)
        assertEquals("Seattle", shipment.currentLocation)
    }

    @Test
    fun applyUpdateHistoryCheck() {
        val shipment = createTestShipment()
        strategy.applyUpdate(shipment, 123L, "Seattle")
        val update = shipment.updateHistory.first()
        assertEquals("created", update.previousStatus)
        assertEquals("location", update.newStatus)
        assertEquals(123L, update.timestamp)
    }
}
