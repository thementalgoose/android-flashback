package tmg.flashback.ads.noop.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tmg.flashback.ads.contract.components.NativeBannerProvider
import tmg.flashback.ads.noop.components.NoopNativeBannerProviderImpl

@Module
@InstallIn(SingletonComponent::class)
internal class AdsModule {

    @Provides
    fun provideNoopNativeBannerProvider(impl: NoopNativeBannerProviderImpl): NativeBannerProvider = impl
}