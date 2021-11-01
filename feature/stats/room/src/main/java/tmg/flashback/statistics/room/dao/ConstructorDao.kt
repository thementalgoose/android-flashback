package tmg.flashback.statistics.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import tmg.flashback.statistics.room.models.constructors.Constructor
import tmg.flashback.statistics.room.models.drivers.Driver

@Dao
interface ConstructorDao {

    @Query("SELECT * FROM constructor WHERE id == :id LIMIT 1")
    fun getConstructor(id: String): Constructor?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(constructors: List<Constructor>)
}