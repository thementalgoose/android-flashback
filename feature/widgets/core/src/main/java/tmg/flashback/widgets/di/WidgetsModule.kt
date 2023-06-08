package tmg.flashback.widgets.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tmg.flashback.widgets.contract.usecases.HasWidgetsUseCase
import tmg.flashback.widgets.contract.usecases.UpdateWidgetsUseCase
import tmg.flashback.widgets.usecases.HasWidgetsUseCaseImpl
import tmg.flashback.widgets.usecases.UpdateWidgetsUseCaseImpl

@Module
@InstallIn(SingletonComponent::class)
internal class WidgetsModule {

    @Provides
    fun provideUpdateWidgetsUseCase(impl: UpdateWidgetsUseCaseImpl): UpdateWidgetsUseCase = impl

    @Provides
    fun provideHasWidgetsUseCase(impl: HasWidgetsUseCaseImpl): HasWidgetsUseCase = impl
}