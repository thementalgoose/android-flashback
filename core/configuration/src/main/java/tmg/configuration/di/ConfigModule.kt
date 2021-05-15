package tmg.configuration.di

import org.koin.dsl.module
import tmg.configuration.controllers.ConfigController
import tmg.configuration.firebase.FirebaseRemoteConfigService
import tmg.configuration.services.RemoteConfigService
import tmg.configuration.repository.ConfigRepository

val configModule = module {
    single<RemoteConfigService> { FirebaseRemoteConfigService() }
    single { ConfigRepository(get()) }
    single { ConfigController(get(), get()) }
}