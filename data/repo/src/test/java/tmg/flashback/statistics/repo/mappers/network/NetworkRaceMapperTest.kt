package tmg.flashback.statistics.repo.mappers.network

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.NetworkQualifyingResult
import tmg.flashback.NetworkRaceResult
import tmg.flashback.NetworkSprintResult
import tmg.flashback.statistics.network.models.races.SprintResult
import tmg.flashback.statistics.network.models.races.model
import tmg.flashback.statistics.room.models.race.QualifyingResult
import tmg.flashback.statistics.room.models.race.RaceResult
import tmg.flashback.statistics.room.models.overview.model
import tmg.flashback.statistics.room.models.race.model

internal class NetworkRaceMapperTest {

    private lateinit var sut: NetworkRaceMapper

    @BeforeEach
    internal fun setUp() {
        sut = NetworkRaceMapper()
    }

    @Test
    fun `mapRaceResults maps fields correctly`() {
        val inputSeason = 2020
        val inputRound = 1
        val inputData = NetworkRaceResult.model()
        val expected = RaceResult.model()

        assertEquals(expected, sut.mapRaceResults(inputSeason, inputRound, inputData))
    }

    @Test
    fun `mapRaceResults null fastest laps is null model`() {
        val inputSeason = 2020
        val inputRound = 1
        val inputData = NetworkRaceResult.model(fastestLap = null)

        assertNull(sut.mapRaceResults(inputSeason, inputRound, inputData).fastestLap)
    }

    @Test
    fun `mapQualifyingResults maps fields correctly`() {
        val inputSeason = 2020
        val inputRound = 1
        val inputData = NetworkQualifyingResult.model()
        val expected = QualifyingResult.model()

        assertEquals(expected, sut.mapQualifyingResults(inputSeason, inputRound, inputData))
    }

    @Test
    fun `mapSprintResults maps fields correctly`() {
        val inputSeason = 2020
        val inputRound = 1
        val inputData = NetworkSprintResult.model()
        val expected = SprintResult.model()

        assertEquals(expected, sut.mapSprintResults(inputSeason, inputRound, inputData))
    }
}