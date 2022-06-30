package tmg.flashback.statistics.repo.mappers.network

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.statistics.network.models.circuits.CircuitResult
import tmg.flashback.statistics.network.models.circuits.model
import tmg.flashback.statistics.room.models.circuit.CircuitRound
import tmg.flashback.statistics.room.models.circuit.CircuitRoundResult
import tmg.flashback.statistics.room.models.circuit.model

internal class NetworkCircuitMapperTest {

    private lateinit var sut: NetworkCircuitMapper

    @BeforeEach
    internal fun setUp() {
        sut = NetworkCircuitMapper()
    }

    @Test
    fun `map circuit rounds maps fields correctly`() {
        val input = CircuitResult.model()
        val expected = CircuitRound.model()

        assertEquals(expected, sut.mapCircuitRounds(expected.circuitId, input))
    }

    @Test
    fun `map circuit previews maps fields correctly`() {
        val input = CircuitResult.model()
        val expected = listOf(CircuitRoundResult.model())

        assertEquals(expected, sut.mapCircuitPreviews(input))
    }

    @Test
    fun `map circuit previews returns empty list when mapping null`() {
        assertEquals(emptyList<CircuitRoundResult>(), sut.mapCircuitPreviews(CircuitResult.model(preview = null)))
    }
}