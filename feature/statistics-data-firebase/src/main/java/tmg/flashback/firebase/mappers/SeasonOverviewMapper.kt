package tmg.flashback.firebase.mappers

import androidx.core.graphics.toColorInt
import java.lang.NullPointerException
import org.threeten.bp.LocalDate
import tmg.crash_reporting.controllers.CrashController
import tmg.flashback.data.models.stats.Constructor
import tmg.flashback.data.models.stats.ConstructorStandings
import tmg.flashback.data.models.stats.Driver
import tmg.flashback.data.models.stats.DriverStandings
import tmg.flashback.data.models.stats.FastestLap
import tmg.flashback.data.models.stats.SeasonStanding
import tmg.flashback.data.utils.toLapTime
import tmg.flashback.firebase.base.ConverterUtils.fromDateRequired
import tmg.flashback.firebase.models.FRound
import tmg.flashback.firebase.models.FSeasonOverviewConstructor
import tmg.flashback.firebase.models.FSeasonOverviewDriver
import tmg.flashback.firebase.models.FSeasonOverviewRaceRaceFastestLap
import tmg.flashback.firebase.models.FSeasonStatistics

class SeasonOverviewMapper(
    private val crashController: CrashController
) {


    fun mapDriverStandings(input: FSeasonStatistics, allDrivers: List<Pair<FSeasonOverviewDriver, FSeasonOverviewConstructor>>): DriverStandings {
        val orderByPosition = input.drivers?.all { it.value.pos != null && it.value.pos != -1  } ?: false
        return (input.drivers ?: emptyMap())
            .map { (key, value) ->
                val driver = allDrivers.firstOrNull { driver -> driver.first.id == key }
                return@map Triple(driver?.let { mapDriver(driver.first, mapConstructor(driver.second))}, value.p ?: 0.0, value.pos ?: -1)
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
                    val availableIds = allDrivers.joinToString(separator = ",") { it.first.id }
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
        val constructorAtLastRace = lastRaceThatDriverWasPartOf?.driverCon!![driverId]
        return allConstructors
            .firstOrNull { it.id == constructorAtLastRace }
            ?.let { mapConstructor(it) }
    }

    fun mapDriver(input: FSeasonOverviewDriver, constructor: Constructor): Driver {
        return Driver(
            id = input.id,
            firstName = input.firstName,
            lastName = input.lastName,
            code = input.code,
            number = input.number ?: 0,
            wikiUrl = input.wikiUrl,
            photoUrl = input.photoUrl,
            dateOfBirth = fromDateRequired(input.dob),
            nationality = input.nationality,
            nationalityISO = input.nationalityISO,
            constructorAtEndOfSeason = constructor,
        )
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

    fun mapFastestLap(input: FSeasonOverviewRaceRaceFastestLap): FastestLap {
        return FastestLap(
            rank = input.pos,
            lap = input.lap,
            lapTime = input.time.toLapTime()
        )
    }
}