package tmg.flashback.di

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tmg.flashback.ui.SplashViewModel
import tmg.flashback.ui.circuit.CircuitInfoViewModel
import tmg.flashback.ui.dashboard.DashboardViewModel
import tmg.flashback.ui.dashboard.list.ListViewModel
import tmg.flashback.ui.dashboard.search.SearchViewModel
import tmg.flashback.ui.dashboard.season.SeasonViewModel
import tmg.flashback.ui.overviews.constructor.ConstructorViewModel
import tmg.flashback.ui.overviews.driver.DriverViewModel
import tmg.flashback.ui.overviews.driver.season.DriverSeasonViewModel
import tmg.flashback.ui.race.RaceViewModel
import tmg.flashback.ui.settings.SettingsViewModel
import tmg.flashback.ui.settings.privacy.PrivacyPolicyViewModel

val viewModelModule = module {

    // Splash
    viewModel { SplashViewModel(get(), get(), get()) }

    // Dashboard
    viewModel { DashboardViewModel(get(), get(), get(), get()) }
    viewModel { SeasonViewModel(get(), get(), get(), get(), get(), get(), get(), get()) }
    viewModel { ListViewModel(get(), get()) }
    viewModel { SearchViewModel() }

    // Circuit
    viewModel { CircuitInfoViewModel(get(), get()) }

    // Race
    viewModel { RaceViewModel(get(), get(), get(), get(), get()) }

    // Driver
    viewModel { DriverViewModel(get(), get()) }
    viewModel { DriverSeasonViewModel(get(), get(), get()) }

    // Constructor
    viewModel { ConstructorViewModel(get(), get()) }

    // Settings
    viewModel { SettingsViewModel(get(), get(), get(), get(), get(), get(), get()) }
    viewModel { tmg.flashback.ui.admin.LockoutViewModel(get(), get()) }
    viewModel { PrivacyPolicyViewModel() }
}