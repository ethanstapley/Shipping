package shipment.shipmentType

import kotlin.test.Test
import kotlin.test.assertTrue

class ShipmentTypeTests {

    @Test
    fun BulkDateUnder() {
        val created = 1_000_000L
        val delivery = created + 2 * 86_400_000L
        val s = BulkShipment("created", "B1", arrayListOf(), arrayListOf(), delivery, "Depot", created)

        s.validateExpectedDeliveryDate()

        assertTrue(s.notes.any { it.contains("bulk shipment expected sooner") })
    }

    @Test
    fun BulkDateOver() {
        val created = 1_000_000L
        val delivery = created + 3 * 86_400_000L
        val s = BulkShipment("created", "B2", arrayListOf(), arrayListOf(), delivery, "Depot", created)

        s.validateExpectedDeliveryDate()

        assertTrue(s.notes.isEmpty())
    }

    @Test
    fun ExpressLate() {
        val created = 1_000_000L
        val delivery = created + 4 * 86_400_000L
        val s = ExpressShipment("in transit", "E1", arrayListOf(), arrayListOf(), delivery, "Hub", created)

        s.validateExpectedDeliveryDate()

        assertTrue(s.notes.any { it.contains("express shipment was updated") })
    }

    @Test
    fun ExpressLateDelayed() {
        val created = 1_000_000L
        val delivery = created + 4 * 86_400_000L
        val s = ExpressShipment("delayed", "E2", arrayListOf(), arrayListOf(), delivery, "Hub", created)

        s.validateExpectedDeliveryDate()

        assertTrue(s.notes.isEmpty())
    }

    @Test
    fun OvernightLate() {
        val created = 1_000_000L
        val delivery = created + 2 * 86_400_000L
        val s = OvernightShipment("in transit", "O1", arrayListOf(), arrayListOf(), delivery, "Airport", created)

        s.validateExpectedDeliveryDate()

        assertTrue(s.notes.any { it.contains("overnight shipment was updated") })
    }

    @Test
    fun OvernightLateDelayed() {
        val created = 1_000_000L
        val delivery = created + 2 * 86_400_000L
        val s = OvernightShipment("delayed", "O2", arrayListOf(), arrayListOf(), delivery, "Airport", created)

        s.validateExpectedDeliveryDate()

        assertTrue(s.notes.isEmpty())
    }
}
