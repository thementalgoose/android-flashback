package tmg.flashback.di

import org.koin.dsl.module
import tmg.flashback.managers.analytics.UserPropertiesManager
import tmg.flashback.managers.analytics.FirebaseUserPropertiesManager

val analyticsModule = module {

    single<UserPropertiesManager> { FirebaseUserPropertiesManager(get()) }
}