package tmg.flashback.statistics.repo.mappers.network

import androidx.room.ColumnInfo
import tmg.flashback.statistics.network.models.races.RaceData
import tmg.flashback.statistics.room.models.round.Round
import java.lang.RuntimeException
import kotlin.jvm.Throws

class NetworkRaceDataMapper {

    @Throws(RuntimeException::class)
    fun mapRaceData(raceData: RaceData): Round {
        return Round(
            season = raceData.season,
            round = raceData.round,
            name = raceData.name,
            date = raceData.date,
            circuitId = raceData.circuit.id,
            time = raceData.time,
            wikiUrl = raceData.wikiUrl,
        )
    }
}