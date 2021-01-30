package tmg.flashback.di

import org.koin.dsl.module
import tmg.flashback.firebase.crash.FirebaseCrashManagerImpl
import tmg.flashback.firebase.crash.FirebaseCrashManager
import tmg.flashback.firebase.repos.*
import tmg.flashback.managers.remoteconfig.FirebaseRemoteConfigManager
import tmg.flashback.managers.remoteconfig.RemoteConfigManager
import tmg.flashback.data.config.RemoteConfigRepository
import tmg.flashback.data.db.DataRepository
import tmg.flashback.data.db.stats.*

val firebaseModule = module {

    // Remote config
    single<RemoteConfigManager> { FirebaseRemoteConfigManager(get(), get()) }
    single<RemoteConfigRepository> { FirebaseRemoteConfigManager(get(), get()) }

    // Crashlytics
    single<FirebaseCrashManager> { FirebaseCrashManagerImpl() }

    // Firestore
    single<SeasonOverviewRepository> { SeasonOverviewFirestore(get()) }
    single<HistoryRepository> { HistoryFirestore(get()) }
    single<CircuitRepository> { CircuitFirestore(get()) }
    single<DataRepository> { DataFirestore(get()) }
    single<DriverRepository> { DriverFirestore(get()) }
    single<ConstructorRepository> { ConstructorFirestore(get()) }
}
