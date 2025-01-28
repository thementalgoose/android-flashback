package tmg.flashback.sandbox.core.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tmg.flashback.sandbox.SandboxNavigationComponent
import tmg.flashback.sandbox.core.SandboxNavigationComponentImpl
import tmg.flashback.sandbox.core.manager.SandboxBaseUrlOverrideManager
import tmg.flashback.sandbox.manager.BaseUrlLocalOverrideManager

@Module
@InstallIn(SingletonComponent::class)
internal abstract class SandboxModule {

    @Binds
    abstract fun bindsBaseUrlLocalOverrideManager(impl: SandboxBaseUrlOverrideManager): BaseUrlLocalOverrideManager

    @Binds
    abstract fun bindsDebugNavigationComponent(impl: SandboxNavigationComponentImpl): SandboxNavigationComponent
}