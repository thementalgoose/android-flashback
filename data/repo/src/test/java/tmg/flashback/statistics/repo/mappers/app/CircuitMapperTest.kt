package tmg.flashback.statistics.repo.mappers.app

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.formula1.model.Circuit
import tmg.flashback.formula1.model.CircuitHistory
import tmg.flashback.formula1.model.model
import tmg.flashback.statistics.room.models.circuit.model

internal class CircuitMapperTest {

    private lateinit var underTest: CircuitMapper

    @BeforeEach
    internal fun setUp() {
        underTest = CircuitMapper()
    }

    @Test
    fun `mapCircuit maps fields correctly`() {
        val input = tmg.flashback.statistics.room.models.circuit.Circuit.model()
        val expected = Circuit.model()

        assertEquals(expected, underTest.mapCircuit(input))
    }

    @Test
    fun `mapCircuit maps location to null if lat is null`() {
        val input = tmg.flashback.statistics.room.models.circuit.Circuit.model(locationLat = null)

        assertNull(underTest.mapCircuit(input)!!.location)
    }

    @Test
    fun `mapCircuit maps location to null if lng is null`() {
        val input = tmg.flashback.statistics.room.models.circuit.Circuit.model(locationLng = null)

        assertNull(underTest.mapCircuit(input)!!.location)
    }

    @Test
    fun `mapCircuitHistory maps fields correctly`() {
        val input = tmg.flashback.statistics.room.models.circuit.CircuitHistory.model()
        val expected = CircuitHistory.model()

        assertEquals(expected, underTest.mapCircuitHistory(input))
    }

    @Test
    fun `mapCircuitHistory returns null when input is null`() {
        assertNull(underTest.mapCircuitHistory(null))
    }
}