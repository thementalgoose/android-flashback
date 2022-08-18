package tmg.flashback.statistics.repo.mappers.network

import tmg.flashback.statistics.network.models.races.RaceData
import tmg.flashback.statistics.room.models.race.RaceInfo
import javax.inject.Inject

class NetworkRaceDataMapper @Inject constructor() {

    @Throws(RuntimeException::class)
    fun mapRaceData(raceData: RaceData): RaceInfo {
        return RaceInfo(
            season = raceData.season,
            round = raceData.round,
            name = raceData.name,
            date = raceData.date,
            laps = raceData.laps,
            circuitId = raceData.circuit.id,
            time = raceData.time,
            wikiUrl = raceData.wikiUrl,
            youtube = raceData.youtubeUrl
        )
    }
}