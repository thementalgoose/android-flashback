package tmg.analytics.di

import org.koin.dsl.module
import tmg.analytics.controllers.AnalyticsController
import tmg.analytics.managers.AnalyticsManager
import tmg.analytics.managers.FirebaseAnalyticsManager

/**
 * Koin module to specify dependencies
 */
val analyticsModule = module {
    single<AnalyticsManager> { FirebaseAnalyticsManager(get()) }
    single { AnalyticsController(get(), get()) }
}