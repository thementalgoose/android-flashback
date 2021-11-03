package tmg.flashback.statistics.room.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import tmg.flashback.statistics.room.models.race.standings.*

@Dao
interface SeasonStandingsDao {

    //region Driver Standings

    @Transaction
    @Query("SELECT * FROM DriverStanding WHERE season == :season")
    fun getDriverStandings(season: Int): Flow<List<DriverStandingWithConstructors>>

    @Transaction
    fun insertDriverStandings(standing: DriverStanding, constructors: List<DriverStandingConstructor>) {
        deleteDriverStandingDrivers(standing.driverId, standing.season)
        insertDriverStandingConstructors(constructors)
        insertDriverStandings(standing)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDriverStandings(standing: DriverStanding)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDriverStandings(standing: List<DriverStanding>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDriverStandingConstructors(constructors: List<DriverStandingConstructor>)

    @Query("DELETE FROM DriverStandingConstructor WHERE season == :season AND driver_id == :driverId")
    fun deleteDriverStandingDrivers(driverId: String, season: Int)

    //endregion

    //region Constructor Standings

    @Transaction
    @Query("SELECT * FROM ConstructorStanding WHERE season == :season")
    fun getConstructorStandings(season: Int): Flow<List<ConstructorStandingWithDrivers>>

    @Transaction
    fun insertConstructorStandings(standing: ConstructorStanding, drivers: List<ConstructorStandingDriver>) {
        deleteConstructorStandingDrivers(standing.constructorId, standing.season)
        insertConstructorStandingDrivers(drivers)
        insertConstructorStandings(standing)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertConstructorStandings(standing: ConstructorStanding)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertConstructorStandings(standing: List<ConstructorStanding>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertConstructorStandingDrivers(drivers: List<ConstructorStandingDriver>)

    @Query("DELETE FROM ConstructorStandingDriver WHERE season == :season AND constructor_id == :constructorId")
    fun deleteConstructorStandingDrivers(constructorId: String, season: Int)

    //endregion
}