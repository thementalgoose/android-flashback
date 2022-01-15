package tmg.flashback.statistics.repo.mappers.network

import tmg.flashback.statistics.network.models.races.SprintQualifyingResult
import tmg.flashback.statistics.room.models.race.FastestLap
import tmg.flashback.statistics.room.models.race.QualifyingResult
import tmg.flashback.statistics.room.models.race.QualifyingSprintResult
import tmg.flashback.statistics.room.models.race.RaceResult

class NetworkRaceMapper {

    @Throws(RuntimeException::class)
    fun mapRaceResults(season: Int, round: Int, data: tmg.flashback.statistics.network.models.races.RaceResult): RaceResult {
        return RaceResult(
            driverId = data.driverId,
            season = season,
            round = round,
            constructorId = data.constructorId,
            points = data.points,
            qualified = data.qualified,
            gridPos = data.gridPos,
            finished = data.finished,
            status = data.status,
            time = data.time,
            fastestLap = mapFastestLap(data.fastestLap)
        )
    }

    @Throws(RuntimeException::class)
    fun mapQualifyingResults(season: Int, round: Int, data: tmg.flashback.statistics.network.models.races.QualifyingResult): QualifyingResult {
        return QualifyingResult(
            driverId = data.driverId,
            season = season,
            round = round,
            constructorId = data.constructorId,
            qualified = data.qualified,
            q1 = data.q1,
            q2 = data.q2,
            q3 = data.q3,
            qSprint = mapSprintQualifying(data.qSprint)
        )
    }

    private fun mapFastestLap(data: tmg.flashback.statistics.network.models.races.FastestLap?): FastestLap? {
        if (data == null) return null
        return FastestLap(
            position = data.position,
            time = data.time
        )
    }

    private fun mapSprintQualifying(data: SprintQualifyingResult?): QualifyingSprintResult? {
        if (data == null) return null
        return QualifyingSprintResult(
            points = data.points,
            gridPos = data.gridPos,
            finished = data.finished,
            status = data.status,
            time = data.time,
        )
    }
}