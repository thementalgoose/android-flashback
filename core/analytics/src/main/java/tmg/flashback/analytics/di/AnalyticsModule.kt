package tmg.flashback.analytics.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tmg.flashback.analytics.services.AnalyticsService
import tmg.flashback.analytics.services.FirebaseAnalyticsService

@Module
@InstallIn(SingletonComponent::class)
internal abstract class AnalyticsModule {

    @Binds
    abstract fun bindsAnalyticsService(impl: FirebaseAnalyticsService): AnalyticsService
}