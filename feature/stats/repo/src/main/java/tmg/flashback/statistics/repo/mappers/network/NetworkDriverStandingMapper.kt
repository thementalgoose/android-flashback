package tmg.flashback.statistics.repo.mappers.network

import tmg.flashback.statistics.network.models.races.ConstructorStandings
import tmg.flashback.statistics.network.models.races.DriverStandings
import tmg.flashback.statistics.room.models.race.standings.DriverStanding
import tmg.flashback.statistics.room.models.race.standings.DriverStandingConstructor
import java.lang.RuntimeException
import kotlin.jvm.Throws

class NetworkDriverStandingMapper {

    @Throws(RuntimeException::class)
    fun mapDriverStanding(season: Int, data: DriverStandings): DriverStanding {
        return DriverStanding(
            driverId = data.driverId,
            season = season,
            points = data.points,
            position = data.position,
            inProgress = data.inProgress,
            races = data.races
        )
    }

    @Throws(RuntimeException::class)
    fun mapDriverStandingConstructor(season: Int, data: DriverStandings): List<DriverStandingConstructor> {
        return data.constructors.map { (driverId, points) ->
            DriverStandingConstructor(
                constructorId = data.driverId,
                season = season,
                driverId = driverId,
                points = points,
            )
        }
    }
}