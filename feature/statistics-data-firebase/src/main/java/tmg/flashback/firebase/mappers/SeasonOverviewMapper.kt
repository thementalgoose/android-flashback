package tmg.flashback.firebase.mappers

import androidx.core.graphics.toColorInt
import tmg.crash_reporting.controllers.CrashController
import tmg.flashback.data.enums.raceStatusUnknown
import tmg.flashback.data.models.stats.CircuitSummary
import tmg.flashback.data.models.stats.Constructor
import tmg.flashback.data.models.stats.ConstructorStandings
import tmg.flashback.data.models.stats.Driver
import tmg.flashback.data.models.stats.DriverStandings
import tmg.flashback.data.models.stats.FastestLap
import tmg.flashback.data.models.stats.Round
import tmg.flashback.data.models.stats.RoundQualifyingResult
import tmg.flashback.data.models.stats.RoundRaceResult
import tmg.flashback.data.models.stats.RoundSprintQualifyingResult
import tmg.flashback.data.models.stats.Season
import tmg.flashback.data.models.stats.SeasonStanding
import tmg.flashback.data.utils.toLapTime
import tmg.flashback.firebase.base.ConverterUtils.fromDateRequired
import tmg.flashback.firebase.base.ConverterUtils.fromTime
import tmg.flashback.firebase.models.FRound
import tmg.flashback.firebase.models.FSeason
import tmg.flashback.firebase.models.FSeasonOverviewConstructor
import tmg.flashback.firebase.models.FSeasonOverviewRaceCircuit
import tmg.flashback.firebase.models.FSeasonOverviewRaceQualifying
import tmg.flashback.firebase.models.FSeasonOverviewRaceRace
import tmg.flashback.firebase.models.FSeasonOverviewRaceRaceFastestLap
import tmg.flashback.firebase.models.FSeasonOverviewRaceSprintQualifying
import tmg.flashback.firebase.models.FSeasonStatistics

