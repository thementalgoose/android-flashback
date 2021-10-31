package tmg.flashback.statistics.room.dao

import androidx.room.Dao
import androidx.room.Query
import tmg.flashback.statistics.room.models.constructors.Constructor

@Dao
interface ConstructorDao {

    @Query("SELECT * FROM constructor WHERE id == :id LIMIT 1")
    fun getConstructor(id: String): Constructor?
}