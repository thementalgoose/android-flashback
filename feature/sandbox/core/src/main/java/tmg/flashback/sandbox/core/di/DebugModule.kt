package tmg.flashback.sandbox.core.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tmg.flashback.sandbox.DebugNavigationComponent
import tmg.flashback.sandbox.core.DebugNavigationComponentImpl
import tmg.flashback.sandbox.core.manager.DebugBaseUrlOverrideManager
import tmg.flashback.sandbox.manager.BaseUrlLocalOverrideManager

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DebugModule {

    @Binds
    abstract fun bindsBaseUrlLocalOverrideManager(impl: DebugBaseUrlOverrideManager): BaseUrlLocalOverrideManager

    @Binds
    abstract fun bindsDebugNavigationComponent(impl: DebugNavigationComponentImpl): DebugNavigationComponent
}