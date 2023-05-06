package tmg.flashback.maintenance.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tmg.flashback.maintenance.MaintenanceNavigationComponentImpl
import tmg.flashback.maintenance.contract.MaintenanceNavigationComponent

@Module
@InstallIn(SingletonComponent::class)
class MaintenanceModule {

    @Provides
    fun provideMaintenanceNavigationComponent(impl: MaintenanceNavigationComponentImpl): MaintenanceNavigationComponent = impl
}