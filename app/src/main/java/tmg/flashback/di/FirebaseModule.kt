package tmg.flashback.di

import org.koin.dsl.module
import tmg.flashback.firebase.CrashReporterFirebase
import tmg.flashback.firebase.repos.*
import tmg.flashback.prefs.SharedPrefsDB
import tmg.flashback.repo.db.CrashReporter
import tmg.flashback.repo.db.PrefsDB
import tmg.flashback.repo.db.stats.*

val firebaseModule = module {

    single<SeasonOverviewDB> { SeasonOverviewFirestore(get()) }
    single<HistoryDB> { HistoryFirestore(get()) }
    single<PrefsDB> { SharedPrefsDB(get()) }
    single<CrashReporter> { CrashReporterFirebase(get(), get()) }
    single<CircuitDB> { CircuitFirestore(get()) }
    single<DataDB> { DataFirestore(get()) }
    single<DriverDB> { DriverFirestore(get()) }
}