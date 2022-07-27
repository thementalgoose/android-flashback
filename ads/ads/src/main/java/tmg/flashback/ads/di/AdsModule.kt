package tmg.flashback.ads.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import tmg.flashback.ads.manager.AdsManager
import tmg.flashback.ads.repository.AdsCacheRepository
import tmg.flashback.ads.usecases.ClearCachedAdvertsUseCase
import tmg.flashback.ads.usecases.GetAdUseCase
import tmg.flashback.ads.usecases.InitialiseAdsUseCase

val adsModule = module {

    single { AdsManager() }

    factory { InitialiseAdsUseCase(androidContext(), get(), get()) }
    factory { GetAdUseCase(get(), get(), get()) }
    factory { ClearCachedAdvertsUseCase(get()) }

    single { AdsCacheRepository() }
}