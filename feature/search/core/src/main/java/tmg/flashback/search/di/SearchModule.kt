package tmg.flashback.search.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tmg.flashback.search.contract.usecases.SearchAppShortcutUseCase
import tmg.flashback.search.usecases.SearchAppShortcutUseCaseImpl

@Module
@InstallIn(SingletonComponent::class)
internal class SearchModule {

    @Provides
    fun provideSearchAppShortcutUseCase(impl: SearchAppShortcutUseCaseImpl): SearchAppShortcutUseCase = impl
}