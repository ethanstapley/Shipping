package shipment

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import shipment.updateStrategy.ShippedUpdateStrategy
import kotlin.test.*

class TrackerViewHelperTest {

    @Test
    fun onShipmentUpdatedAttributesVerification() = runTest {
        val shipment = createTestShipment()
        ShippedUpdateStrategy().applyUpdate(shipment, 123L, "888888")
        shipment.addNote("Fragile")

        val helper = TrackerViewHelper()
        helper.trackShipment(shipment)

        assertEquals("abc", helper.shipmentId.first())
        assertEquals("shipped", helper.shipmentStatus.first())
        assertEquals(listOf("Fragile"), helper.shipmentNotes.first())
        assertEquals("1969-12-31 17:14:48", helper.expectedShipmentDeliveryDate.first())
        assertTrue(helper.shipmentUpdateHistory.first().first().contains("Shipment went from created to shipped"))
    }

    @Test
    fun trackShipmentReplaceTest() {
        val helper = TrackerViewHelper()

        val oldShipment = createTestShipment()
        val newShipment = createTestShipment()
        newShipment.id = "def"

        helper.trackShipment(oldShipment)
        helper.trackShipment(newShipment)

        assertEquals("def", helper.shipmentId.value)
    }

    @Test
    fun stopTrackingShouldRemoveObserver() {
        val helper = TrackerViewHelper()
        val shipment = createTestShipment()

        helper.trackShipment(shipment)
        helper.stopTracking()

        shipment.addNote("Testing")

        assertTrue(helper.shipmentNotes.value.isEmpty())
    }

    @Test
    fun stopTrackingWhenNoShipmentDoesNotThrow() {
        val helper = TrackerViewHelper()
        helper.stopTracking()
        // no assertion, just makes sure there is no throw. Used to cover branches on coverage
    }

}
