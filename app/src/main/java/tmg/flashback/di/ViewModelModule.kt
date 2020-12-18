package tmg.flashback.di

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tmg.flashback.SplashViewModel
import tmg.flashback.admin.lockout.LockoutViewModel
import tmg.flashback.circuit.CircuitInfoViewModel
import tmg.flashback.home.HomeViewModel
import tmg.flashback.home.season.SeasonViewModel
import tmg.flashback.overviews.constructor.ConstructorViewModel
import tmg.flashback.overviews.driver.DriverViewModel
import tmg.flashback.overviews.driver.season.DriverSeasonViewModel
import tmg.flashback.race.RaceViewModel
import tmg.flashback.settings.SettingsViewModel
import tmg.flashback.settings.privacy.PrivacyPolicyViewModel

val viewModelModule = module {

    // Splash
    viewModel { SplashViewModel(get(), get(), get()) }

    // Home
    viewModel { HomeViewModel(get(), get(), get(), get(), get(), get(), get(), get()) }
    viewModel { SeasonViewModel(get()) }

    // Circuit
    viewModel { CircuitInfoViewModel(get(), get()) }

    // Race
    viewModel { RaceViewModel(get(), get(), get()) }

    // Driver
    viewModel { DriverViewModel(get(), get()) }
    viewModel { DriverSeasonViewModel(get(), get(), get()) }

    // Constructor
    viewModel { ConstructorViewModel(get(), get()) }

    // Settings
    viewModel { SettingsViewModel(get(), get(), get()) }
    viewModel { LockoutViewModel(get(), get()) }
    viewModel { PrivacyPolicyViewModel() }
}