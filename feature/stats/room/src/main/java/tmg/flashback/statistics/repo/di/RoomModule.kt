package tmg.flashback.statistics.repo.di

import androidx.room.Room
import org.koin.dsl.module
import tmg.flashback.statistics.repo.FlashbackDatabase

val roomModule = module {
    single { Room
        .databaseBuilder(get(), FlashbackDatabase::class.java, "flashback-database")
        .build()
    }
}