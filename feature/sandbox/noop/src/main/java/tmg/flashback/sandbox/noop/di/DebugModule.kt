package tmg.flashback.sandbox.noop.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tmg.flashback.sandbox.DebugNavigationComponent
import tmg.flashback.sandbox.manager.BaseUrlLocalOverrideManager
import tmg.flashback.sandbox.noop.NoopDebugNavigationComponent
import tmg.flashback.sandbox.noop.manager.NoopBaseUrlLocalOverrideManager

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DebugModule {

    @Binds
    abstract fun bindsBaseUrlLocalOverrideManager(impl: NoopBaseUrlLocalOverrideManager): BaseUrlLocalOverrideManager

    @Binds
    abstract fun bindsDebugNavigationComponent(impl: NoopDebugNavigationComponent): DebugNavigationComponent
}