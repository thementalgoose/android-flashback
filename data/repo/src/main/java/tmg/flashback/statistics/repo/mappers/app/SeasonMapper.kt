package tmg.flashback.statistics.repo.mappers.app

import tmg.flashback.formula1.model.Season
import tmg.flashback.statistics.room.models.overview.WinterTesting

class SeasonMapper(
    private val raceMapper: RaceMapper,
    private val winterTestingMapper: EventMapper
) {
    fun mapSeason(
        season: Int,
        races: List<tmg.flashback.statistics.room.models.race.Race>,
        winterTesting: List<WinterTesting>
    ): Season {
        return Season(
            season = season,
            races = races.mapNotNull { raceMapper.mapRace(it) },
            event = winterTesting.mapNotNull { winterTestingMapper.mapEvent(it) }
        )
    }
}