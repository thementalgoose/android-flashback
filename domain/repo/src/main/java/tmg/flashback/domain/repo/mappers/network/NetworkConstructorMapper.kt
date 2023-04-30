package tmg.flashback.domain.repo.mappers.network

import tmg.flashback.flashbackapi.api.models.constructors.ConstructorHistoryStanding
import tmg.flashback.flashbackapi.api.models.constructors.ConstructorHistoryStandingDriver
import tmg.flashback.domain.persistence.models.constructors.ConstructorSeason
import tmg.flashback.domain.persistence.models.constructors.ConstructorSeasonDriver
import javax.inject.Inject

class NetworkConstructorMapper @Inject constructor() {

    @Throws(RuntimeException::class)
    fun mapConstructorSeason(constructorId: String, constructor: ConstructorHistoryStanding): ConstructorSeason {
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
    fun mapConstructorSeasonDriver(constructorId: String, season: Int, data: ConstructorHistoryStandingDriver): ConstructorSeasonDriver {
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