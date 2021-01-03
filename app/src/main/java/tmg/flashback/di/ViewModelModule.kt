package tmg.flashback.di

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tmg.flashback.SplashViewModel
import tmg.flashback.admin.lockout.LockoutViewModel
import tmg.flashback.circuit.CircuitInfoViewModel
import tmg.flashback.dashboard.DashboardViewModel
import tmg.flashback.dashboard.list.ListViewModel
import tmg.flashback.dashboard.search.SearchViewModel
import tmg.flashback.dashboard.season.SeasonViewModel
import tmg.flashback.overviews.constructor.ConstructorViewModel
import tmg.flashback.overviews.driver.DriverViewModel
import tmg.flashback.overviews.driver.season.DriverSeasonViewModel
import tmg.flashback.race.RaceViewModel
import tmg.flashback.settings.SettingsViewModel
import tmg.flashback.settings.privacy.PrivacyPolicyViewModel

val viewModelModule = module {

    // Splash
    viewModel { SplashViewModel(get(), get(), get()) }

    // Dashboard
    viewModel { DashboardViewModel(get(), get(), get()) }
    viewModel { SeasonViewModel(get(), get(), get(), get(), get(), get()) }
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
    viewModel { LockoutViewModel(get(), get()) }
    viewModel { PrivacyPolicyViewModel() }
}