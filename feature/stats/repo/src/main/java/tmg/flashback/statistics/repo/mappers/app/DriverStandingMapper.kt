package tmg.flashback.statistics.repo.mappers.app

import tmg.flashback.formula1.model.SeasonDriverStandingSeason
import tmg.flashback.formula1.model.SeasonDriverStandingSeasonConstructor
import tmg.flashback.formula1.model.SeasonDriverStandings
import tmg.flashback.statistics.room.models.standings.DriverStandingConstructorWithConstructor
import tmg.flashback.statistics.room.models.standings.DriverStandingWithConstructors

class DriverStandingMapper(
    private val driverDataMapper: DriverDataMapper,
    private val constructorDataMapper: ConstructorDataMapper
) {

    fun mapDriverStanding(list: List<DriverStandingWithConstructors>): SeasonDriverStandings? {
        if (list.isEmpty()) return null
        return SeasonDriverStandings(
            driver = driverDataMapper.mapDriver(list.first().driver),
            standings = list.map {
                mapDriverStanding(it)
            }
        )
    }

    fun mapDriverStanding(data: DriverStandingWithConstructors): SeasonDriverStandingSeason {
        return SeasonDriverStandingSeason(
            season = data.standing.season,
            driver = driverDataMapper.mapDriver(data.driver),
            points = data.standing.points,
            inProgress = data.standing.inProgress,
            inProgressName = data.standing.inProgressName,
            inProgressRound = data.standing.inProgressRound,
            races = data.standing.races,
            championshipPosition = data.standing.position,
            constructors = data.constructors.map {
                mapDriverStandingConstructor(it)
            },
        )
    }

    private fun mapDriverStandingConstructor(data: DriverStandingConstructorWithConstructor): SeasonDriverStandingSeasonConstructor {
        return SeasonDriverStandingSeasonConstructor(
            points = data.standing.points,
            constructor = constructorDataMapper.mapConstructorData(data.constructor)
        )
    }
}