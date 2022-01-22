package tmg.flashback.statistics.repo.mappers.app

import tmg.flashback.formula1.model.Season
import tmg.flashback.statistics.room.models.overview.Event

class SeasonMapper(
    private val raceMapper: RaceMapper,
    private val eventMapper: EventMapper
) {
    fun mapSeason(
        season: Int,
        races: List<tmg.flashback.statistics.room.models.race.Race>,
        events: List<Event>
    ): Season {
        return Season(
            season = season,
            races = races.mapNotNull { raceMapper.mapRace(it) },
            event = events.mapNotNull { eventMapper.mapEvent(it) }
        )
    }
}