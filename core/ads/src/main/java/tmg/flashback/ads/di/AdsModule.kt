package tmg.flashback.ads.di

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tmg.flashback.ads.controller.AdsController
import tmg.flashback.ads.manager.AdsManager
import tmg.flashback.ads.repository.AdsRepository
import tmg.flashback.ads.ui.settings.adverts.SettingsAdvertViewModel

val adsModule = module {

    viewModel { SettingsAdvertViewModel(get()) }

    single { AdsManager(get()) }
    single { AdsRepository(get(), get()) }
    single { AdsController(get(), get()) }
}