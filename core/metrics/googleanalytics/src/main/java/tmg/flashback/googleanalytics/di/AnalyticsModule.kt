package tmg.flashback.googleanalytics.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tmg.flashback.googleanalytics.services.AnalyticsService
import tmg.flashback.googleanalytics.services.FirebaseAnalyticsService

@Module
@InstallIn(SingletonComponent::class)
internal abstract class AnalyticsModule {

    @Binds
    abstract fun bindsAnalyticsService(impl: FirebaseAnalyticsService): AnalyticsService
}