class SeasonOverviewMapper(
    private val crashController: CrashController
) {

    fun mapSeason(input: FSeason, season: Int): Season {
        val drivers = (input.drivers ?: emptyMap()).mapNotNull { (_, value) -> mapDriver(input, value.id) }
        val fConstructors = (input.constructors ?: emptyMap()).map { it.value }

        return Season(
            season = season,
            drivers = drivers,
            constructors = fConstructors.map { mapConstructor(it) },
            rounds = input.race
                ?.map { (_, value) ->
                    mapRound(value, drivers, fConstructors)
                }
                ?: emptyList(),
            driverStandings = input.standings?.let { mapDriverStandings(it, drivers) } ?: emptyList(),
            constructorStandings = input.standings?.let { mapConstructorStandings(it, fConstructors) } ?: emptyList()
        )
    }

    fun mapRound(input: FRound, allDrivers: List<Driver>, allConstructors: List<FSeasonOverviewConstructor>): Round {

        val sprintQualifyingMap = (input.sprintQualifying ?: emptyMap())
            .map { (driverId, sprintQualiResult) ->
                driverId to RoundSprintQualifyingResult(
                    driver = allDrivers.first { it.id == driverId },
                    time = sprintQualiResult.time?.toLapTime(),
                    points = sprintQualiResult.points ?: 0.0,
                    grid = sprintQualiResult.grid ?: 0,
                    qualified = getSprintQualified(sprintQualiResult),
                    finish = sprintQualiResult.result ?: 0,
                    status = sprintQualiResult.status ?: raceStatusUnknown
                )
            }

        val raceMap = (input.race ?: emptyMap())
            .map { (driverId, raceResult) ->
                driverId to RoundRaceResult(
                    driver = allDrivers.first { it.id == driverId },
                    time = raceResult.time?.toLapTime(),
                    points = raceResult.points ?: 0.0,
                    grid = raceResult.grid ?: 0,
                    qualified = getQualified(raceResult, input.sprintQualifying?.get(driverId)),
                    finish = raceResult.result ?: 0,
                    status = raceResult.status ?: raceStatusUnknown,
                    fastestLap = raceResult.fastestLap?.let { mapFastestLap(it) }
                )
            }
            .toMap()

        return Round(
            season = input.season,
            round = input.round,
            date = fromDateRequired(input.date),
            time = fromTime(input.time),
            name = input.name,
            wikipediaUrl = input.wiki,
            drivers = allDrivers,
            constructors = allConstructors.map { mapConstructor(it) },
            circuit = mapCircuit(input.circuit),
        )
    }

    private fun getQualified(raceResult: FSeasonOverviewRaceRace, sprintQualifyingResult: FSeasonOverviewRaceSprintQualifying?): Int? {
        if (sprintQualifyingResult != null) {
            return getSprintQualified(sprintQualifyingResult)
        }
        if (raceResult.qualified != -1 && raceResult.qualified != 0) {
            return raceResult.qualified
        }
        if (raceResult.grid != null && raceResult.grid != -1 && raceResult.grid != 0) {
            return raceResult.grid
        }
        return null
    }

    private fun getSprintQualified(sprintQualiResult: FSeasonOverviewRaceSprintQualifying): Int? {
        if (sprintQualiResult.qualified != -1 && sprintQualiResult.qualified != 0) {
            return sprintQualiResult.qualified
        }
        if (sprintQualiResult.grid != null && sprintQualiResult.grid != -1 && sprintQualiResult.grid != 0) {
            return sprintQualiResult.grid
        }
        return null
    }

    fun mapDriverStandings(input: FSeasonStatistics, allDrivers: List<Driver>): DriverStandings {
        val orderByPosition = input.drivers?.all { it.value.pos != null && it.value.pos != -1  } ?: false
        return (input.drivers ?: emptyMap())
            .map { (key, value) ->
                val driver = allDrivers.firstOrNull { driver -> driver.id == key }
                return@map Triple(driver, value.p ?: 0.0, value.pos ?: -1)
            }
            .let { list ->
                return@let when (orderByPosition) {
                    true -> list
                        .sortedBy { it.third }
                    false -> list
                        .sortedByDescending { it.second }
                        .mapIndexed { index, triple -> Triple(triple.first, triple.second, index + 1) }

                }
            }
            .mapNotNull { (driver, points, position) ->
                if (driver == null) {
                    val standingsIds = input.drivers?.map { it.key }?.joinToString(separator = ",") ?: "N/A"
                    val availableIds = allDrivers.joinToString(separator = ",") { it.id }
                    crashController.logException(NullPointerException("SeasonOverviewMapper.mapDriverStandings standings have $standingsIds vs. available $availableIds"))
                    return@mapNotNull null
                }
                return@mapNotNull SeasonStanding(
                    item = driver,
                    points = points,
                    position = position
                )
            }
    }

    fun mapConstructorStandings(input: FSeasonStatistics, allConstructors: List<FSeasonOverviewConstructor>): ConstructorStandings {
        val orderByPosition = input.constructors?.all { it.value.pos != null && it.value.pos != -1  } ?: false
        return (input.constructors ?: emptyMap())
            .map { (key, value) ->
                val constructor = allConstructors.firstOrNull { constructor -> constructor.id == key}
                return@map Triple(constructor?.let { mapConstructor(it) }, value.p ?: 0.0, value.pos ?: -1)
            }
            .let { list ->
                return@let when (orderByPosition) {
                    true -> list
                        .sortedBy { it.third }
                    false -> list
                        .sortedByDescending { it.second }
                        .mapIndexed { index, triple -> Triple(triple.first, triple.second, index + 1) }

                }
            }
            .mapNotNull { (constructor, points, position) ->
                if (constructor == null) {
                    val standingsIds = input.constructors?.map { it.key }?.joinToString(separator = ",") ?: "N/A"
                    val availableIds = allConstructors.joinToString(separator = ",") { it.id }
                    crashController.logException(NullPointerException("SeasonOverviewMapper.mapConstructorStandings standings have $standingsIds vs. available $availableIds"))
                    return@mapNotNull null
                }
                return@mapNotNull SeasonStanding(
                    item = constructor,
                    points = points,
                    position = position
                )
            }
    }

    private fun getConstructorAtLastRaceDriver(driverId: String, race: Map<String, FRound>?, allConstructors: List<FSeasonOverviewConstructor>): Constructor? {
        val lastRaceThatDriverWasPartOf = (race ?: emptyMap())
            .filter { (_, round) -> round.driverCon?.containsKey(driverId) ?: false }
            .maxByOrNull { (_, round) -> round.round }
            ?.value
        val constructorAtLastRace = lastRaceThatDriverWasPartOf?.driverCon?.get(driverId)
        return allConstructors
            .firstOrNull { it.id == constructorAtLastRace }
            ?.let { mapConstructor(it) }
    }

    fun mapDriver(input: FSeason, driverId: String): Driver? {
        val constructors = input.constructors?.values?.toList() ?: emptyList()
        val constructor = getConstructorAtLastRaceDriver(driverId, input.race, constructors)
            ?: return null
        return input.drivers
            ?.get(driverId)
            ?.let {
                Driver(
                    id = it.id,
                    firstName = it.firstName,
                    lastName = it.lastName,
                    code = it.code,
                    number = it.number ?: 0,
                    wikiUrl = it.wikiUrl,
                    photoUrl = it.photoUrl,
                    dateOfBirth = fromDateRequired(it.dob),
                    nationality = it.nationality,
                    nationalityISO = it.nationalityISO,
                    constructorAtEndOfSeason = constructor
                )
            }
    }

    fun mapConstructor(input: FSeasonOverviewConstructor): Constructor {
        return Constructor(
            id = input.id,
            name = input.name,
            wikiUrl = input.wikiUrl,
            nationality = input.nationality,
            nationalityISO = input.nationalityISO,
            color = input.colour.toColorInt()
        )
    }

    fun mapCircuit(input: FSeasonOverviewRaceCircuit): CircuitSummary {
        return CircuitSummary(
            id = input.id,
            name = input.name,
            wikiUrl = input.wikiUrl,
            locality = input.locality,
            country = input.country,
            countryISO = input.countryISO,
            locationLat = input.location.lat?.toDoubleOrNull() ?: 0.0,
            locationLng = input.location.lng?.toDoubleOrNull() ?: 0.0
        )
    }

    fun mapFastestLap(input: FSeasonOverviewRaceRaceFastestLap): FastestLap {
        return FastestLap(
            rank = input.pos,
            lap = input.lap,
            lapTime = input.time.toLapTime()
        )
    }
}