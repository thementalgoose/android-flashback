package tmg.flashback.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import tmg.flashback.device.managers.BuildConfigManager
import tmg.flashback.managers.buildconfig.AppBuildConfigManager

@Module
@InstallIn(SingletonComponent::class)
class AppModule() {

    @Provides
    fun providesBuildConfigManager(impl: AppBuildConfigManager): BuildConfigManager = impl

    @Provides
    fun providesCoroutineDispatcher(): CoroutineDispatcher = Dispatchers.IO
}