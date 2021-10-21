package tmg.flashback.statistics.di

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tmg.flashback.data.db.DataRepository
import tmg.flashback.data.db.stats.*
import tmg.flashback.firebase.mappers.AppLockoutMapper
import tmg.flashback.firebase.mappers.CircuitMapper
import tmg.flashback.firebase.mappers.ConstructorMapper
import tmg.flashback.firebase.mappers.DriverMapper
import tmg.flashback.firebase.mappers.HistoryMapper
import tmg.flashback.firebase.mappers.LocationMapper
import tmg.flashback.firebase.mappers.SearchMapper
import tmg.flashback.firebase.mappers.SeasonOverviewMapper
import tmg.flashback.firebase.mappers.seasonoverview.SeasonOverviewConstructorMapper
import tmg.flashback.firebase.mappers.seasonoverview.SeasonOverviewDriverMapper
import tmg.flashback.firebase.mappers.seasonoverview.SeasonOverviewRaceMapper
import tmg.flashback.firebase.mappers.seasonoverview.SeasonOverviewStandingsMapper
import tmg.flashback.firebase.repos.*
import tmg.flashback.formula1.constants.Formula1
import tmg.flashback.statistics.controllers.RaceController
import tmg.flashback.statistics.controllers.SearchController
import tmg.flashback.statistics.controllers.SeasonController
import tmg.flashback.statistics.repository.StatisticsRepository
import tmg.flashback.statistics.ui.admin.maintenance.MaintenanceViewModel
import tmg.flashback.statistics.ui.circuit.CircuitInfoViewModel
import tmg.flashback.statistics.ui.dashboard.season.SeasonViewModel
import tmg.flashback.statistics.ui.overview.constructor.ConstructorViewModel
import tmg.flashback.statistics.ui.overview.driver.DriverViewModel
import tmg.flashback.statistics.ui.overview.driver.season.DriverSeasonViewModel
import tmg.flashback.statistics.ui.race.RaceViewModel
import tmg.flashback.statistics.ui.search.SearchViewModel
import tmg.flashback.statistics.ui.search.category.CategoryViewModel
import tmg.flashback.statistics.ui.settings.statistics.SettingsStatisticsViewModel

val statisticsModule = module {

    viewModel { MaintenanceViewModel(get(), get()) }
    viewModel { CircuitInfoViewModel(get(), get(), get()) }
    viewModel { SeasonViewModel(get(), get(), get(), get(), get(), get()) }
    viewModel { ConstructorViewModel(get(), get()) }
    viewModel { DriverViewModel(get(), get()) }
    viewModel { DriverSeasonViewModel(get(), get(), get()) }
    viewModel { RaceViewModel(get(), get(), get(), get()) }
    viewModel { SearchViewModel(get()) }
    viewModel { CategoryViewModel() }

    viewModel { SettingsStatisticsViewModel(get(), get()) }

    single { RaceController(get()) }
    single { SeasonController(get()) }
    single { SearchController(get()) }

    // App
    single { StatisticsRepository(get(), get()) }

    // Firestore Mappers
    single { AppLockoutMapper() }
    single { CircuitMapper(get(), get()) }
    single { HistoryMapper(Formula1.allDataUpToo, get()) }
    single { DriverMapper() }
    single { ConstructorMapper() }
    single { SearchMapper(get(), get()) }
    single { LocationMapper() }
    single { SeasonOverviewMapper(get(), get(), get(), get()) }
    single { SeasonOverviewConstructorMapper() }
    single { SeasonOverviewDriverMapper(get()) }
    single { SeasonOverviewRaceMapper(get()) }
    single { SeasonOverviewStandingsMapper(get(), get(), get()) }

    // Firestore
    single<DataRepository> { DataFirestore(get(), get()) }
    single<SeasonOverviewRepository> { SeasonOverviewFirestore(get(), get()) }
    single<HistoryRepository> { HistoryFirestore(get(), get()) }
    single<CircuitRepository> { CircuitFirestore(get(), get()) }
    single<DriverRepository> { DriverFirestore(get(), get()) }
    single<ConstructorRepository> { ConstructorFirestore(get(), get()) }
    single<SearchRepository> { SearchFirestore(get(), get(), get()) }

}