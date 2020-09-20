package tmg.flashback.di

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tmg.flashback.admin.lockout.LockoutViewModel
import tmg.flashback.circuit.CircuitInfoViewModel
import tmg.flashback.driver.DriverViewModel
import tmg.flashback.driver.overview.DriverOverviewViewModel
import tmg.flashback.driver.season.DriverSeasonViewModel
import tmg.flashback.home.HomeViewModel
import tmg.flashback.home.season.SeasonViewModel
import tmg.flashback.news.NewsViewModel
import tmg.flashback.race.RaceViewModel
import tmg.flashback.settings.ConnectivityManager
import tmg.flashback.settings.NetworkConnectivityManager
import tmg.flashback.settings.SettingsViewModel
import tmg.flashback.settings.news.SettingsNewsViewModel
import tmg.flashback.di.async.ScopeProvider
import tmg.flashback.di.async.ViewModelScopeProvider
import tmg.flashback.di.device.AppBuildConfigProvider
import tmg.flashback.di.device.BuildConfigProvider

var flashbackModule = module {

    viewModel { HomeViewModel(get(), get(), get(), get(), get(), get(), get()) }
    viewModel { SeasonViewModel(get(), get()) }
    viewModel { NewsViewModel(get(), get(), get(), get()) }

    viewModel { CircuitInfoViewModel(get(), get(), get()) }

    viewModel { RaceViewModel(get(), get(), get(), get()) }

    viewModel { DriverSeasonViewModel(get(), get(), get()) }
    viewModel { DriverViewModel(get(), get()) }
    viewModel { DriverOverviewViewModel(get(), get(), get()) }

    viewModel { SettingsViewModel(get(), get()) }
    viewModel { SettingsNewsViewModel(get(), get()) }
    viewModel { LockoutViewModel(get(), get(), get()) }

    single<ConnectivityManager> { NetworkConnectivityManager(get()) }

    single<BuildConfigProvider> { AppBuildConfigProvider() }
    single<ScopeProvider> { ViewModelScopeProvider() }
}