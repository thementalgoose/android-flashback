package tmg.flashback.data.persistence.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import tmg.flashback.data.persistence.models.constructors.Constructor
import tmg.flashback.data.persistence.models.constructors.ConstructorHistory
import tmg.flashback.data.persistence.models.constructors.ConstructorSeason
import tmg.flashback.data.persistence.models.constructors.ConstructorSeasonDriver

@Dao
interface ConstructorDao {

    @Query("SELECT * FROM Constructor")
    fun getConstructors(): Flow<List<Constructor>>

    @Query("SELECT * FROM Constructor WHERE id == :id LIMIT 1")
    fun getConstructor(id: String): Constructor?

    @Transaction
    @Query("SELECT * FROM Constructor WHERE id == :id LIMIT 1")
    fun getConstructorHistory(id: String): Flow<ConstructorHistory?>

    @Query("SELECT COUNT(*) FROM ConstructorSeason WHERE constructor_id == :id")
    fun getConstructorSeasonCount(id: String): Int

    @Transaction
    fun insertConstructor(constructor: Constructor, constructorSeasons: List<ConstructorSeason>, constructorSeasonRaces: List<ConstructorSeasonDriver>) {
        insertConstructorSeasonDrivers(constructorSeasonRaces)
        insertConstructorSeasons(constructorSeasons)
        insert(constructor)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(constructor: Constructor)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(constructors: List<Constructor>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertConstructorSeasons(constructorSeasons: List<ConstructorSeason>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertConstructorSeasonDrivers(constructorSeasonDrivers: List<ConstructorSeasonDriver>)

}