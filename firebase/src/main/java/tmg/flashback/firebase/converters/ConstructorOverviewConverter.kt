package tmg.flashback.firebase.converters

import androidx.core.graphics.toColorInt
import tmg.flashback.firebase.models.*
import tmg.flashback.repo.models.ConstructorDriver
import tmg.flashback.repo.models.stats.Constructor
import tmg.flashback.repo.models.stats.ConstructorOverview
import tmg.flashback.repo.models.stats.ConstructorOverviewDriverStanding
import tmg.flashback.repo.models.stats.ConstructorOverviewStanding

fun FConstructorOverview.convert(): ConstructorOverview {
    return ConstructorOverview(
            id = this.data.id,
            name = this.data.name,
            wikiUrl = this.data.wikiUrl,
            nationality = this.data.nationality,
            nationalityISO = this.data.nationalityISO,
            color = this.data.colour.toColorInt(),
            standings = convertStandings()
    )
}

private fun FConstructorOverview.convertStandings(): List<ConstructorOverviewStanding> {
    if (this.drivers == null || this.standings == null) {
        return emptyList()
    }
    return this.standings
            .map {
                it.value.convert(this.data, this.drivers)
            }
            .sortedByDescending { it.season }
}

fun FConstructorOverviewStandings.convert(data: FConstructorOverviewData, drivers: Map<String, FConstructorOverviewDrivers>): ConstructorOverviewStanding {

    return ConstructorOverviewStanding(
            drivers = (this.drivers ?: emptyMap())
                    .map { entry ->
                        drivers[entry.key]?.let {
                            return@map entry.value.convert(it.convert(data))
                        }
                        return@map null
                    }
                    .filterNotNull()
                    .map { it.driver.id to it }
                    .toMap(),
            isInProgress = this.inProgress == true,
            championshipStanding = this.championshipStanding ?: 0,
            points = this.p ?: 0,
            season = this.s,
            races = this.races ?: 0,
    )
}

fun FConstructorOverviewDrivers.convert(data: FConstructorOverviewData): ConstructorDriver {
    return ConstructorDriver(
            id = this.id,
            firstName = this.firstName,
            lastName = this.surname,
            code = this.driverCode,
            number = this.driverNumber?.toIntOrNull() ?: 0,
            wikiUrl = this.wikiUrl,
            photoUrl = this.photoUrl,
            dateOfBirth = fromDate(this.dob),
            nationality = this.nationality,
            nationalityISO = this.nationalityISO,
            constructor = Constructor(
                    id = data.id,
                    name = data.name,
                    wikiUrl = data.wikiUrl,
                    nationality = data.nationality,
                    nationalityISO = data.nationalityISO,
                    color = data.colour.toColorInt()
            )

    )
}

fun FConstructorOverviewStandingsDriver.convert(driver: ConstructorDriver): ConstructorOverviewDriverStanding {
    return ConstructorOverviewDriverStanding(
            driver = driver,
            bestFinish = this.bF ?: -1,
            bestQualifying = this.bQ ?: -1,
            points = this.p ?: 0,
            finishesInP1 = this.p1 ?: 0,
            finishesInP2 = this.p2 ?: 0,
            finishesInP3 = this.p3 ?: 0,
            finishesInPoints = this.pF ?: 0,
            qualifyingP1 = this.q1 ?: 0,
            qualifyingP2 = this.q2 ?: 0,
            qualifyingP3 = this.q3 ?: 0,
            qualifyingTop10 = this.q10,
            races = this.races ?: 0,
            championshipStanding = this.pos ?: -1
    )
}