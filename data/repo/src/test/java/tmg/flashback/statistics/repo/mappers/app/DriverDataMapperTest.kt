package tmg.flashback.statistics.repo.mappers.app

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.RoomDriver
import tmg.flashback.formula1.model.Driver
import tmg.flashback.formula1.model.model
import tmg.flashback.statistics.room.models.drivers.model

internal class DriverDataMapperTest {

    private lateinit var sut: DriverDataMapper

    @BeforeEach
    internal fun setUp() {
        sut = DriverDataMapper()
    }

    @Test
    fun `mapDriver maps fields correctly`() {
        val input = RoomDriver.model()
        val expected = Driver.model()

        assertEquals(expected, sut.mapDriver(input))
    }
}