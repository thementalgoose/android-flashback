package tmg.flashback.statistics.di

import org.koin.dsl.module
import tmg.flashback.data.db.DataRepository
import tmg.flashback.data.db.stats.*
import tmg.flashback.firebase.repos.*
import tmg.flashback.statistics.repository.StatisticsRepository

val statisticsModule = module {

    // App
    single { StatisticsRepository(get()) }

    // Firestore
    single<DataRepository> { DataFirestore(get()) }
    single<SeasonOverviewRepository> { SeasonOverviewFirestore(get()) }
    single<HistoryRepository> { HistoryFirestore(get()) }
    single<CircuitRepository> { CircuitFirestore(get()) }
    single<DriverRepository> { DriverFirestore(get()) }
    single<ConstructorRepository> { ConstructorFirestore(get()) }

}