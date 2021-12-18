package tmg.flashback.statistics.room.di

import android.util.Log
import androidx.room.Room
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import org.koin.dsl.module
import tmg.flashback.statistics.room.FlashbackDatabase

val roomModule = module {
    single { Room
        .databaseBuilder(get(), FlashbackDatabase::class.java, "flashback-database")
        .addMigrations(MIGRATION_1_2)
        .build()
    }
}

private val MIGRATION_1_2 = object : Migration(1,2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE RaceInfo ADD COLUMN youtube TEXT DEFAULT NULL")
        Log.i("Database", "Migrated DB from version $startVersion to $endVersion")
    }
}