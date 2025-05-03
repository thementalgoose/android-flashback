package tmg.flashback.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import tmg.flashback.device.managers.BuildConfigManager
import tmg.flashback.device.managers.DeviceConfigManager
import tmg.flashback.managers.buildconfig.AppBuildConfigManager
import tmg.flashback.managers.deviceconfig.AppDeviceConfigManager

@Module
@InstallIn(SingletonComponent::class)
interface AppModule {

    @Binds
    fun providesDeviceConfigManager(impl: AppDeviceConfigManager): DeviceConfigManager

    @Binds
    fun providesBuildConfigManager(impl: AppBuildConfigManager): BuildConfigManager
}

