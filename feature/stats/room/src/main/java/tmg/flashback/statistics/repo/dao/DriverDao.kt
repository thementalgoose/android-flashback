package tmg.flashback.statistics.repo.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import tmg.flashback.statistics.repo.models.drivers.Driver

@Dao
interface DriverDao {

    @Query("SELECT * FROM driver WHERE id == :id LIMIT 1")
    fun getDriver(id: String): Driver?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDrivers(vararg driver: Driver)
}