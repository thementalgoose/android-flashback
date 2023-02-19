package tmg.flashback.ads.adsnoop.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tmg.flashback.ads.ads.components.AdvertProvider
import tmg.flashback.ads.ads.usecases.InitialiseAdsUseCase
import tmg.flashback.ads.adsnoop.components.NoopNativeBannerProviderImpl
import tmg.flashback.ads.adsnoop.usecases.NoopInitialiseAdsUseCaseImpl

@Module
@InstallIn(SingletonComponent::class)
internal class AdsModule {

    @Provides
    fun provideNoopInitialiseAdsUseCase(impl: NoopInitialiseAdsUseCaseImpl): InitialiseAdsUseCase = impl

    @Provides
    fun provideNoopNativeBannerProvider(impl: NoopNativeBannerProviderImpl): AdvertProvider = impl
}