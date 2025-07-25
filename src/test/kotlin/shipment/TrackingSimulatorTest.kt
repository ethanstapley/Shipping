package shipment

import kotlin.test.*
import java.io.File
import kotlinx.coroutines.test.runTest

class TrackingSimulatorTest {

    @Test
    fun addShipmentStoreShipmentInMap() {
        val simulator = TrackingServer()
        val shipment = createTestShipment()

        simulator.addShipment(shipment)

        assertEquals(shipment, simulator.findShipment("abc"))
    }

    @Test
    fun findShipmentReturnNull() {
        val simulator = TrackingServer()
        assertNull(simulator.findShipment("not real id"))
    }

    @Test
    fun testCreateAndUpdateCorrectly() = runTest {
        val simulator = TrackingServer()

        val input = """
        created,abc,123
        shipped,abc,456,888888
        noteadded,abc,789,Handle with care
    """.trimIndent()

        val tempFile = File.createTempFile("test_sim", ".txt")
        tempFile.writeText(input)

        simulator.runSimulation(tempFile.absolutePath)

        val shipment = simulator.findShipment("abc")!!
        assertEquals("shipped", shipment.status)
        assertEquals("Handle with care", shipment.notes.first())
        assertEquals(2, shipment.updateHistory.size)

        tempFile.deleteOnExit()
    }


    @Test
    fun ignoreInvalidLines() = runTest {
        val simulator = TrackingServer()

        val input = """
            created,abc,123
            badline
            unknown,abc,456,info
            test,test,test,test,test,test
            test,test,test,test,test
        """.trimIndent()

        val tempFile = File.createTempFile("invalid_test_sim", ".txt")
        tempFile.writeText(input)

        simulator.runSimulation(tempFile.absolutePath)

        val shipment = simulator.findShipment("abc")
        assertNotNull(shipment)
        assertEquals("created", shipment!!.status)

        tempFile.deleteOnExit()
    }

    @Test
    fun filePathDoesNotExistShouldNotThrow() = runTest {
        val simulator = TrackingServer()
        simulator.runSimulation("/fake/path")
        // used to get test with coverage. making sure it doesn't crash
    }

    @Test
    fun testDuplicateCreateIds() = runTest {
        val simulator = TrackingServer()

        val input = """
        created,abc,123
        created,abc,123
    """.trimIndent()

        val tempFile = File.createTempFile("test_sim", ".txt")
        tempFile.writeText(input)

        simulator.runSimulation(tempFile.absolutePath)

        val shipment = simulator.findShipment("abc")!!
        assertEquals("created", shipment.status)

        tempFile.deleteOnExit()
    }

    @Test
    fun testNullShipmentUpdateFinder() = runTest {
        val simulator = TrackingServer()

        val input = """
        shipped,def,456,888888
    """.trimIndent()

        val tempFile = File.createTempFile("test_sim", ".txt")
        tempFile.writeText(input)

        simulator.runSimulation(tempFile.absolutePath)

        val shipment = simulator.findShipment("def")
        assertNull(shipment)

        tempFile.deleteOnExit()
    }

}
