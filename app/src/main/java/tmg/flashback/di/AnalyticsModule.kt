package tmg.flashback.di

import org.koin.dsl.module
import tmg.flashback.analytics.AnalyticsUserProperties
import tmg.flashback.analytics.FirebaseAnalyticsUserProperties

val analyticsModule = module {

    single<AnalyticsUserProperties> { FirebaseAnalyticsUserProperties(get()) }
}