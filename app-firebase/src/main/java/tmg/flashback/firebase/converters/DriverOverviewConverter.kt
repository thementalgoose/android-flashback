package tmg.flashback.firebase.converters

import androidx.core.graphics.toColorInt
import org.threeten.bp.LocalDate
import tmg.flashback.firebase.models.*
import tmg.flashback.repo.models.stats.DriverOverview
import tmg.flashback.repo.models.stats.DriverOverviewRace
import tmg.flashback.repo.models.stats.DriverOverviewStanding
import tmg.flashback.repo.models.stats.SlimConstructor

fun FDriverOverview.convert(): DriverOverview {
    return DriverOverview(
            id = this.driver.id,
            firstName = this.driver.firstName,
            lastName = this.driver.surname,
            code = this.driver.driverCode ?: this.driver.surname.take(3),
            number = this.driver.driverNumber?.toInt() ?: 0,
            wikiUrl = this.driver.wikiUrl,
            photoUrl = this.driver.photoUrl,
            dateOfBirth = fromDate(this.driver.dob),
            nationality = this.driver.nationality,
            nationalityISO = this.driver.nationalityISO ?: "",
            standings = this.standings?.map { (_, value) -> value.convert() } ?: emptyList()
    )
}

fun FDriverOverviewStanding.convert(): DriverOverviewStanding {
    val constructors = this.constructor?.map { it.convert() } ?: emptyList()
    return DriverOverviewStanding(
             bestFinish = this.bestFinish ?: 0,
             bestFinishQuantity = this.bestFinishQuantity ?: 0,
             bestQualifying = this.bestQualifying ?: 0,
             bestQualifyingQuantity = this.bestQualifyingQuantity ?: 0,
             championshipStanding = this.championshipStanding ?: 0,
             isInProgress = this.inProgress ?: false,
             points = this.p ?: 0,
             podiums = this.podiums ?: 0,
             races = this.races ?: 0,
             season = this.s,
             wins = this.wins ?: 0,
             constructors = constructors,
             raceOverview = this.history?.map { (_, value) -> value.convert(this.s, constructors) } ?: emptyList()
    )
}


fun FDriverOverviewStandingHistory.convert(season: Int, constructors: List<SlimConstructor>): DriverOverviewRace {
    return DriverOverviewRace(
            finished = this.f ?: 0,
            points = this.p ?: 0,
            qualified = this.q ?: 0,
            round = this.r,
            season = season,
            raceName = this.rName ?: "",
            date = fromDate(this.date),
            constructor = when (constructors.size) {
                1 -> constructors.first()
                else -> constructors.firstOrNull { it.id == this.con }
            },
            circuitName = this.cName,
            circuitId = this.cId,
            circuitNationality = this.cCountry,
            circuitNationalityISO = this.cISO ?: "",
            status = this.fStatus ?: ""
    )
}

fun FDriverOverviewStandingConstructor.convert(): SlimConstructor {
    return SlimConstructor(
            id = this.id,
            color = this.color.toColorInt(),
            name = this.name
    )
}