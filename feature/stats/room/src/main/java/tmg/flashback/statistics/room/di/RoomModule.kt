package tmg.flashback.statistics.room.di

import androidx.room.Room
import org.koin.dsl.module
import tmg.flashback.statistics.room.FlashbackDatabase

val roomModule = module {
    single { Room
        .databaseBuilder(get(), FlashbackDatabase::class.java, "flashback-database")
        .build()
    }
}