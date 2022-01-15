package tmg.flashback.statistics.repo.mappers.network

import tmg.flashback.statistics.network.models.races.RaceData
import tmg.flashback.statistics.room.models.race.RaceInfo

class NetworkRaceDataMapper {

    @Throws(RuntimeException::class)
    fun mapRaceData(raceData: RaceData): RaceInfo {
        return RaceInfo(
            season = raceData.season,
            round = raceData.round,
            name = raceData.name,
            date = raceData.date,
            circuitId = raceData.circuit.id,
            time = raceData.time,
            wikiUrl = raceData.wikiUrl,
            youtube = raceData.youtubeUrl
        )
    }
}