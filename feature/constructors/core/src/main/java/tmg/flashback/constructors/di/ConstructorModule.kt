package tmg.flashback.constructors.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal class ConstructorModule {

    @Provides
    fun provideConstructorsNavigationComponent(impl: ConstructorsNavigationComponentImpl): ConstructorsNavigationComponent = impl
}