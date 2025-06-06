package tmg.flashback.data.persistence.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import tmg.flashback.data.persistence.models.drivers.Driver
import tmg.flashback.data.persistence.models.drivers.DriverHistory
import tmg.flashback.data.persistence.models.drivers.DriverSeason
import tmg.flashback.data.persistence.models.drivers.DriverSeasonRace

@Dao
interface DriverDao {

    @Query("SELECT * FROM driver")
    fun getDrivers(): Flow<List<Driver>>

    @Query("SELECT * FROM driver WHERE id == :id LIMIT 1")
    fun getDriver(id: String): Flow<Driver?>

    @Transaction
    @Query("SELECT * FROM driver WHERE id == :id LIMIT 1")
    fun getDriverHistory(id: String): Flow<DriverHistory?>

    @Query("SELECT COUNT(*) FROM DriverSeason WHERE driver_id == :id")
    suspend fun getDriverSeasonCount(id: String): Int

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