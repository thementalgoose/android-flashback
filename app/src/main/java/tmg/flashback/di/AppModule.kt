package tmg.flashback.di

import org.koin.dsl.module
import tmg.flashback.FlashbackStartup

val appModule = module {

    single { FlashbackStartup(get(), get(), get(), get(), get(), get()) }
}