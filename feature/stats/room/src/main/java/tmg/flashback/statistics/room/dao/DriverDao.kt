package tmg.flashback.statistics.room.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import tmg.flashback.statistics.room.models.drivers.Driver
import tmg.flashback.statistics.room.models.drivers.DriverHistory
import tmg.flashback.statistics.room.models.drivers.DriverSeason
import tmg.flashback.statistics.room.models.drivers.DriverSeasonRace

@Dao
interface DriverDao {

    @Query("SELECT * FROM driver WHERE id == :id LIMIT 1")
    fun getDriver(id: String): Flow<Driver?>

    @Transaction
    @Query("SELECT * FROM driver WHERE id == :id LIMIT 1")
    fun getDriverHistory(id: String): Flow<DriverHistory?>

    @Transaction
    fun insertDriver(driver: Driver, driverSeasons: List<DriverSeason>, driverSeasonRaces: List<DriverSeasonRace>) {
        insertDriverSeasonRaces(driverSeasonRaces)
        insertDriverSeasons(driverSeasons)
        insert(driver)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(driver: Driver)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(drivers: List<Driver>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDriverSeasons(driverSeasons: List<DriverSeason>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDriverSeasonRaces(driverSeasonRaces: List<DriverSeasonRace>)
}