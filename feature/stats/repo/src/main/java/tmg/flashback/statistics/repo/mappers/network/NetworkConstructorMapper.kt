package tmg.flashback.statistics.repo.mappers.network

import tmg.flashback.statistics.network.models.constructors.ConstructorStanding
import tmg.flashback.statistics.network.models.constructors.ConstructorStandingDriver
import tmg.flashback.statistics.room.models.constructors.ConstructorSeason
import tmg.flashback.statistics.room.models.constructors.ConstructorSeasonDriver

class NetworkConstructorMapper {

    @Throws(RuntimeException::class)
    fun mapConstructorSeason(constructorId: String, constructor: ConstructorStanding): ConstructorSeason {
        return ConstructorSeason(
            constructorId = constructorId,
            season = constructor.season,
            championshipStanding = constructor.championshipPosition,
            points = constructor.points ?: 0.0,
            inProgress = constructor.inProgress,
            races = constructor.races ?: 0
        )
    }

    @Throws(RuntimeException::class)
    fun mapConstructorSeasonDriver(constructorId: String, season: Int, data: ConstructorStandingDriver): ConstructorSeasonDriver {
        return ConstructorSeasonDriver(
            constructorId = constructorId,
            season = season,
            driverId = data.driver.id,
            points = data.points,
            championshipPosition = data.championshipPosition,
            wins = data.wins ?: 0,
            races = data.races ?: 0,
            podiums = data.podiums ?: 0,
            pointsFinishes = data.pointsFinishes ?: 0,
            pole = data.pole ?: 0
        )
    }
}