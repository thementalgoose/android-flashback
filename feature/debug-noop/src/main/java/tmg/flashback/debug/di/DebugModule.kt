package tmg.flashback.debug.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tmg.flashback.debug.manager.DebugBaseUrlOverrideManager
import tmg.flashback.statistics.network.manager.BaseUrlLocalOverrideManager

@Module
@InstallIn(SingletonComponent::class)
abstract class DebugModule {

    @Binds
    abstract fun bindsBaseUrlLocalOverrideManager(impl: DebugBaseUrlOverrideManager): BaseUrlLocalOverrideManager
}