package tmg.flashback.sandbox.noop.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tmg.flashback.sandbox.SandboxNavigationComponent
import tmg.flashback.sandbox.manager.BaseUrlLocalOverrideManager
import tmg.flashback.sandbox.noop.SandboxNavigationComponentNoop
import tmg.flashback.sandbox.noop.manager.BaseUrlLocalOverrideManagerNoop
import tmg.flashback.sandbox.noop.usecases.GetSandboxMenuItemsUseCaseNoop
import tmg.flashback.sandbox.usecases.GetSandboxMenuItemsUseCase

@Module
@InstallIn(SingletonComponent::class)
internal abstract class SandboxModule {

    @Binds
    abstract fun bindsBaseUrlLocalOverrideManager(impl: BaseUrlLocalOverrideManagerNoop): BaseUrlLocalOverrideManager

    @Binds
    abstract fun bindsDebugNavigationComponent(impl: SandboxNavigationComponentNoop): SandboxNavigationComponent

    @Binds
    abstract fun bindsGetSandboxMenuItemsUseCase(impl: GetSandboxMenuItemsUseCaseNoop): GetSandboxMenuItemsUseCase
}