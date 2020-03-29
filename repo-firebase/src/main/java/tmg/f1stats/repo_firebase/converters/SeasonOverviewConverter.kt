package tmg.f1stats.repo_firebase.converters

import tmg.f1stats.repo.enums.RaceStatus
import tmg.f1stats.repo.models.*
import tmg.f1stats.repo.utils.toLapTime
import tmg.f1stats.repo_firebase.models.*

fun FSeason.convert(): Season {
    return Season(
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
        q1 = qualifying.onResult(drivers, constructors) { it.q1 },
        q2 = qualifying.onResult(drivers, constructors) { it.q2 },
        q3 = qualifying.onResult(drivers, constructors) { it.q3 },
        race = (race ?: mapOf())
            .map { (driverId, raceResult) ->
                driverId to RoundRaceResult(
                    driver = drivers.values.first { it.id == driverId }.convert(constructors),
                    time = raceResult.time?.toLapTime(),
                    points = raceResult.points ?: 0,
                    grid = raceResult.grid ?: 0,
                    finish = raceResult.result ?: 0,
                    status = RaceStatus.fromStatus(raceResult.status ?: RaceStatus.RETIRED.statusCode),
                    fastestLap = raceResult.fastestLap?.convert()
                )
            }
            .toMap()
    )
}
private fun Map<String, FSeasonOverviewRaceQualifying>?.onResult(
    drivers: Map<String, FSeasonOverviewDriver>,
    constructors: Map<String, FSeasonOverviewConstructor>,
    callback: (race: FSeasonOverviewRaceQualifying) -> String?
): Map<String, RoundQualifyingResult> {
    return (this ?: mapOf())
        .map { (driverId, qualiResult) ->
            val lapTime: LapTime? = callback(qualiResult)?.toLapTime()
            return@map driverId to RoundQualifyingResult(
                driver = drivers.values.first { it.id == driverId }.convert(constructors),
                time = lapTime,
                position = (this ?: mapOf())
                    .map { (driverId, res) -> driverId to res.q1?.toLapTime() }
                    .sortedBy { it.second?.totalMillis ?: Int.MAX_VALUE }
                    .indexOfFirst { it.first == driverId }
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