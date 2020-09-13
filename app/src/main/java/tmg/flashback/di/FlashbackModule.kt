package tmg.flashback.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tmg.flashback.admin.lockout.LockoutViewModel
import tmg.flashback.circuit.CircuitInfoViewModel
import tmg.flashback.driver.season.DriverSeasonViewModel
import tmg.flashback.env
import tmg.flashback.home.HomeViewModel
import tmg.flashback.home.season.SeasonViewModel
import tmg.flashback.news.NewsViewModel
import tmg.flashback.race.RaceViewModel
import tmg.flashback.settings.ConnectivityManager
import tmg.flashback.settings.NetworkConnectivityManager
import tmg.flashback.settings.SettingsViewModel
import tmg.flashback.settings.news.SettingsNewsViewModel

var flashbackModule = module {

    viewModel { HomeViewModel(get(), get(), get(), get(), get(), get()) }
    viewModel { SeasonViewModel(get()) }
    viewModel { NewsViewModel(get(), get(), get()) }

    viewModel { CircuitInfoViewModel(get(), get(), get()) }

    viewModel { RaceViewModel(get(), get(), get()) }

    viewModel { DriverSeasonViewModel(get(), get(), get()) }

    viewModel { SettingsViewModel(get()) }
    viewModel { SettingsNewsViewModel(get()) }
    viewModel { LockoutViewModel(get(), get()) }

    single<ConnectivityManager> { NetworkConnectivityManager(get()) }

    single<CoroutineScope?> { null }
}