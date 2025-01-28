package tmg.flashback.sandbox.noop.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tmg.flashback.sandbox.SandboxNavigationComponent
import tmg.flashback.sandbox.manager.BaseUrlLocalOverrideManager
import tmg.flashback.sandbox.noop.NoopSandboxNavigationComponent
import tmg.flashback.sandbox.noop.manager.NoopBaseUrlLocalOverrideManager

@Module
@InstallIn(SingletonComponent::class)
internal abstract class SandboxModule {

    @Binds
    abstract fun bindsBaseUrlLocalOverrideManager(impl: NoopBaseUrlLocalOverrideManager): BaseUrlLocalOverrideManager

    @Binds
    abstract fun bindsDebugNavigationComponent(impl: NoopSandboxNavigationComponent): SandboxNavigationComponent
}