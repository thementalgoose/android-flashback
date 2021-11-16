package tmg.flashback.ads.di

import org.koin.dsl.module
import tmg.flashback.ads.controller.AdsController
import tmg.flashback.ads.manager.AdsManager
import tmg.flashback.ads.repository.AdsRepository

val adsModule = module {

    single { AdsManager(get()) }
    single { AdsRepository(get()) }
    single { AdsController(get(), get()) }
}