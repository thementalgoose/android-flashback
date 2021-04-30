package tmg.analytics.di

import org.koin.dsl.module
import tmg.analytics.controllers.AnalyticsController
import tmg.analytics.managers.AnalyticsManager
import tmg.analytics.managers.FirebaseAnalyticsManager
import tmg.analytics.repository.AnalyticsRepository

val analyticsModule = module {
    single<AnalyticsManager> { FirebaseAnalyticsManager(get()) }
    single { AnalyticsRepository(get()) }
    single { AnalyticsController(get(), get()) }
}