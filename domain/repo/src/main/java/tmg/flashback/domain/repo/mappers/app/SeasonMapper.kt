package tmg.flashback.domain.repo.mappers.app

import tmg.flashback.formula1.model.Season
import tmg.flashback.domain.persistence.models.overview.Event
import javax.inject.Inject

class SeasonMapper @Inject constructor(
    private val raceMapper: RaceMapper,
    private val eventMapper: EventMapper
) {
    fun mapSeason(
        season: Int,
        races: List<tmg.flashback.domain.persistence.models.race.Race>,
        events: List<Event>
    ): Season {
        return Season(
            season = season,
            races = races.mapNotNull { raceMapper.mapRace(it) },
            event = events.mapNotNull { eventMapper.mapEvent(it) }
        )
    }
}