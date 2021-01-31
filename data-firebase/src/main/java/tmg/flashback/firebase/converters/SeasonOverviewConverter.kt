package tmg.flashback.firebase.converters

import tmg.flashback.firebase.base.ConverterUtils.fromDateRequired
import tmg.flashback.firebase.base.ConverterUtils.fromTime
import tmg.flashback.data.enums.raceStatusUnknown
import tmg.flashback.data.models.stats.*
import tmg.flashback.data.utils.toLapTime
import tmg.flashback.data.utils.toMaxIfZero
import tmg.flashback.firebase.models.*

fun FSeason.convert(season: Int): Season {
    val drivers = (this.drivers ?: mapOf())
            .values
            .map { it.convert(this.constructorAtEndOfSeason(it.id)) }
    val constructors = (this.constructors ?: mapOf())
            .values
            .map { it.convert() }

    return Season(
            season = season,
            drivers = drivers,
            constructors = constructors,
            rounds = (this.race ?: mapOf())
                    .values
                    .map {
                        it.convert(this)
                    },
            driverStandings = this.standings?.drivers?.let {
                return@let it.convertDriver(drivers)
            } ?: mapOf(),
            constructorStandings = this.standings?.constructors?.let {
                return@let it.convertConstructor(constructors)
            } ?: mapOf()
    )
}

fun FRound.convert(
        fSeason: FSeason
): Round {

    val driverList = (fSeason.drivers ?: emptyMap()).values
            .map { driver ->
                driver.convert(
                        fSeason.constructors ?: emptyMap(),
                        driverCon?.toList()?.firstOrNull { it.first == driver.id }?.second,
                        fSeason.constructorAtEndOfSeason(driver.id)
                )
            }
    val constructorList = (fSeason.constructors ?: emptyMap()).values
            .map { constructor -> constructor.convert() }

    val raceMap = (race ?: mapOf())
            .map { (driverId, raceResult) ->
                driverId to RoundRaceResult(
                        driver = driverList.first { it.id == driverId },
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

    return Round(
            season = season,
            round = round,
            date = fromDateRequired(date),
            time = fromTime(time),
            name = name,
            wikipediaUrl = wiki,
            drivers = driverList,
            constructors = constructorList,
            circuit = circuit.convert(),
            q1 = qualifying.onResult(fSeason, driverCon ?: emptyMap()) { it.q1 },
            q2 = qualifying.onResult(fSeason, driverCon ?: emptyMap()) { it.q2 },
            q3 = qualifying.onResult(fSeason, driverCon ?: emptyMap()) { it.q3 },
            race = raceMap
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
            ?.maxByOrNull { it.value.round }
            ?.value
            ?.driverCon?.get(driverId)
    return this.constructors?.get(constructorId)!!.convert()
}

// key = driverId, value = model
private fun Map<String, FSeasonStatisticsPoints>.convertDriver(drivers: List<Driver>): DriverStandings = this
        .map {
            it.key to Pair(drivers.first { driver -> driver.id == it.key }, it.value.p ?: 0)
        }
        .toMap()

private fun Map<String, FSeasonStatisticsPoints>.convertConstructor(constructors: List<Constructor>): ConstructorStandings = this
        .map {
            it.key to Pair(constructors.first { constructor -> constructor.id == it.key }, it.value.p ?: 0)
        }
        .toMap()