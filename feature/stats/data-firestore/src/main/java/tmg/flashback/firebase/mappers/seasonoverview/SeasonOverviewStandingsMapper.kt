package tmg.flashback.firebase.mappers.seasonoverview

import tmg.crash_reporting.controllers.CrashController
import tmg.flashback.formula1.model.ConstructorStandings
import tmg.flashback.formula1.model.DriverStandings
import tmg.flashback.firebase.models.FSeasonOverviewConstructor
import tmg.flashback.firebase.models.FSeasonStatistics

class SeasonOverviewStandingsMapper(
    private val driverMapper: SeasonOverviewDriverMapper,
    private val constructorMapper: SeasonOverviewConstructorMapper,
    private val crashController: CrashController
) {

    /**
     * Map driver standings from [FSeasonStatistics] to [DriverStandings]
     */
    fun mapDriverStandings(input: FSeasonStatistics, allDriverWithEmbeddedConstructors: List<tmg.flashback.formula1.model.DriverWithEmbeddedConstructor>): tmg.flashback.formula1.model.DriverStandings {
        val orderByPosition = input.drivers?.all { it.value.pos != null && it.value.pos != -1  } ?: false
        return (input.drivers ?: emptyMap())
            .map { (key, value) ->
                val driver = allDriverWithEmbeddedConstructors.firstOrNull { driver -> driver.id == key }
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
                    val availableIds = allDriverWithEmbeddedConstructors.joinToString(separator = ",") { it.id }
                    crashController.logException(NullPointerException("SeasonOverviewMapper.mapDriverStandings standings have $standingsIds vs. available $availableIds"))
                    return@mapNotNull null
                }
                return@mapNotNull tmg.flashback.formula1.model.SeasonStanding(
                    item = driver,
                    points = points,
                    position = position
                )
            }
    }

    /**
     * Map driver standings from [FSeasonStatistics] to [ConstructorStandings]
     */
    fun mapConstructorStandings(input: FSeasonStatistics, allConstructors: List<FSeasonOverviewConstructor>): tmg.flashback.formula1.model.ConstructorStandings {

        val orderByPosition = input.constructors?.all { it.value.pos != null && it.value.pos != -1  } ?: false
        return (input.constructors ?: emptyMap())
            .map { (key, value) ->
                val constructor = allConstructors.firstOrNull { constructor -> constructor.id == key}
                return@map Triple(constructor?.let { constructorMapper.mapConstructor(it) }, value.p ?: 0.0, value.pos ?: -1)
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
                return@mapNotNull tmg.flashback.formula1.model.SeasonStanding(
                    item = constructor,
                    points = points,
                    position = position
                )
            }
    }
}