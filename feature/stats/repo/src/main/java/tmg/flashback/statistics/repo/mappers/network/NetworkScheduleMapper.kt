package tmg.flashback.statistics.repo.mappers.network

import tmg.flashback.statistics.network.models.overview.OverviewRace
import tmg.flashback.statistics.network.models.races.Race
import tmg.flashback.statistics.room.models.overview.Schedule
import kotlin.jvm.Throws

class NetworkScheduleMapper {

    @Throws
    fun mapSchedules(race: OverviewRace): List<Schedule> {
        return race.schedule?.map {
            mapSchedule(race.season, race.round, it)
        } ?: emptyList()
    }

    @Throws(RuntimeException::class)
    fun mapSchedules(race: Race): List<Schedule> {
        return race.schedule?.map {
            mapSchedule(race.data.season, race.data.round, it)
        } ?: emptyList()
    }

    @Throws(RuntimeException::class)
    fun mapSchedule(season: Int, round: Int, data: tmg.flashback.statistics.network.models.overview.Schedule): Schedule {
        return Schedule(
            season = season,
            round = round,
            label = data.label,
            date = data.date,
            time = data.time
        )
    }
}