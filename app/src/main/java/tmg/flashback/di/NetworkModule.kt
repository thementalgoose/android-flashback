package tmg.flashback.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tmg.flashback.flashbackapi.api.NetworkConfigManager
import tmg.flashback.flashbacknews.api.NewsNetworkConfigManager
import tmg.flashback.managers.AppNetworkConfigManager

@Module
@InstallIn(SingletonComponent::class)
interface NetworkModule {

    @Binds
    fun providesNetworkConfigManager(impl: AppNetworkConfigManager): NetworkConfigManager

    @Binds
    fun providesNewsNetworkConfigManager(impl: AppNetworkConfigManager): NewsNetworkConfigManager
}