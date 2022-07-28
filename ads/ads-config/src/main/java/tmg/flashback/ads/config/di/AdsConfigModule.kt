package tmg.flashback.ads.config.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tmg.flashback.ads.config.AdsNavigationComponent
import tmg.flashback.ads.config.repository.AdsRepository
import tmg.flashback.ads.config.ui.settings.adverts.SettingsAdvertViewModel

val adsConfigModule = module {

    viewModel { SettingsAdvertViewModel(get()) }

    single { AdsRepository(get(), get()) }

    single { AdsNavigationComponent(get()) }
}