package tmg.flashback.statistics.repo.mappers.network

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.NetworkCircuitHistory
import tmg.flashback.statistics.network.models.circuits.CircuitResult
import tmg.flashback.statistics.network.models.circuits.model
import tmg.flashback.statistics.room.models.circuit.*

internal class NetworkCircuitMapperTest {

    private val mockNetworkCircuitDataMapper: NetworkCircuitDataMapper = mockk(relaxed = true)

    private lateinit var sut: NetworkCircuitMapper

    @BeforeEach
    internal fun setUp() {
        sut = NetworkCircuitMapper(mockNetworkCircuitDataMapper)

        every { mockNetworkCircuitDataMapper.mapCircuitData(any()) } returns Circuit.model()
    }

    @Test
    fun `mapCircuit maps fields correctly`() {
        val input = NetworkCircuitHistory.model()
        val expected = CircuitHistory.model()

        assertEquals(expected, sut.mapCircuit(input))
    }

    @Test
    fun `mapCircuit returns null if input is null`() {
        assertNull(sut.mapCircuit(null))
    }

    @Test
    fun `mapCircuit with null results map has empty list of races`() {
        val input = NetworkCircuitHistory.model(results = null)
        assertEquals(emptyList<CircuitRoundWithResults>(), sut.mapCircuit(input)!!.races)
    }

    @Test
    fun `mapCircuitRounds maps fields correctly`() {
        val inputCircuitId: String = "circuitId"
        val inputCircuitRound: CircuitResult = CircuitResult.model()
        val expected = CircuitRound.model()

        assertEquals(expected, sut.mapCircuitRounds(inputCircuitId, inputCircuitRound))
    }
}