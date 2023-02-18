package tmg.flashback.debug.noop.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tmg.flashback.debug.DebugNavigationComponent
import tmg.flashback.debug.manager.BaseUrlLocalOverrideManager
import tmg.flashback.debug.noop.NoopDebugNavigationComponent
import tmg.flashback.debug.noop.manager.NoopBaseUrlLocalOverrideManager

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DebugModule {

    @Binds
    abstract fun bindsBaseUrlLocalOverrideManager(impl: NoopBaseUrlLocalOverrideManager): BaseUrlLocalOverrideManager

    @Binds
    abstract fun bindsDebugNavigationComponent(impl: NoopDebugNavigationComponent): DebugNavigationComponent
}