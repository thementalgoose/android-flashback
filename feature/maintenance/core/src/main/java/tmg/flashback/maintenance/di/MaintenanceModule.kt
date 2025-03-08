package tmg.flashback.maintenance.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tmg.flashback.maintenance.MaintenanceNavigationComponentImpl
import tmg.flashback.maintenance.contract.MaintenanceNavigationComponent
import tmg.flashback.maintenance.contract.usecases.ShouldForceUpgradeUseCase
import tmg.flashback.maintenance.contract.usecases.ShouldSoftUpgradeUseCase
import tmg.flashback.maintenance.usecases.ShouldForceUpgradeUseCaseImpl
import tmg.flashback.maintenance.usecases.ShouldSoftUpgradeUseCaseImpl

@Module
@InstallIn(SingletonComponent::class)
internal class MaintenanceModule {

    @Provides
    fun provideMaintenanceNavigationComponent(impl: MaintenanceNavigationComponentImpl): MaintenanceNavigationComponent = impl

    @Provides
    fun provideShouldForceUpgradeUseCase(impl: ShouldForceUpgradeUseCaseImpl): ShouldForceUpgradeUseCase = impl

    @Provides
    fun provideShouldSoftUpgradeUseCase(impl: ShouldSoftUpgradeUseCaseImpl): ShouldSoftUpgradeUseCase = impl
}