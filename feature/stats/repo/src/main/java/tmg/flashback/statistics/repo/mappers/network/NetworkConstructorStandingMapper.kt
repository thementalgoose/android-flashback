package tmg.flashback.statistics.repo.mappers.network

import tmg.flashback.statistics.network.models.races.ConstructorStandings
import tmg.flashback.statistics.room.models.race.standings.ConstructorStanding
import tmg.flashback.statistics.room.models.race.standings.ConstructorStandingDriver
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
            inProgress = data.inProgress,
            races = data.races
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