package tmg.flashback.device.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tmg.flashback.device.managers.AndroidNetworkConnectivityManager
import tmg.flashback.device.managers.NetworkConnectivityManager

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DeviceModule {

    @Binds
    abstract fun bindNetworkConnectivityManager(impl: AndroidNetworkConnectivityManager): NetworkConnectivityManager
}