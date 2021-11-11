package tmg.flashback.configuration.di

import org.koin.dsl.module
import tmg.flashback.configuration.controllers.ConfigController
import tmg.flashback.configuration.firebase.FirebaseRemoteConfigService
import tmg.flashback.configuration.manager.ConfigManager
import tmg.flashback.configuration.services.RemoteConfigService
import tmg.flashback.configuration.repository.ConfigRepository

val configModule = module {
    single<RemoteConfigService> { FirebaseRemoteConfigService() }
    single { ConfigRepository(get()) }
    single { ConfigController(get(), get()) }
    single { ConfigManager(get()) }
}