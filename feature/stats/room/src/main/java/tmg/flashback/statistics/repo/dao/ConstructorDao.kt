package tmg.flashback.statistics.repo.dao

import androidx.room.Dao
import androidx.room.Query
import tmg.flashback.statistics.repo.models.constructors.Constructor

@Dao
interface ConstructorDao {

    @Query("SELECT * FROM constructor WHERE id == :id LIMIT 1")
    fun getConstructor(id: String): Constructor?
}