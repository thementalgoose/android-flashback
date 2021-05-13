package tmg.core.analytics.di

import org.koin.dsl.module
import tmg.core.analytics.manager.AnalyticsManager
import tmg.core.analytics.services.AnalyticsService
import tmg.core.analytics.services.FirebaseAnalyticsService
import tmg.core.analytics.repository.AnalyticsRepository

val analyticsModule = module {
    single<AnalyticsService> { FirebaseAnalyticsService(get()) }
    single { AnalyticsRepository(get()) }
    single { AnalyticsManager(get(), get()) }
}