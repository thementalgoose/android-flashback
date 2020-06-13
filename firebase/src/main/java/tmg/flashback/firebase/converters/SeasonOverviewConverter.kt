package tmg.flashback.firebase.converters

import tmg.flashback.repo.enums.raceStatusUnknown
import tmg.flashback.repo.models.stats.*
import tmg.flashback.repo.utils.toLapTime
import tmg.flashback.repo.utils.toMaxIfZero
import tmg.flashback.firebase.models.*

fun FSeason.convert(season: Int): Season {
    return Season(
        season = season,
        drivers = (this.drivers ?: mapOf())
            .values
            .map { it.convert(this.constructorAtEndOfSeason(it.id)) },
        constructors = (this.constructors ?: mapOf())
            .values
            .map { it.convert() },
        rounds = (this.race ?: mapOf())
            .values
            .map {
                it.convert(this)
            }
    )
}

fun FRound.convert(
    fSeason: FSeason
): Round {
    return Round(
        season = season,
        round = round,
        date = fromDate(date),
        time = fromTime(time),
        name = name,
        circuit = circuit.convert(),
        q1 = qualifying.onResult(fSeason, driverCon ?: emptyMap()) { it.q1 },
        q2 = qualifying.onResult(fSeason, driverCon ?: emptyMap()) { it.q2 },
        q3 = qualifying.onResult(fSeason, driverCon ?: emptyMap()) { it.q3 },
        race = (race ?: mapOf())
            .map { (driverId, raceResult) ->
                driverId to RoundRaceResult(
                    driver = (fSeason.drivers ?: emptyMap()).values.first { it.id == driverId }
                        .convert(
                            fSeason.constructors ?: emptyMap(),
                            driverCon?.toList()?.firstOrNull { it.first == driverId }?.second,
                            fSeason.constructorAtEndOfSeason(driverId)
                        ),
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
    season: FSeason,
    driverOverrideMap: Map<String, String>,
    callback: (race: FSeasonOverviewRaceQualifying) -> String?
): Map<String, RoundQualifyingResult> {

    val drivers: Map<String, FSeasonOverviewDriver> = season.drivers ?: emptyMap()
    val constructors: Map<String, FSeasonOverviewConstructor> = season.constructors ?: emptyMap()
    return (this ?: mapOf())
        .toSortedMap()
        .toList()
        .map { Triple(it.first, it.second, callback(it.second)?.toLapTime()) }
        .sortedBy { (_, _, lapTime) -> lapTime?.totalMillis.toMaxIfZero() }
        .mapIndexed { index, triplet ->
            val (driverId, item, lapTime) = triplet
            val driver = drivers.values.first { it.id == driverId }.convert(constructors, driverOverrideMap.toList().firstOrNull { it.first == driverId }?.second, season.constructorAtEndOfSeason(driverId))
            return@mapIndexed driverId to RoundQualifyingResult(
                driver = driver,
                time = lapTime,
                position = (if (lapTime == null) (item.pos) else null) ?: (index + 1)
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

private fun FSeason.constructorAtEndOfSeason(driverId: String): Constructor {
    val constructorId: String? = this.race
        ?.filter { it.value.driverCon?.get(driverId) != null }
        ?.maxBy { it.value.round }
        ?.value
        ?.driverCon?.get(driverId)
    return this.constructors?.get(constructorId)!!.convert()
}