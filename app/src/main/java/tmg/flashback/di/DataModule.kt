package tmg.flashback.di

import org.koin.dsl.module
import tmg.flashback.data.db.DataRepository
import tmg.flashback.data.db.stats.*
import tmg.flashback.data.repositories.AppRepository
import tmg.flashback.firebase.FirestoreCrashManager
import tmg.flashback.firebase.repos.*
import tmg.flashback.managers.crash.FirebaseCrashManager
import tmg.flashback.repositories.SharedPreferenceRepository

val dataModule = module {

    // App
    single<AppRepository> { SharedPreferenceRepository(get()) }

    // Firestore
    single<FirestoreCrashManager> { FirebaseCrashManager() }
    single<DataRepository> { DataFirestore(get()) }
    single<SeasonOverviewRepository> { SeasonOverviewFirestore(get()) }
    single<HistoryRepository> { HistoryFirestore(get()) }
    single<CircuitRepository> { CircuitFirestore(get()) }
    single<DriverRepository> { DriverFirestore(get()) }
    single<ConstructorRepository> { ConstructorFirestore(get()) }
}