package tmg.flashback.di

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tmg.flashback.BuildConfig
import tmg.flashback.admin.lockout.LockoutViewModel
import tmg.flashback.circuit.CircuitInfoViewModel
import tmg.flashback.overviews.constructor.ConstructorViewModel
import tmg.flashback.di.async.ScopeProvider
import tmg.flashback.di.async.ViewModelScopeProvider
import tmg.flashback.di.device.AppBuildConfigProvider
import tmg.flashback.di.device.BuildConfigProvider
import tmg.flashback.overviews.driver.DriverViewModel
import tmg.flashback.overviews.driver.season.DriverSeasonViewModel
import tmg.flashback.firebase.FirebaseCrashManager
import tmg.flashback.home.HomeViewModel
import tmg.flashback.home.season.SeasonViewModel
import tmg.flashback.news.NewsViewModel
import tmg.flashback.race.RaceViewModel
import tmg.flashback.repo.db.CrashManager
import tmg.flashback.settings.ConnectivityManager
import tmg.flashback.settings.NetworkConnectivityManager
import tmg.flashback.settings.SettingsViewModel
import tmg.flashback.settings.news.SettingsNewsViewModel

var flashbackModule = module {

    // Home
    viewModel { HomeViewModel(get(), get(), get(), get(), get(), get(), get()) }
    viewModel { SeasonViewModel(get(), get()) }

    // News
    viewModel { NewsViewModel(get(), get(), get(), get()) }

    // Circuit
    viewModel { CircuitInfoViewModel(get(), get(), get()) }

    // Race
    viewModel { RaceViewModel(get(), get(), get(), get()) }

    // Driver
    viewModel { DriverViewModel(get(), get(), get()) }
    viewModel { DriverSeasonViewModel(get(), get(), get(), get()) }

    // Constructor
    viewModel { ConstructorViewModel(get()) }

    // Settings
    viewModel { SettingsViewModel(get(), get()) }
    viewModel { SettingsNewsViewModel(get(), get()) }
    viewModel { LockoutViewModel(get(), get(), get()) }

    single<ConnectivityManager> { NetworkConnectivityManager(get()) }

    single<BuildConfigProvider> { AppBuildConfigProvider() }
    single<ScopeProvider> { ViewModelScopeProvider() }

    single<CrashManager> { FirebaseCrashManager(get(), BuildConfig.ENVIRONMENT != 1) }
}