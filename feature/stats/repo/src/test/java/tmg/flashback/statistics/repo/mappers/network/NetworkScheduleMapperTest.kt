package tmg.flashback.statistics.repo.mappers.network

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.NetworkSchedule
import tmg.flashback.RoomSchedule
import tmg.flashback.statistics.network.models.overview.OverviewRace
import tmg.flashback.statistics.network.models.overview.model
import tmg.flashback.statistics.network.models.races.Race
import tmg.flashback.statistics.network.models.races.model
import tmg.flashback.statistics.room.models.overview.Schedule
import tmg.flashback.statistics.room.models.overview.model

internal class NetworkScheduleMapperTest {

    private lateinit var sut: NetworkScheduleMapper

    @BeforeEach
    internal fun setUp() {
        sut = NetworkScheduleMapper()
    }

    @Test
    fun `mapSchedule race overview maps fields correctly`() {
        val input = OverviewRace.model()
        val expected: List<Schedule> = listOf(Schedule.model())

        assertEquals(expected, sut.mapSchedules(input))
    }

    @Test
    fun `mapSchedule race maps fields correctly`() {
        val input = Race.model()
        val expected: List<Schedule> = listOf(Schedule.model())

        assertEquals(expected, sut.mapSchedules(input))
    }

    @Test
    fun `mapSchedule schedule maps fields correctly`() {
        val inputSeason = 2020
        val inputRound = 1
        val input = NetworkSchedule.model()
        val expected = RoomSchedule.model()

        assertEquals(expected, sut.mapSchedule(inputSeason, inputRound, input))
    }
}