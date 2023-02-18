package tmg.flashback.ads.adsadmob.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tmg.flashback.ads.ads.components.AdvertProvider
import tmg.flashback.ads.ads.usecases.InitialiseAdsUseCase
import tmg.flashback.ads.adsadmob.components.NativeBannerProviderImpl
import tmg.flashback.ads.adsadmob.usecases.InitialiseAdsUseCaseImpl

@Module
@InstallIn(SingletonComponent::class)
internal class AdsModule {

    @Provides
    fun provideInitialiseAdsUseCase(impl: InitialiseAdsUseCaseImpl): InitialiseAdsUseCase = impl

    @Provides
    fun provideNativeBannerProvider(impl: NativeBannerProviderImpl): AdvertProvider = impl
}