package tmg.flashback.data.persistence.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import tmg.flashback.data.persistence.FlashbackDatabase
import java.util.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class RoomModule {

    @Provides
    @Singleton
    fun flashbackDatabase(@ApplicationContext applicationContext: Context) = Room
        .databaseBuilder(applicationContext, FlashbackDatabase::class.java, "flashback-database")
        .addMigrations(
            MIGRATION_1_2,
            MIGRATION_2_3,
            MIGRATION_3_4,
            MIGRATION_4_5,
            MIGRATION_5_6,
            MIGRATION_6_7,
            MIGRATION_7_8,
            MIGRATION_8_9,
            MIGRATION_9_10,
            MIGRATION_10_11,
        )
        .build()

}

private val MIGRATION_1_2 = object : Migration(1,2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE RaceInfo ADD COLUMN youtube TEXT DEFAULT NULL")
        Log.i("Database", "Migrated DB from version $startVersion to $endVersion")
    }
}

private val MIGRATION_2_3 = object : Migration(2,3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("CREATE TABLE IF NOT EXISTS Event (" +
                "label TEXT NOT NULL, " +
                "date TEXT NOT NULL, " +
                "type TEXT NOT NULL, " +
                "season INTEGER NOT NULL, " +
                "id TEXT NOT NULL PRIMARY KEY)")
        Log.i("Database", "Migrated DB from version $startVersion to $endVersion")
    }
}

private val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE RaceInfo ADD COLUMN laps TEXT DEFAULT NULL")
        database.execSQL("ALTER TABLE Overview ADD COLUMN laps TEXT DEFAULT NULL")
        Log.i("Database", "Migrated DB from version $startVersion to $endVersion")
    }
}

private val MIGRATION_4_5 = object : Migration(4, 5) {
    override fun migrate(database: SupportSQLiteDatabase) {

        // Remove the Sprint Qualifying fields from QualifyingResult
        database.execSQL("CREATE TABLE QualifyingResult_Temp (" +
                "driver_id TEXT NOT NULL," +
                "season INTEGER NOT NULL," +
                "round INTEGER NOT NULL," +
                "constructor_id TEXT NOT NULL," +
                "qualified INTEGER," +
                "q1 TEXT," +
                "q2 TEXT," +
                "q3 TEXT," +
                "id TEXT NOT NULL PRIMARY KEY," +
                "season_round_id TEXT NOT NULL)")
        database.execSQL("INSERT INTO QualifyingResult_Temp " +
                "(driver_id, season, round, constructor_id, qualified, q1, q2, q3, id, season_round_id) " +
                "SELECT driver_id, season, round, constructor_id, qualified, q1, q2, q3, id, season_round_id " +
                "FROM QualifyingResult")
        database.execSQL("DROP TABLE QualifyingResult")
        database.execSQL("ALTER TABLE QualifyingResult_Temp RENAME TO QualifyingResult")

        // Add SprintResult
        database.execSQL("CREATE TABLE IF NOT EXISTS SprintResult (" +
                "driver_id TEXT NOT NULL, " +
                "season INTEGER NOT NULL, " +
                "round INTEGER NOT NULL, " +
                "constructor_id TEXT NOT NULL, " +
                "points REAL NOT NULL, " +
                "grid_position INTEGER, " +
                "finished INTEGER NOT NULL, " +
                "status TEXT NOT NULL," +
                "time TEXT, " +
                "id TEXT NOT NULL PRIMARY KEY," +
                "season_round_id TEXT NOT NULL)")
    }
}

private val MIGRATION_5_6 = object : Migration(5, 6) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE Overview ADD COLUMN has_sprint_data INTEGER NOT NULL DEFAULT 0")
        Log.i("Database", "Migrated DB from version $startVersion to $endVersion")
    }
}

private val MIGRATION_6_7 = object : Migration(6, 7) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE CircuitRound ADD COLUMN season_round TEXT NOT NULL DEFAULT 'CIRCUIT_ROUND_ADDED'")
        database.execSQL("ALTER TABLE CircuitRoundResult ADD COLUMN season_round_id TEXT NOT NULL DEFAULT 'CIRCUIT_ROUND_RESULT_ADDED'")
        Log.i("Database", "Migrated DB from version $startVersion to $endVersion")
    }
}

private val MIGRATION_7_8 = object : Migration(7, 8) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE Constructor ADD COLUMN photoUrl TEXT DEFAULT NULL")
        Log.i("Database", "Migrated DB from version $startVersion to $endVersion")
    }
}

