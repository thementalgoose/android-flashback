package tmg.analytics.di

import org.koin.dsl.module
import tmg.analytics.manager.AnalyticsManager
import tmg.analytics.services.AnalyticsService
import tmg.analytics.services.FirebaseAnalyticsService
import tmg.analytics.repository.AnalyticsRepository

val analyticsModule = module {
    single<AnalyticsService> { FirebaseAnalyticsService(get()) }
    single { AnalyticsRepository(get()) }
    single { AnalyticsManager(get(), get()) }
}