package tmg.flashback.ads.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tmg.flashback.ads.contract.components.NativeBannerProvider
import tmg.flashback.ads.core.components.NativeBannerProviderImpl

@Module
@InstallIn(SingletonComponent::class)
internal class AdsModule {

    @Provides
    fun provideNativeBannerProvider(impl: NativeBannerProviderImpl): NativeBannerProvider = impl
}