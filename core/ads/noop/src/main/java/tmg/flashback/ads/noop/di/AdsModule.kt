package tmg.flashback.ads.noop.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tmg.flashback.ads.contract.components.AdvertProvider
import tmg.flashback.ads.contract.usecases.InitialiseAdsUseCase
import tmg.flashback.ads.noop.components.NoopNativeBannerProviderImpl
import tmg.flashback.ads.noop.usecases.NoopInitialiseAdsUseCaseImpl

@Module
@InstallIn(SingletonComponent::class)
internal class AdsModule {

    @Provides
    fun provideNoopInitialiseAdsUseCase(impl: NoopInitialiseAdsUseCaseImpl): InitialiseAdsUseCase = impl

    @Provides
    fun provideNoopNativeBannerProvider(impl: NoopNativeBannerProviderImpl): AdvertProvider = impl
}