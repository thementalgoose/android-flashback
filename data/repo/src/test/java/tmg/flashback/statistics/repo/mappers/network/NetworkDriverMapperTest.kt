package tmg.flashback.statistics.repo.mappers.network

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.statistics.network.models.drivers.DriverHistoryStanding
import tmg.flashback.statistics.network.models.drivers.DriverHistoryStandingRace
import tmg.flashback.statistics.network.models.drivers.model
import tmg.flashback.statistics.room.models.drivers.DriverSeason
import tmg.flashback.statistics.room.models.drivers.DriverSeasonRace
import tmg.flashback.statistics.room.models.drivers.model

internal class NetworkDriverMapperTest {

    private lateinit var sut: NetworkDriverMapper

    @BeforeEach
    internal fun setUp() {
        sut = NetworkDriverMapper()
    }

    @Test
    fun `mapDriverSeason maps fields correctly`() {
        val inputDriverId: String = "driverId"
        val inputDriver = DriverHistoryStanding.model()
        val expected = DriverSeason.model()

        assertEquals(expected, sut.mapDriverSeason(inputDriverId, inputDriver))
    }

    @Test
    fun `mapDriverSeasonRace maps fields correctly`() {
        val inputDriverId: String = "driverId"
        val inputSeason: Int = 2020
        val inputData = DriverHistoryStandingRace.model()
        val expected = DriverSeasonRace.model()

        assertEquals(expected, sut.mapDriverSeasonRace(inputDriverId, inputSeason, inputData))
    }

    @Test
    fun `mapDriverSeasonRace null sprint quali defaults to false`() {
        val inputDriverId: String = "driverId"
        val inputSeason: Int = 2020
        val inputData = DriverHistoryStandingRace.model(sprintQuali = null)

        assertFalse(sut.mapDriverSeasonRace(inputDriverId, inputSeason, inputData).sprintQuali)
    }
}