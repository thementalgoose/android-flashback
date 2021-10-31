package tmg.flashback.firebase.mappers.seasonoverview

import tmg.flashback.firebase.mappers.LocationMapper
import tmg.flashback.firebase.models.FSeasonOverviewRaceCircuit
import tmg.flashback.firebase.models.FSeasonOverviewRaceQualifying
import tmg.flashback.firebase.models.FSeasonOverviewRaceRace
import tmg.flashback.firebase.models.FSeasonOverviewRaceRaceFastestLap
import tmg.flashback.firebase.models.FSeasonOverviewRaceSprintQualifying

class SeasonOverviewRaceMapper(
    private val locationMapper: LocationMapper
) {

    enum class Qualifying { Q1, Q2, Q3 }

    /**
     * Map qualifying data
     *
     * @param forRound The round that the qualifying is for
     * @param fieldToBaseFilteringOn Q1, Q2 or Q3
     * @param input Map of driver id -> qualifying result (pos,q1,q2,q3)
     * @param allDrivers Top level list of all drivers returned in the season
     */
    fun mapQualifying(forRound: Int, fieldToBaseFilteringOn: Qualifying, input: Map<String, FSeasonOverviewRaceQualifying>?, allDrivers: List<Driver>): Map<String, RoundQualifyingResult> {
        return (input ?: mapOf())
            .toSortedMap()
            .toList()
            .map { (driverId, qualifyingResult) ->
                return@map allDrivers
                    .first { it.id == driverId }
                    .let { Pair(it, qualifyingResult) }
            }
            .map { (driver, qualifyingResult) ->
                Triple(driver, qualifyingResult, when (fieldToBaseFilteringOn) {
                    Qualifying.Q1 -> qualifyingResult.q1?.toLapTime()
                    Qualifying.Q2 -> qualifyingResult.q2?.toLapTime()
                    Qualifying.Q3 -> qualifyingResult.q3?.toLapTime()
                })
            }
            .sortedBy { (_, _, lapTime) ->
                lapTime?.totalMillis.let {
                    if (this || this == 0) Int.MAX_VALUE else this
                }
            }
            .mapIndexed { index, (driver, qualifyingResult, lapTime) ->
                driver.id to RoundQualifyingResult(
                    driver = driver.toConstructorDriver(forRound),
                    time = lapTime,
                    position = (if (lapTime == null) qualifyingResult.pos else null) ?: (index + 1)
                )
            }
            .toMap()
    }

    /**
     * Map sprint data
     *
     * @param forRound The round that the qualifying is for
     * @param input Map of driver id -> sprint quali result
     * @param allDrivers Top level list of all drivers returned in the season
     */
    fun mapSprintQualifying(forRound: Int, input: Map<String, FSeasonOverviewRaceSprintQualifying>?, allDrivers: List<Driver>): Map<String, RoundSprintQualifyingResult> {
        return (input ?: emptyMap())
            .map { (driverId, sprintQualifying) ->
                return@map allDrivers
                    .first { it.id == driverId }
                    .let { Pair(it, sprintQualifying ) }
            }
            .map { (driver, sprintQualifying) ->
                driver.id to RoundSprintQualifyingResult(
                    driver = driver.toConstructorDriver(forRound),
                    time = sprintQualifying.time?.toLapTime(),
                    points = sprintQualifying.points ?: 0.0,
                    grid = sprintQualifying.grid ?: 0,
                    qualified = sprintQualifying.getSprintQualified,
                    finish = sprintQualifying.result ?: 0,
                    status = sprintQualifying.status
                        ?: tmg.flashback.formula1.enums.raceStatusUnknown
                )
            }
            .toMap()
    }

    /**
     * Map sprint data
     *
     * @param forRound The round that the qualifying is for
     * @param input Map of driver id -> sprint quali result
     * @param allDrivers Top level list of all drivers returned in the season
     */
    fun mapRace(forRound: Int, input: Map<String, FSeasonOverviewRaceRace>?, sprintQualifyingData: Map<String, FSeasonOverviewRaceSprintQualifying>?, allDrivers: List<Driver>): Map<String, RoundRaceResult> {
        return (input ?: emptyMap())
            .map { (driverId, sprintQualifying) ->
                return@map allDrivers
                    .first { it.id == driverId }
                    .let { Pair(it, sprintQualifying ) }
            }
            .map { (driver, raceResult) ->
                driver.id to RoundRaceResult(
                    driver = driver.toConstructorDriver(forRound),
                    time = raceResult.time?.toLapTime(),
                    points = raceResult.points ?: 0.0,
                    grid = raceResult.grid ?: 0,
                    qualified = sprintQualifyingData?.get(driver.id)?.getSprintQualified
                        ?: raceResult.getQualified,
                    finish = raceResult.result ?: 0,
                    status = raceResult.status ?: tmg.flashback.formula1.enums.raceStatusUnknown,
                    fastestLap = raceResult.fastestLap?.let { mapFastestLap(it) }
                )
            }
            .toMap()
    }

    /**
     * Map a circuit overview
     */
    fun mapCircuit(input: FSeasonOverviewRaceCircuit): CircuitSummary {
        return CircuitSummary(
            id = input.id,
            name = input.name,
            wikiUrl = input.wikiUrl ?: "",
            locality = input.locality,
            country = input.country,
            countryISO = input.countryISO,
            location = locationMapper.mapCircuitLocation(input.location)
        )
    }

    /**
     * Map the fastest lap object
     */
    fun mapFastestLap(input: FSeasonOverviewRaceRaceFastestLap): FastestLap {
        return FastestLap(
            rank = input.pos,
            lap = input.lap,
            lapTime = input.time.toLapTime()
        )
    }
}