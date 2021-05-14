package tmg.flashback.statistics.di

import org.koin.android.experimental.dsl.viewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tmg.flashback.data.db.DataRepository
import tmg.flashback.data.db.stats.*
import tmg.flashback.firebase.repos.*
import tmg.flashback.statistics.controllers.RaceController
import tmg.flashback.statistics.controllers.SeasonController
import tmg.flashback.statistics.controllers.UserNotificationController
import tmg.flashback.statistics.repository.StatisticsRepository
import tmg.flashback.statistics.ui.admin.maintenance.MaintenanceViewModel
import tmg.flashback.statistics.ui.circuit.CircuitInfoViewModel
import tmg.flashback.statistics.ui.dashboard.season.SeasonViewModel
import tmg.flashback.statistics.ui.overview.constructor.ConstructorViewModel
import tmg.flashback.statistics.ui.overview.driver.DriverViewModel
import tmg.flashback.statistics.ui.overview.driver.season.DriverSeasonViewModel
import tmg.flashback.statistics.ui.race.RaceViewModel

val statisticsModule = module {

    viewModel { MaintenanceViewModel(get(), get()) }
    viewModel { CircuitInfoViewModel(get(), get()) }
    viewModel { SeasonViewModel(get(), get(), get(), get(), get(), get(), get(), get()) }
    viewModel { ConstructorViewModel(get(), get()) }
    viewModel { DriverViewModel(get(), get()) }
    viewModel { DriverSeasonViewModel(get(), get(), get()) }
    viewModel { RaceViewModel(get(), get(), get(), get(), get()) }

    single { RaceController(get()) }
    single { SeasonController(get(), get()) }
    single { UserNotificationController(get()) }

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