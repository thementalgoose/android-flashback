package tmg.flashback.di

import org.koin.dsl.module
import tmg.flashback.di.toggle.FlashbackToggleRepository
import tmg.flashback.firebase.config.FirebaseRemoteConfigRepository
import tmg.flashback.firebase.repos.*
import tmg.flashback.repo.config.RemoteConfigRepository
import tmg.flashback.repo.config.ToggleRepository
import tmg.flashback.repo.db.DataRepository
import tmg.flashback.repo.db.stats.*

val firebaseModule = module {

    single<SeasonOverviewRepository> { SeasonOverviewFirestore(get()) }
    single<HistoryRepository> { HistoryFirestore(get()) }
    single<CircuitRepository> { CircuitFirestore(get()) }
    single<DataRepository> { DataFirestore(get()) }
    single<DriverRepository> { DriverFirestore(get()) }
    single<ConstructorRepository> { ConstructorFirestore(get()) }

    single<RemoteConfigRepository> { FirebaseRemoteConfigRepository(get()) }
    single<ToggleRepository> { FlashbackToggleRepository(get()) }
}
