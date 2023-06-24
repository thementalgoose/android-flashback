package tmg.flashback.googleanalytics.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tmg.flashback.googleanalytics.services.FireabseAnalyticsService
import tmg.flashback.googleanalytics.services.FirebaseFireabseAnalyticsServiceImpl

@Module
@InstallIn(SingletonComponent::class)
internal abstract class AnalyticsModule {

    @Binds
    abstract fun bindsAnalyticsService(impl: FirebaseFireabseAnalyticsServiceImpl): FireabseAnalyticsService
}