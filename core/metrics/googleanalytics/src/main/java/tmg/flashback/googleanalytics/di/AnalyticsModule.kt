package tmg.flashback.googleanalytics.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tmg.flashback.googleanalytics.services.FirebaseAnalyticsService
import tmg.flashback.googleanalytics.services.FirebaseAnalyticsServiceImpl

@Module
@InstallIn(SingletonComponent::class)
internal abstract class AnalyticsModule {

    @Binds
    abstract fun bindsAnalyticsService(impl: FirebaseAnalyticsServiceImpl): FirebaseAnalyticsService
}