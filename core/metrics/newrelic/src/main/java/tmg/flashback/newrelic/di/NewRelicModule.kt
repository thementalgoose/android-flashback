package tmg.flashback.newrelic.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tmg.flashback.newrelic.services.NewRelicService
import tmg.flashback.newrelic.services.NewRelicServiceImpl

@Module
@InstallIn(SingletonComponent::class)
internal class NewRelicModule {

    @Provides
    fun provideNewRelicService(impl: NewRelicServiceImpl): NewRelicService = impl
}