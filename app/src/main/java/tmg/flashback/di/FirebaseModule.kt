package tmg.flashback.di

import org.koin.dsl.module
import tmg.flashback.di.toggle.FlashbackToggleDB
import tmg.flashback.firebase.repos.*
import tmg.flashback.repo.toggle.ToggleRepository
import tmg.flashback.repo.db.stats.*

val firebaseModule = module {

    single<SeasonOverviewRepository> { SeasonOverviewFirestore(get()) }
    single<HistoryRepository> { HistoryFirestore(get()) }
    single<CircuitRepository> { CircuitFirestore(get()) }
    single<DataRepository> { DataFirestore(get()) }
    single<DriverRepository> { DriverFirestore(get()) }
    single<ConstructorRepository> { ConstructorFirestore(get()) }

    single<ToggleRepository> { FlashbackToggleDB(get()) }
}
