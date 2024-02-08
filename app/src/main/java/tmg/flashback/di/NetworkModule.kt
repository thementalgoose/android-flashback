package tmg.flashback.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tmg.flashback.flashbackapi.api.NetworkConfigManager
import tmg.flashback.flashbacknews.api.NewsNetworkConfigManager
import tmg.flashback.managers.AppNetworkConfigManager

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    fun providesNetworkConfigManager(impl: AppNetworkConfigManager): NetworkConfigManager = impl

    // News temporarily served by flashback.pages.dev until better solution found
    @Provides
    fun providesNewsNetworkConfigManager(impl: AppNetworkConfigManager): NewsNetworkConfigManager = impl
}