package tmg.flashback.statistics.repo.mappers.network

import tmg.flashback.statistics.network.models.races.ConstructorStandings
import tmg.flashback.statistics.room.models.standings.ConstructorStanding
import tmg.flashback.statistics.room.models.standings.ConstructorStandingDriver
import java.lang.RuntimeException
import kotlin.jvm.Throws

class NetworkConstructorStandingMapper {

    @Throws(RuntimeException::class)
    fun mapConstructorStanding(season: Int, data: ConstructorStandings): ConstructorStanding {
        return ConstructorStanding(
            constructorId = data.constructorId,
            season = season,
            points = data.points,
            position = data.position,
            inProgress = data.inProgress ?: false,
            inProgressName = data.inProgressInfo?.name,
            inProgressRound = data.inProgressInfo?.round,
            races = data.races ?: 0
        )
    }

    @Throws(RuntimeException::class)
    fun mapConstructorStandingDriver(season: Int, data: ConstructorStandings): List<ConstructorStandingDriver> {
        return data.drivers.map { (driverId, points) ->
            ConstructorStandingDriver(
                constructorId = data.constructorId,
                season = season,
                driverId = driverId,
                points = points,
            )
        }
    }
}