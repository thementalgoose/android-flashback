package tmg.flashback.firebase.mappers

import androidx.core.graphics.toColorInt
import tmg.flashback.data.models.stats.DriverOverview
import tmg.flashback.data.models.stats.DriverOverviewRace
import tmg.flashback.data.models.stats.DriverOverviewStanding
import tmg.flashback.data.models.stats.SlimConstructor
import tmg.flashback.firebase.base.ConverterUtils.fromDateRequired
import tmg.flashback.firebase.converters.convert
import tmg.flashback.firebase.currentYear
import tmg.flashback.firebase.models.FDriverOverview
import tmg.flashback.firebase.models.FDriverOverviewDriver
import tmg.flashback.firebase.models.FDriverOverviewStanding
import tmg.flashback.firebase.models.FDriverOverviewStandingConstructor
import tmg.flashback.firebase.models.FDriverOverviewStandingHistory

class DriverMapper {

    fun mapDriverOverview(input: FDriverOverview): DriverOverview {
        return DriverOverview(
            id = input.driver.id,
            firstName = input.driver.firstName,
            lastName = input.driver.surname,
            code = (input.driver.driverCode ?: input.driver.surname.take(3)).uppercase(),
            number = input.driver.driverNumber?.toIntOrNull() ?: 0,
            wikiUrl = input.driver.wikiUrl,
            photoUrl = input.driver.photoUrl,
            dateOfBirth = fromDateRequired(input.driver.dob),
            nationality = input.driver.nationality,
            nationalityISO = input.driver.nationalityISO ?: "",
            standings = input.standings
                ?.map { (_, value) ->
                    mapDriverOverviewStanding(value)
                }
                ?: emptyList()
        )
    }

    fun mapDriverOverviewStanding(input: FDriverOverviewStanding): DriverOverviewStanding {
        return DriverOverviewStanding(
            bestFinish = input.bestFinish ?: 0,
            bestFinishQuantity = input.bestFinishQuantity ?: 0,
            bestQualifying = input.bestQualifying ?: 0,
            bestQualifyingQuantity = input.bestQualifyingQuantity ?: 0,
            championshipStanding = input.championshipStanding ?: 0,
            isInProgress = if (input.s >= currentYear) (input.inProgress ?: false) else false,
            points = input.p ?: 0,
            podiums = input.podiums ?: 0,
            races = input.races ?: 0,
            season = input.s,
            wins = input.wins ?: 0,
            constructors = input.constructor?.map { mapSlimConstructor(it) } ?: emptyList(),
            raceOverview = input.history?.map { (_, value) -> mapDriverOverviewRace(input.s, input.constructor ?: emptyList(), value) } ?: emptyList()
        )
    }

    fun mapSlimConstructor(input: FDriverOverviewStandingConstructor): SlimConstructor {
        return SlimConstructor(
            id = input.id,
            name = input.name,
            color = input.color.toColorInt()
        )
    }

    fun mapDriverOverviewRace(season: Int, constructors: List<FDriverOverviewStandingConstructor>, input: FDriverOverviewStandingHistory): DriverOverviewRace {
        return DriverOverviewRace(
            finished = input.f ?: 0,
            points = input.p ?: 0,
            qualified = input.q ?: 0,
            round = input.r,
            season = season,
            raceName = input.rName ?: "",
            date = fromDateRequired(input.date),
            constructor = when (constructors.size) {
                0 -> null
                1 -> mapSlimConstructor(constructors.first())
                else -> constructors.firstOrNull { it.id == input.con }?.let { mapSlimConstructor(it) } ?: mapSlimConstructor(constructors.first())
            },
            circuitName = input.cName,
            circuitId = input.cId,
            circuitNationality = input.cCountry,
            circuitNationalityISO = input.cISO ?: "",
            status = input.fStatus ?: "Unknown"
        )
    }

}