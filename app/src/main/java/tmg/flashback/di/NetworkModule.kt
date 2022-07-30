package tmg.flashback.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tmg.flashback.managers.AppNetworkConfigManager
import tmg.flashback.statistics.network.NetworkConfigManager

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    fun providesNetworkConfigManager(impl: AppNetworkConfigManager): NetworkConfigManager = impl
}