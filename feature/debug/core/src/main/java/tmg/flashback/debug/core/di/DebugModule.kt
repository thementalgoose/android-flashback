package tmg.flashback.debug.core.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tmg.flashback.debug.DebugNavigationComponent
import tmg.flashback.debug.core.DebugNavigationComponentImpl
import tmg.flashback.debug.manager.BaseUrlLocalOverrideManager
import tmg.flashback.debug.core.manager.DebugBaseUrlOverrideManager

@Module
@InstallIn(SingletonComponent::class)
abstract class DebugModule {

    @Binds
    abstract fun bindsBaseUrlLocalOverrideManager(impl: DebugBaseUrlOverrideManager): BaseUrlLocalOverrideManager

    @Binds
    abstract fun bindsDebugNavigationComponent(impl: DebugNavigationComponentImpl): DebugNavigationComponent
}