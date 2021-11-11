package tmg.flashback.statistics.repo.mappers.network

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.NetworkDriver
import tmg.flashback.statistics.network.models.drivers.model
import tmg.flashback.statistics.room.models.drivers.Driver
import tmg.flashback.statistics.room.models.drivers.model

internal class NetworkDriverDataMapperTest {

    private lateinit var sut: NetworkDriverDataMapper

    @BeforeEach
    internal fun setUp() {
        sut = NetworkDriverDataMapper()
    }

    @Test
    fun `mapDriverData maps fields correctly`() {
        val input = NetworkDriver.model()
        val expected = Driver.model()

        assertEquals(expected, sut.mapDriverData(input))
    }

    @Test
    fun `mapDriverData invalid permanent number is set to null`() {
        val input = NetworkDriver.model(permanentNumber = "invalid")

        assertNull(sut.mapDriverData(input).number)
    }
}