package tmg.flashback.firebase.mappers

import androidx.core.graphics.toColorInt
import tmg.flashback.data.models.stats.ConstructorDriver
import tmg.flashback.data.models.stats.Constructor
import tmg.flashback.data.models.stats.ConstructorOverview
import tmg.flashback.data.models.stats.ConstructorOverviewDriverStanding
import tmg.flashback.data.models.stats.ConstructorOverviewStanding
import tmg.flashback.firebase.currentYear
import tmg.flashback.firebase.models.FConstructorOverview
import tmg.flashback.firebase.models.FConstructorOverviewData
import tmg.flashback.firebase.models.FConstructorOverviewDrivers
import tmg.flashback.firebase.models.FConstructorOverviewStandings
import tmg.flashback.firebase.models.FConstructorOverviewStandingsDriver
import tmg.utilities.utils.LocalDateUtils.Companion.requireFromDate

class ConstructorMapper {

    fun mapConstructorOverview(input: FConstructorOverview): ConstructorOverview {
        val standings: List<ConstructorOverviewStanding> = when {
            input.drivers.isNullOrEmpty() -> emptyList()
            input.standings.isNullOrEmpty() -> emptyList()
            else -> (input.standings)
                .map { (_, value) ->
                    mapConstructorOverviewStandings(value, input.data, input.drivers)
                }
        }
        return ConstructorOverview(
            id = input.data.id,
            name = input.data.name,
            wikiUrl = input.data.wikiUrl,
            nationality = input.data.nationality,
            nationalityISO = input.data.nationalityISO,
            color = input.data.colour.toColorInt(),
            standings = standings
        )
    }

    fun mapConstructorOverviewStandings(input: FConstructorOverviewStandings, overviewData: FConstructorOverviewData, drivers: Map<String, FConstructorOverviewDrivers>): ConstructorOverviewStanding {
        return ConstructorOverviewStanding(
            drivers = (input.drivers ?: emptyMap())
                .map { (key, value) ->
                    drivers[key]?.let { driver ->
                        return@map mapConstructorOverviewDriverStanding(value, driver, overviewData)
                    }
                    return@map null
                }
                .filterNotNull()
                .map { it.driver.id to it }
                .toMap(),
            isInProgress =  if (input.s >= currentYear) (input.inProgress ?: false) else false,
            championshipStanding = input.championshipStanding ?: 0,
            points = input.p ?: 0.0,
            season = input.s,
            races = input.races ?: 0
        )
    }

    fun mapConstructorOverviewDriverStanding(input: FConstructorOverviewStandingsDriver, driver: FConstructorOverviewDrivers, constructorData: FConstructorOverviewData): ConstructorOverviewDriverStanding {
        return ConstructorOverviewDriverStanding(
            driver = mapConstructorDriver(driver, constructorData),
            bestFinish = input.bF ?: -1,
            bestQualifying = input.bQ ?: -1,
            points = input.p ?: 0.0,
            finishesInP1 = input.p1 ?: 0,
            finishesInP2 = input.p2 ?: 0,
            finishesInP3 = input.p3 ?: 0,
            finishesInPoints = input.pF ?: 0,
            qualifyingP1 = input.q1 ?: 0,
            qualifyingP2 = input.q2 ?: 0,
            qualifyingP3 = input.q3 ?: 0,
            qualifyingTop10 = input.q10,
            races = input.races ?: 0,
            championshipStanding = input.pos ?: -1
        )
    }

    fun mapConstructorDriver(input: FConstructorOverviewDrivers, constructorData: FConstructorOverviewData): ConstructorDriver {
        return ConstructorDriver(
            id = input.id,
            firstName = input.firstName,
            lastName = input.surname,
            code = input.driverCode,
            number = input.driverNumber?.toIntOrNull() ?: 0,
            wikiUrl = input.wikiUrl,
            photoUrl = input.photoUrl,
            dateOfBirth = requireFromDate(input.dob),
            nationality = input.nationality,
            nationalityISO = input.nationalityISO,
            constructor = mapConstructor(constructorData)
        )
    }

    fun mapConstructor(input: FConstructorOverviewData): Constructor {
        return Constructor(
            id = input.id,
            name = input.name,
            wikiUrl = input.wikiUrl,
            nationality = input.nationality,
            nationalityISO = input.nationalityISO,
            color = input.colour.toColorInt()
        )
    }
}