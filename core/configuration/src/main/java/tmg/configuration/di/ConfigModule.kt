package tmg.configuration.di

import org.koin.dsl.module
import tmg.configuration.controllers.ConfigController
import tmg.configuration.firebase.FirebaseRemoteConfigManager
import tmg.configuration.managers.RemoteConfigManager
import tmg.configuration.repository.ConfigRepository

val configModule = module {
    single<RemoteConfigManager> { FirebaseRemoteConfigManager() }
    single { ConfigRepository(get()) }
    single { ConfigController(get()) }
}