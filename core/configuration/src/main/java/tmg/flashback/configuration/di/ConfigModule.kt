package tmg.flashback.configuration.di

import org.koin.dsl.module
import tmg.flashback.configuration.controllers.ConfigController
import tmg.flashback.configuration.firebase.FirebaseRemoteConfigService
import tmg.flashback.configuration.manager.ConfigManager
import tmg.flashback.configuration.repository.ConfigRepository
import tmg.flashback.configuration.services.RemoteConfigService
import tmg.flashback.configuration.usecases.ApplyConfigUseCase
import tmg.flashback.configuration.usecases.FetchConfigUseCase
import tmg.flashback.configuration.usecases.InitialiseConfigUseCase
import tmg.flashback.configuration.usecases.ResetConfigUseCase

val configModule = module {
    single<RemoteConfigService> { FirebaseRemoteConfigService() }
    single { ConfigRepository(get()) }
    single { ConfigController(get(), get()) }
    single { ConfigManager(get(), get()) }

    factory { InitialiseConfigUseCase(get()) }
    factory { ResetConfigUseCase(get(), get()) }
    factory { ApplyConfigUseCase(get(), get()) }
    factory { FetchConfigUseCase(get(), get()) }
}