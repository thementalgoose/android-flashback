package tmg.flashback.statistics.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import tmg.flashback.statistics.room.models.drivers.Driver

@Dao
interface DriverDao {

    @Query("SELECT * FROM driver WHERE id == :id LIMIT 1")
    fun getDriver(id: String): Driver?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDrivers(vararg driver: Driver)
}