package shipment

import kotlin.test.*

class ShipmentTest {

    @Test
    fun addNoteShouldAppendToNotesList() {
        val shipment = createTestShipment()
        shipment.addNote("Handle with care")
        shipment.addNote("Hello There!")

        assertEquals(2, shipment.notes.size)
        assertEquals("Handle with care", shipment.notes[0])
        assertEquals("Hello There!", shipment.notes[1])
    }

    @Test
    fun deleteObserverShouldNotTriggerNotification() {
        val shipment = createTestShipment()
        val observer = DummyObserver()
        shipment.addObserver(observer)
        shipment.deleteObserver(observer)

        shipment.addNote("test note")

        assertFalse(observer.notified)
    }

    @Test
    fun addNoteShouldNotifyObservers() {
        val shipment = createTestShipment()
        val observer = DummyObserver()
        shipment.addObserver(observer)

        shipment.addNote("Important note")

        assertTrue(observer.notified)
        assertEquals(shipment, observer.testShipment)
    }

    @Test
    fun addUpdateShouldNotifyObservers() {
        val shipment = createTestShipment()
        val observer = DummyObserver()
        shipment.addObserver(observer)

        val update = ShippingUpdate("created", "shipped", 123L)
        shipment.addUpdate(update)

        assertTrue(observer.notified)
        assertEquals(shipment, observer.testShipment)
    }

    @Test
    fun addShippingUpdateShouldAppendUpdateToHistory() {
        val shipment = createTestShipment()
        val update = ShippingUpdate("created", "shipped", 123456L)
        val update2 = ShippingUpdate("canceled", "shipped", 12345678L)

        shipment.addUpdate(update)
        shipment.addUpdate(update2)

        assertEquals(2, shipment.updateHistory.size)
        assertEquals(update, shipment.updateHistory[0])
        assertEquals(update2, shipment.updateHistory[1])
    }
}
