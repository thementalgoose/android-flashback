package tmg.flashback.widgets.upnext.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tmg.flashback.widgets.upnext.contract.usecases.HasUpNextWidgetsUseCase
import tmg.flashback.widgets.upnext.usecases.HasUpNextUpNextWidgetsUseCaseImpl

@Module
@InstallIn(SingletonComponent::class)
internal class WidgetsModule {

    @Provides
    fun provideHasWidgetsUseCase(impl: HasUpNextUpNextWidgetsUseCaseImpl): HasUpNextWidgetsUseCase = impl
}