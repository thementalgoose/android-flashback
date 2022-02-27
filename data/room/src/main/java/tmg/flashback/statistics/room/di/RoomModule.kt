package tmg.flashback.statistics.room.di

import android.util.Log
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import org.koin.dsl.module
import tmg.flashback.statistics.room.FlashbackDatabase

val roomModule = module {
    single { Room
        .databaseBuilder(get(), FlashbackDatabase::class.java, "flashback-database")
        .addMigrations(
            MIGRATION_1_2,
            MIGRATION_2_3,
            MIGRATION_3_4,
        )
        .build()
    }
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