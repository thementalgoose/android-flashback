package tmg.flashback.firebase.mappers

import androidx.core.graphics.toColorInt
import tmg.flashback.formula1.model.DriverOverview
import tmg.flashback.formula1.model.DriverOverviewRace
import tmg.flashback.formula1.model.DriverOverviewStanding
import tmg.flashback.formula1.model.SlimConstructor
import tmg.flashback.firebase.currentYear
import tmg.flashback.firebase.models.FDriverOverview
import tmg.flashback.firebase.models.FDriverOverviewStanding
import tmg.flashback.firebase.models.FDriverOverviewStandingConstructor
import tmg.flashback.firebase.models.FDriverOverviewStandingHistory
import tmg.utilities.utils.LocalDateUtils.Companion.requireFromDate

class DriverMapper {

    fun mapDriverOverview(input: FDriverOverview): tmg.flashback.formula1.model.DriverOverview {
        return tmg.flashback.formula1.model.DriverOverview(
            id = input.driver.id,
            firstName = input.driver.firstName,
            lastName = input.driver.surname,
            code = (input.driver.driverCode ?: input.driver.surname.take(3)).uppercase(),
            number = input.driver.driverNumber?.toIntOrNull() ?: 0,
            wikiUrl = input.driver.wikiUrl,
            photoUrl = input.driver.photoUrl,
            dateOfBirth = requireFromDate(input.driver.dob),
            nationality = input.driver.nationality,
            nationalityISO = input.driver.nationalityISO ?: "",
            standings = input.standings
                ?.map { (_, value) ->
                    mapDriverOverviewStanding(value)
                }
                ?: emptyList()
        )
    }

    fun mapDriverOverviewStanding(input: FDriverOverviewStanding): tmg.flashback.formula1.model.DriverOverviewStanding {
        return tmg.flashback.formula1.model.DriverOverviewStanding(
            bestFinish = input.bestFinish ?: 0,
            bestFinishQuantity = input.bestFinishQuantity ?: 0,
            bestQualifying = input.bestQualifying ?: 0,
            bestQualifyingQuantity = input.bestQualifyingQuantity ?: 0,
            championshipStanding = input.championshipStanding ?: 0,
            isInProgress = if (input.s >= currentYear) (input.inProgress ?: false) else false,
            points = input.p ?: 0.0,
            podiums = input.podiums ?: 0,
            races = input.races ?: 0,
            season = input.s,
            wins = input.wins ?: 0,
            constructors = input.constructor?.map { mapSlimConstructor(it) } ?: emptyList(),
            raceOverview = input.history?.map { (_, value) ->
                mapDriverOverviewRace(
                    input.s,
                    input.constructor ?: emptyList(),
                    value
                )
            } ?: emptyList()
        )
    }

    fun mapSlimConstructor(input: FDriverOverviewStandingConstructor): tmg.flashback.formula1.model.SlimConstructor {
        return tmg.flashback.formula1.model.SlimConstructor(
            id = input.id,
            name = input.name,
            color = input.color.toColorInt()
        )
    }

    fun mapDriverOverviewRace(season: Int, constructors: List<FDriverOverviewStandingConstructor>, input: FDriverOverviewStandingHistory): tmg.flashback.formula1.model.DriverOverviewRace {
        return tmg.flashback.formula1.model.DriverOverviewRace(
            finished = input.f ?: 0,
            points = input.p ?: 0.0,
            qualified = input.q ?: 0,
            round = input.r,
            season = season,
            raceName = input.rName ?: "",
            date = requireFromDate(input.date),
            constructor = when (constructors.size) {
                0 -> null
                1 -> mapSlimConstructor(constructors.first())
                else -> constructors.firstOrNull { it.id == input.con }
                    ?.let { mapSlimConstructor(it) } ?: mapSlimConstructor(constructors.first())
            },
            circuitName = input.cName,
            circuitId = input.cId,
            circuitNationality = input.cCountry,
            circuitNationalityISO = input.cISO ?: "",
            status = input.fStatus ?: "Unknown"
        )
    }

}