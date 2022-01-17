package tmg.flashback.statistics.repo.mappers.network

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.statistics.network.models.constructors.ConstructorHistoryStanding
import tmg.flashback.statistics.network.models.constructors.ConstructorHistoryStandingDriver
import tmg.flashback.statistics.network.models.constructors.model
import tmg.flashback.statistics.room.models.constructors.ConstructorSeason
import tmg.flashback.statistics.room.models.constructors.ConstructorSeasonDriver
import tmg.flashback.statistics.room.models.constructors.model

internal class NetworkConstructorMapperTest {

    private lateinit var sut: NetworkConstructorMapper

    @BeforeEach
    internal fun setUp() {
        sut = NetworkConstructorMapper()
    }

    @Test
    fun `mapConstructorSeason maps fields correctly`() {
        val inputConstructorId = "constructorId"
        val inputStanding = ConstructorHistoryStanding.model()
        val expected = ConstructorSeason.model()

        assertEquals(expected, sut.mapConstructorSeason(inputConstructorId, inputStanding))
    }

    @Test
    fun `mapConstructorSeason null points defaults to 0`() {
        val inputConstructorId = "constructorId"
        val inputStanding = ConstructorHistoryStanding.model(points = null)

        assertEquals(0.0, sut.mapConstructorSeason(inputConstructorId, inputStanding).points)
    }

    @Test
    fun `mapConstructorSeason null races defaults to 0`() {
        val inputConstructorId = "constructorId"
        val inputStanding = ConstructorHistoryStanding.model(races = null)

        assertEquals(0, sut.mapConstructorSeason(inputConstructorId, inputStanding).races)
    }

    @Test
    fun `mapConstructorSeasonDriver maps fields correctly`() {
        val inputConstructorId = "constructorId"
        val inputSeason = 2020
        val inputStanding = ConstructorHistoryStandingDriver.model()
        val expected = ConstructorSeasonDriver.model()

        assertEquals(expected, sut.mapConstructorSeasonDriver(inputConstructorId, inputSeason, inputStanding))
    }

    @Test
    fun `mapConstructorSeasonDriver null wins defaults to 0`() {
        val inputConstructorId = "constructorId"
        val inputSeason = 2020
        val inputStanding = ConstructorHistoryStandingDriver.model(wins = null)

        assertEquals(0, sut.mapConstructorSeasonDriver(inputConstructorId, inputSeason, inputStanding).wins)
    }

    @Test
    fun `mapConstructorSeasonDriver null races defaults to 0`() {
        val inputConstructorId = "constructorId"
        val inputSeason = 2020
        val inputStanding = ConstructorHistoryStandingDriver.model(races = null)

        assertEquals(0, sut.mapConstructorSeasonDriver(inputConstructorId, inputSeason, inputStanding).races)
    }

    @Test
    fun `mapConstructorSeasonDriver null podiums defaults to 0`() {
        val inputConstructorId = "constructorId"
        val inputSeason = 2020
        val inputStanding = ConstructorHistoryStandingDriver.model(podiums = null)

        assertEquals(0, sut.mapConstructorSeasonDriver(inputConstructorId, inputSeason, inputStanding).podiums)
    }

    @Test
    fun `mapConstructorSeasonDriver null points finishes defaults to 0`() {
        val inputConstructorId = "constructorId"
        val inputSeason = 2020
        val inputStanding = ConstructorHistoryStandingDriver.model(pointsFinishes = null)

        assertEquals(0, sut.mapConstructorSeasonDriver(inputConstructorId, inputSeason, inputStanding).pointsFinishes)
    }

    @Test
    fun `mapConstructorSeasonDriver null pole defaults to 0`() {
        val inputConstructorId = "constructorId"
        val inputSeason = 2020
        val inputStanding = ConstructorHistoryStandingDriver.model(pole = null)

        assertEquals(0, sut.mapConstructorSeasonDriver(inputConstructorId, inputSeason, inputStanding).pole)
    }
}