private val MIGRATION_8_9 = object : Migration(8, 9) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE SprintResult RENAME TO SprintRaceResult")
        database.execSQL("CREATE TABLE IF NOT EXISTS SprintQualifyingResult (" +
                "driver_id TEXT NOT NULL, " +
                "season INTEGER NOT NULL, " +
                "round INTEGER NOT NULL, " +
                "constructor_id TEXT NOT NULL, " +
                "qualified INTEGER NOT NULL, " +
                "sq1 TEXT, " +
                "sq2 TEXT, " +
                "sq3 TEXT, " +
                "id TEXT NOT NULL PRIMARY KEY," +
                "season_round_id TEXT NOT NULL)")


        // Change Qualified to be not null
        database.execSQL("CREATE TABLE QualifyingResult_Temp (" +
                "driver_id TEXT NOT NULL," +
                "season INTEGER NOT NULL," +
                "round INTEGER NOT NULL," +
                "constructor_id TEXT NOT NULL," +
                "qualified INTEGER NOT NULL," +
                "q1 TEXT," +
                "q2 TEXT," +
                "q3 TEXT," +
                "id TEXT NOT NULL PRIMARY KEY," +
                "season_round_id TEXT NOT NULL)")
        database.execSQL("INSERT INTO QualifyingResult_Temp " +
                "(driver_id, season, round, constructor_id, qualified, q1, q2, q3, id, season_round_id) " +
                "SELECT driver_id, season, round, constructor_id, qualified, q1, q2, q3, id, season_round_id " +
                "FROM QualifyingResult")
        database.execSQL("DROP TABLE QualifyingResult")
        database.execSQL("ALTER TABLE QualifyingResult_Temp RENAME TO QualifyingResult")

        Log.i("Database", "Migrated DB from version $startVersion to $endVersion")
    }
}


private val MIGRATION_9_10 = object : Migration(9, 10) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE Schedule ADD COLUMN rain_percent REAL")
        database.execSQL("ALTER TABLE Schedule ADD COLUMN wind_ms REAL")
        database.execSQL("ALTER TABLE Schedule ADD COLUMN wind_bearing INTEGER")
        database.execSQL("ALTER TABLE Schedule ADD COLUMN temp_max_c REAL")
        database.execSQL("ALTER TABLE Schedule ADD COLUMN temp_min_c REAL")
        database.execSQL("ALTER TABLE Schedule ADD COLUMN weather_summary TEXT")
        Log.i("Database", "Migrated DB from version $startVersion to $endVersion")
    }
}


private val MIGRATION_10_11 = object : Migration(10, 11) {
    override fun migrate(database: SupportSQLiteDatabase) {

        database.execSQL("CREATE TABLE DriverSeasonRace_Temp (" +
                "driver_id TEXT NOT NULL," +
                "season INTEGER NOT NULL," +
                "round INTEGER NOT NULL," +
                "constructor_id TEXT NOT NULL," +
                "sprint_qualifying INTEGER NOT NULL DEFAULT 0," +
                "sprint_race INTEGER NOT NULL DEFAULT 0," +
                "qualified INTEGER," +
                "gridPos INTEGER," +
                "finished INTEGER NOT NULL," +
                "status TEXT NOT NULL," +
                "points REAL NOT NULL," +
                "id TEXT NOT NULL PRIMARY KEY," +
                "driver_season_id TEXT NOT NULL," +
                "season_round_id TEXT NOT NULL" +
        ")")
        database.execSQL("INSERT INTO DriverSeasonRace_Temp " +
                "(driver_id, season, round, constructor_id, sprint_qualifying, qualified, gridPos, finished, status, points, id, driver_season_id, season_round_id) " +
                "SELECT driver_id, season, round, constructor_id, is_sprint_quali, qualified, gridPos, finished, status, points, id, driver_season_id, season_round_id " +
                "FROM DriverSeasonRace")
        database.execSQL("DROP TABLE DriverSeasonRace")
        database.execSQL("ALTER TABLE DriverSeasonRace_Temp RENAME TO DriverSeasonRace")

//        database.execSQL("ALTER TABLE DriverSeasonRace RENAME COLUMN is_sprint_quali TO sprint_qualifying")
//        database.execSQL("ALTER TABLE DriverSeasonRace ADD COLUMN sprint_race INTEGER NOT NULL DEFAULT 0")
        Log.i("Database", "Migrated DB from version $startVersion to $endVersion")
    }
}