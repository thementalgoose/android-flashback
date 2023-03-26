package tmg.flashback.results.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tmg.flashback.results.contract.ResultsNavigationComponent
import tmg.flashback.results.contract.repository.NotificationsRepository
import tmg.flashback.results.ResultsNavigationComponentImpl
import tmg.flashback.results.repository.NotificationsRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
internal class ResultsModule {

    @Provides
    fun provideNotificationsRepository(impl: NotificationsRepositoryImpl): NotificationsRepository = impl

    @Provides
    fun provideResultsNavigationComponent(impl: ResultsNavigationComponentImpl): ResultsNavigationComponent = impl
}