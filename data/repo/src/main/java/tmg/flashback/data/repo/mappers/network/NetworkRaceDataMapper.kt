package tmg.flashback.data.repo.mappers.network

import tmg.flashback.data.persistence.models.race.RaceInfo
import tmg.flashback.flashbackapi.api.models.races.RaceData
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