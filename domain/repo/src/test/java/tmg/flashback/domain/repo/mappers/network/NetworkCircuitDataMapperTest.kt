package tmg.flashback.domain.repo.mappers.network

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.NetworkCircuit
import tmg.flashback.flashbackapi.api.models.circuits.model
import tmg.flashback.domain.persistence.models.circuit.Circuit
import tmg.flashback.domain.persistence.models.circuit.model

internal class NetworkCircuitDataMapperTest {

    private lateinit var sut: NetworkCircuitDataMapper

    @BeforeEach
    internal fun setUp() {
        sut = NetworkCircuitDataMapper()
    }

    @Test
    fun `mapCircuitData maps fields correctly`() {
        val input = NetworkCircuit.model()
        val expected = Circuit.model()

        assertEquals(expected, sut.mapCircuitData(input))
    }
}