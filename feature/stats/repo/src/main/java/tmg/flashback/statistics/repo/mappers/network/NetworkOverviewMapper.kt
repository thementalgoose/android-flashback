package tmg.flashback.statistics.repo.mappers.network

import tmg.flashback.statistics.network.models.overview.OverviewRace
import tmg.flashback.statistics.room.models.overview.Overview
import java.lang.RuntimeException
import kotlin.jvm.Throws

class NetworkOverviewMapper {
    @Throws(RuntimeException::class)
    fun mapOverview(overview: OverviewRace?): Overview? {
        if (overview == null) return null
        return Overview(
            season = overview.season,
            round = overview.round,
            name = overview.name,
            circuitId = overview.circuit.id,
            date = overview.date,
            time = overview.time,
            hasRace = overview.hasRace,
            hasQualifying = overview.hasQualifying
        )
    }
}