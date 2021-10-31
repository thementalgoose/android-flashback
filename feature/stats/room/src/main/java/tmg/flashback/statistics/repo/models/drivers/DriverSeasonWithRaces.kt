package tmg.flashback.statistics.repo.models.drivers

import androidx.room.Embedded
import androidx.room.Relation

data class DriverSeasonWithRaces(
    @Embedded
    val driverSeason: DriverSeason,
    @Relation(
        entity = DriverSeasonRace::class,
        parentColumn = "id",
        entityColumn = "driver_season_race_id"
    )
    val races: List<DriverSeasonRaceWithConstructor>
)