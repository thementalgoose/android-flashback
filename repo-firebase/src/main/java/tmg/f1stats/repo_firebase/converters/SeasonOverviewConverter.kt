package tmg.f1stats.repo_firebase.converters

import tmg.f1stats.repo.enums.RaceStatus.Companion.fromStatus
import tmg.f1stats.repo.models.*
import tmg.f1stats.repo.utils.addDelta
import tmg.f1stats.repo.utils.toLapTime
import tmg.f1stats.repo_firebase.models.*

fun FSeasonOverview.toModel(): SeasonRound {
    return SeasonRound(
            wikiUrl = wikiUrl,
            raceName = raceName,
            date = fromDate(date),
            time = fromTime(time),
            season = season,
            round = round,
            raceKey = raceKey,
            drivers = drivers.toModels {
                it.toModel(constructors)
            },
            circuit = circuit.toModel(),
            constructors = constructors.toModels {
                it.toModel()
            },
            raceResults = raceResults?.toModels {
                it.toModel(drivers, constructors, winner)
            } ?: listOf(),
            fastestLaps = raceResults?.entries
                    ?.map { it.value }
                    ?.sortedByDescending { it.fastestLap.rank }
                    ?.map {
                        RaceResultFastestLap(
                                lapNumber = it.fastestLap.lapNumber,
                                rank = it.fastestLap.rank,
                                averageSpeed = it.fastestLap.averageSpeed,
                                time = it.time?.toLapTime(),
                                driver = drivers[it.driverId].let { driver -> driver!!.toModel(constructors) }
                        )
                    } ?: listOf(),
            q1Results = qualifyingResult.q1?.toModels {
                it.toModel(drivers, constructors)
            } ?: listOf(),
            q2Results = qualifyingResult.q2?.toModels {
                it.toModel(drivers, constructors)
            } ?: listOf(),
            q3Results = qualifyingResult.q3?.toModels {
                it.toModel(drivers, constructors)
            } ?: listOf()
    )
}

fun FSeasonOverviewDriver.toModel(constructors: Map<String, FSeasonOverviewConstructor>): DriverOnWeekend {
    return DriverOnWeekend(
            driverId = this.driverId,
            driverNumber = this.driverNumber,
            driverCode = this.driverCode,
            wikiUrl = this.wikiUrl,
            name = this.firstName,
            surname = this.lastName,
            dateOfBirth = fromDate(this.dateOfBirth),
            nationality = this.nationality,
            constructor = constructors[this.seasonConstructor].let { constructor -> constructor!!.toModel() }
    )
}

fun FSeasonOverviewCircuit.toModel(): Circuit {
    return Circuit(
            circuitId = this.circuitId,
            wikiUrl = this.wikiUrl,
            circuitName = this.circuitName,
            locationLat = this.locationLat.toDoubleOrNull(),
            locationLng = this.locationLng.toDoubleOrNull(),
            locality = this.locality,
            country = this.country
    )
}

fun FSeasonOverviewConstructor.toModel(): Constructor {
    return Constructor(
            constructorId = this.constructorId,
            wikiUrl = this.wikiUrl,
            name = this.name,
            nationality = this.nationality
    )
}

fun FSeasonOverviewRaces.toModel(drivers: Map<String, FSeasonOverviewDriver>, constructors: Map<String, FSeasonOverviewConstructor>, winner: FSeasonOverviewRaces?): RaceResult {
    return RaceResult(
            driver = drivers[this.driverId].let { driver -> driver!!.toModel(constructors) },
            gridPosition = this.gridPos,
            status = fromStatus(this.status),
            finishPosition = this.finishPos,
            finishPositionText = this.finishPosText,
            time = if (winner != null && winner.driverId != this.driverId) {
                winner.time?.toLapTime()?.addDelta(this.time)
            } else {
                this.time?.toLapTime()
            }
    )
}

fun FSeasonOverviewQualifyingResult.toModel(drivers: Map<String, FSeasonOverviewDriver>, constructors: Map<String, FSeasonOverviewConstructor>): QualifyingResult {
    return QualifyingResult(
            driver = drivers[this.driverId].let { driver -> driver!!.toModel(constructors) },
            time = time.toLapTime(),
            position = position.toIntOrNull() ?: -1
    )
}