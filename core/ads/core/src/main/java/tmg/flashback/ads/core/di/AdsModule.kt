package tmg.flashback.ads.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tmg.flashback.ads.contract.components.AdvertProvider
import tmg.flashback.ads.contract.usecases.InitialiseAdsUseCase
import tmg.flashback.ads.core.components.NativeBannerProviderImpl
import tmg.flashback.ads.core.usecases.InitialiseAdsUseCaseImpl

@Module
@InstallIn(SingletonComponent::class)
internal class AdsModule {

    @Provides
    fun provideInitialiseAdsUseCase(impl: InitialiseAdsUseCaseImpl): InitialiseAdsUseCase = impl

    @Provides
    fun provideNativeBannerProvider(impl: NativeBannerProviderImpl): AdvertProvider = impl
}