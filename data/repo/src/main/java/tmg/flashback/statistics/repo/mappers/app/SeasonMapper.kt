package tmg.flashback.statistics.repo.mappers.app

import tmg.flashback.formula1.model.Season

class SeasonMapper(
    private val raceMapper: RaceMapper
) {
    fun mapSeason(season: Int, races: List<tmg.flashback.statistics.room.models.race.Race>): Season {
        return Season(
            season = season,
            races = races.mapNotNull { raceMapper.mapRace(it) }
        )
    }
}