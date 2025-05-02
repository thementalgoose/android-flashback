package tmg.flashback.drivers.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal class DriverModule {

    @Provides
    fun providesDriverNavigationComponent(impl: DriverNavigationComponentImpl): DriverNavigationComponent = impl
}