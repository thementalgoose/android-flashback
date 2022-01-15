package tmg.flashback.analytics.di

import org.koin.dsl.module
import tmg.flashback.analytics.manager.AnalyticsManager
import tmg.flashback.analytics.repository.AnalyticsRepository
import tmg.flashback.analytics.services.AnalyticsService
import tmg.flashback.analytics.services.FirebaseAnalyticsService

val analyticsModule = module {
    single<AnalyticsService> { FirebaseAnalyticsService(get()) }
    single { AnalyticsRepository(get()) }
    single { AnalyticsManager(get(), get()) }
}