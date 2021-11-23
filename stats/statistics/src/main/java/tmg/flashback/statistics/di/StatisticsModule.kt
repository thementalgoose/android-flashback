package tmg.flashback.statistics.di

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tmg.flashback.statistics.controllers.RaceController
import tmg.flashback.statistics.controllers.SearchController
import tmg.flashback.statistics.controllers.HomeController
import tmg.flashback.statistics.repo.di.repoModule
import tmg.flashback.statistics.repository.HomeRepository
import tmg.flashback.statistics.ui.circuit.CircuitInfoViewModel
import tmg.flashback.statistics.ui.dashboard.season.SeasonViewModel
import tmg.flashback.statistics.ui.overview.constructor.ConstructorViewModel
import tmg.flashback.statistics.ui.overview.driver.DriverViewModel
import tmg.flashback.statistics.ui.overview.driver.season.DriverSeasonViewModel
import tmg.flashback.statistics.ui.race.RaceViewModel
import tmg.flashback.statistics.ui.search.SearchViewModel
import tmg.flashback.statistics.ui.search.category.CategoryViewModel
import tmg.flashback.statistics.ui.settings.home.SettingsHomeViewModel
import tmg.flashback.statistics.ui.settings.statistics.SettingsStatisticsViewModel

val statisticsModule = repoModule + module {

    viewModel { CircuitInfoViewModel(get(), get()) }
    viewModel { SeasonViewModel(get(), get(), get(), get(), get(), get(), get(), get()) }
    viewModel { ConstructorViewModel(get(), get()) }
    viewModel { DriverViewModel(get(), get()) }
    viewModel { DriverSeasonViewModel(get(), get(), get()) }
    viewModel { RaceViewModel(get(), get(), get(), get()) }
    viewModel { SearchViewModel(get(), get(), get(), get()) }
    viewModel { CategoryViewModel() }

    viewModel { SettingsHomeViewModel(get()) }
    viewModel { SettingsStatisticsViewModel() }

    single { RaceController(get()) }
    single { HomeController(get()) }
    single { SearchController(get()) }

    // App
    single { HomeRepository(get(), get()) }
}