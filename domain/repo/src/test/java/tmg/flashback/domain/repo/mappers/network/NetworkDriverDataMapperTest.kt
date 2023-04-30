package tmg.flashback.domain.repo.mappers.network

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.NetworkDriver
import tmg.flashback.flashbackapi.api.models.drivers.model
import tmg.flashback.domain.persistence.models.drivers.Driver
import tmg.flashback.domain.persistence.models.drivers.model

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