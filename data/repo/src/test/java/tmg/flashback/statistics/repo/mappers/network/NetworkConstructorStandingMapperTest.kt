package tmg.flashback.statistics.repo.mappers.network

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.statistics.network.models.races.ConstructorStandings
import tmg.flashback.statistics.network.models.races.model
import tmg.flashback.statistics.room.models.standings.ConstructorStanding
import tmg.flashback.statistics.room.models.standings.ConstructorStandingDriver
import tmg.flashback.statistics.room.models.standings.model

internal class NetworkConstructorStandingMapperTest {

    private lateinit var sut: NetworkConstructorStandingMapper

    @BeforeEach
    internal fun setUp() {
        sut = NetworkConstructorStandingMapper()
    }

    @Test
    fun `mapConstructorStanding maps fields correctly`() {
        val inputSeason: Int = 2020
        val inputStandings = ConstructorStandings.model()
        val expected = ConstructorStanding.model()

        assertEquals(expected, sut.mapConstructorStanding(inputSeason, inputStandings))
    }

    @Test
    fun `mapConstructorStanding null progress defaults to false`() {
        val inputSeason: Int = 2020
        val inputStandings = ConstructorStandings.model(inProgress = null)

        assertFalse(sut.mapConstructorStanding(inputSeason, inputStandings).inProgress)
    }

    @Test
    fun `mapConstructorStandingDriver maps fields correctly`() {
        val inputSeason: Int = 2020
        val inputStandings = ConstructorStandings.model()
        val expected = listOf(ConstructorStandingDriver.model())

        assertEquals(expected, sut.mapConstructorStandingDriver(inputSeason, inputStandings))
    }
}