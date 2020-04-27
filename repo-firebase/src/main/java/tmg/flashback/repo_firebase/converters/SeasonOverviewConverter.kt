package tmg.flashback.repo_firebase.converters

import tmg.flashback.repo.enums.RaceStatus
import tmg.flashback.repo.enums.raceStatusUnknown
import tmg.flashback.repo.models.*
import tmg.flashback.repo.utils.toLapTime
import tmg.flashback.repo.utils.toMaxIfZero
import tmg.flashback.repo_firebase.models.*

fun FSeason.convert(season: Int): Season {
    return Season(
        season = season,
        drivers = (this.drivers ?: mapOf())
            .values
            .map { it.convert() },
        constructors = (this.constructors ?: mapOf())
            .values
            .map { it.convert() },
        rounds = (this.race ?: mapOf())
            .values
            .map {
                it.convert(this.drivers ?: mapOf(), this.constructors ?: mapOf())
            }
    )
}

fun FRound.convert(
    drivers: Map<String, FSeasonOverviewDriver>,
    constructors: Map<String, FSeasonOverviewConstructor>
): Round {
    return Round(
        season = season,
        round = round,
        date = fromDate(date),
        time = fromTime(time),
        name = name,
        circuit = circuit.convert(),
        q1 = qualifying.onResult(drivers, constructors, driverCon ?: emptyMap()) { it.q1 },
        q2 = qualifying.onResult(drivers, constructors, driverCon ?: emptyMap()) { it.q2 },
        q3 = qualifying.onResult(drivers, constructors, driverCon ?: emptyMap()) { it.q3 },
        race = (race ?: mapOf())
            .map { (driverId, raceResult) ->
                driverId to RoundRaceResult(
                    driver = drivers.values.first { it.id == driverId }.convert(constructors, driverCon?.toList()?.firstOrNull { it.first == driverId }?.second),
                    time = raceResult.time?.toLapTime(),
                    points = raceResult.points ?: 0,
                    grid = raceResult.grid ?: 0,
                    qualified = getQualified(raceResult),
                    finish = raceResult.result ?: 0,
                    status = raceResult.status ?: raceStatusUnknown,
                    fastestLap = raceResult.fastestLap?.convert()
                )
            }
            .toMap()
    )
}

private fun getQualified(raceResult: FSeasonOverviewRaceRace): Int? {
    if (raceResult.qualified != -1 && raceResult.qualified != 0) {
        return raceResult.qualified
    }
    if (raceResult.grid != null && raceResult.grid != -1 && raceResult.grid != 0) {
        return raceResult.grid
    }
    return null
}

private fun Map<String, FSeasonOverviewRaceQualifying>?.onResult(
    drivers: Map<String, FSeasonOverviewDriver>,
    constructors: Map<String, FSeasonOverviewConstructor>,
    driverOverrideMap: Map<String, String>,
    callback: (race: FSeasonOverviewRaceQualifying) -> String?
): Map<String, RoundQualifyingResult> {
    return (this ?: mapOf())
        .toSortedMap()
        .toList()
        .map { Triple(it.first, it.second, callback(it.second)?.toLapTime()) }
        .sortedBy { (_, _, lapTime) -> lapTime?.totalMillis.toMaxIfZero() }
        .mapIndexed { index, triplet ->
            val (driverId, item, lapTime) = triplet
            return@mapIndexed driverId to RoundQualifyingResult(
                driver = drivers.values.first { it.id == driverId }.convert(constructors, driverOverrideMap.toList().firstOrNull { it.first == driverId }?.second),
                time = lapTime,
                position = index + 1
            )
        }
        .toMap()
}
private fun FSeasonOverviewRaceRaceFastestLap.convert(): FastestLap {
    return FastestLap(
        rank = pos,
        lap = lap,
        lapTime = time.toLapTime()
    )
}