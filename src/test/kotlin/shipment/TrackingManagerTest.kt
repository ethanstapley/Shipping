package shipment

import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.*

class TrackingManagerTest {
    private lateinit var manager: TrackingManager

    @BeforeTest
    fun setup() {
        manager = TrackingManager()
    }

    @Test
    fun invalidLineTooShort() {
        manager.processLine("created,abc")
        assertNull(manager.findShipment("abc"))
    }

    @Test
    fun invalidLineTooLong() {
        manager.processLine("created,abc,123,info,extra")
        assertNull(manager.findShipment("abc"))
    }

    @Test
    fun createShipmentSuccess() {
        manager.processLine("created,S1,1000,standard")
        val s = manager.findShipment("S1")
        assertNotNull(s)
        assertEquals("S1", s.id)
    }

    @Test
    fun createShipmentDuplicate() {
        manager.processLine("created,S2,2000,standard")
        manager.processLine("created,S2,3000,standard")
        assertNotNull(manager.findShipment("S2"))
    }

    @Test
    fun updateUnknownShipment() {
        manager.processLine("delayed,NOTFOUND,12345")
        assertNull(manager.findShipment("NOTFOUND"))
    }

    @Test
    fun updateUnknownType() {
        manager.processLine("created,S3,4000,standard")
        manager.processLine("notrealupdate,S3,5000")
        assertEquals("created", manager.findShipment("S3")?.status)
    }

    @Test
    fun validUpdateApplies() {
        manager.processLine("created,S4,6000,standard")
        manager.processLine("delayed,S4,1652712855468,1652718051403")
        manager.processLine("lost,S4,1652712855468")
        val s = manager.findShipment("S4")!!
        assertEquals("lost", s.status)
        assertEquals(2, s.updateHistory.size)
    }

    @Test
    fun addShipmentStoreShipmentInMap() {
        val simulator = TrackingManager()
        val shipment = createTestShipment()

        simulator.addShipment(shipment)

        assertEquals(shipment, simulator.findShipment("abc"))
    }

    @Test
    fun findShipmentReturnNull() {
        val simulator = TrackingManager()
        assertNull(simulator.findShipment("not real id"))
    }
}