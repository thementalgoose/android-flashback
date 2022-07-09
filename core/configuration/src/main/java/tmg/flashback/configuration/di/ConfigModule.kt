package tmg.flashback.configuration.di

import androidx.work.WorkerParameters
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module
import tmg.flashback.configuration.firebase.FirebaseRemoteConfigService
import tmg.flashback.configuration.manager.ConfigManager
import tmg.flashback.configuration.repository.ConfigRepository
import tmg.flashback.configuration.services.RemoteConfigService
import tmg.flashback.configuration.usecases.ApplyConfigUseCase
import tmg.flashback.configuration.usecases.FetchConfigUseCase
import tmg.flashback.configuration.usecases.InitialiseConfigUseCase
import tmg.flashback.configuration.usecases.ResetConfigUseCase
import tmg.flashback.configuration.workmanager.ConfigSyncJob

val configModule = module {
    single<RemoteConfigService> { FirebaseRemoteConfigService() }
    single { ConfigRepository(get()) }
    single { ConfigManager(get(), get()) }

    factory { InitialiseConfigUseCase(get()) }
    factory { ResetConfigUseCase(get(), get()) }
    factory { ApplyConfigUseCase(get(), get()) }
    factory { FetchConfigUseCase(get(), get()) }

    //  https://github.com/InsertKoinIO/koin/issues/992
    worker { (worker: WorkerParameters) ->
        ConfigSyncJob(
            fetchConfigUseCase = get(),
            context = androidContext(),
            params = worker
        )
    }
}