package tmg.flashback.ads.di

import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tmg.flashback.ads.manager.AdsManager
import tmg.flashback.ads.repository.AdsRepository
import tmg.flashback.ads.ui.settings.adverts.SettingsAdvertViewModel
import tmg.flashback.ads.usecases.ClearCachedAdvertsUseCase
import tmg.flashback.ads.usecases.GetAdUseCase
import tmg.flashback.ads.usecases.InitialiseAdsUseCase

val adsModule = module {

    viewModel { SettingsAdvertViewModel(get()) }

    single { AdsManager() }
    single { AdsRepository(get(), get()) }

    factory { ClearCachedAdvertsUseCase(get()) }
    factory { InitialiseAdsUseCase(androidContext(), get(), get()) }
    factory { GetAdUseCase(get(), get()) }
}