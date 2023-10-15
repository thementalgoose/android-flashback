package tmg.flashback.circuits.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tmg.flashback.circuits.CircuitNavigationComponentImpl
import tmg.flashback.circuits.contract.CircuitNavigationComponent

@Module
@InstallIn(SingletonComponent::class)
class CircuitModule {

    @Provides
    fun provideCircuitNavigationComponent(impl: CircuitNavigationComponentImpl): CircuitNavigationComponent = impl
}