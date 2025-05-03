package tmg.flashback.season.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tmg.flashback.data.repo.NotificationsRepository

@Module
@InstallIn(SingletonComponent::class)
internal class SeasonModule {

    @Provides
    fun provideNotificationsRepository(impl: NotificationsRepository): NotificationsRepository = impl
}