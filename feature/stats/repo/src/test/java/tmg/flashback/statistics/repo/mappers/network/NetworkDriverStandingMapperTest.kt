package tmg.flashback.statistics.repo.mappers.network

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.statistics.network.models.races.DriverStandings
import tmg.flashback.statistics.network.models.races.model
import tmg.flashback.statistics.room.models.standings.DriverStanding
import tmg.flashback.statistics.room.models.standings.DriverStandingConstructor
import tmg.flashback.statistics.room.models.standings.model

internal class NetworkDriverStandingMapperTest {

    private lateinit var sut: NetworkDriverStandingMapper

    @BeforeEach
    internal fun setUp() {
        sut = NetworkDriverStandingMapper()
    }

    @Test
    fun `mapDriverStanding maps fields correctly`() {
        val inputSeason = 2020
        val inputData = DriverStandings.model()
        val expected = DriverStanding.model()

        assertEquals(expected, sut.mapDriverStanding(inputSeason, inputData))
    }

    @Test
    fun `mapDriverStanding null progress defaults to false`() {
        val inputSeason = 2020
        val inputData = DriverStandings.model(inProgress = null)

        assertFalse(sut.mapDriverStanding(inputSeason, inputData).inProgress)
    }

    @Test
    fun `mapDriverStandingConstructor maps fields correctly`() {
        val inputSeason = 2020
        val inputData = DriverStandings.model()
        val expected = listOf(DriverStandingConstructor.model())

        assertEquals(expected, sut.mapDriverStandingConstructor(inputSeason, inputData))
    }
}