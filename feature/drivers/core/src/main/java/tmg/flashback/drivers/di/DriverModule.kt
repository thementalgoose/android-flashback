package tmg.flashback.drivers.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tmg.flashback.drivers.DriverNavigationComponentImpl
import tmg.flashback.drivers.contract.DriverNavigationComponent

@Module
@InstallIn(SingletonComponent::class)
internal class DriverModule {

    @Provides
    fun providesDriverNavigationComponent(impl: DriverNavigationComponentImpl): DriverNavigationComponent = impl
}