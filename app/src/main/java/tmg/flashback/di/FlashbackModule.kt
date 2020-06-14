package tmg.flashback.di

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tmg.flashback.admin.lockout.LockoutViewModel
import tmg.flashback.home.HomeViewModel
import tmg.flashback.home.season.SeasonViewModel
import tmg.flashback.race.RaceViewModel
import tmg.flashback.settings.ConnectivityManager
import tmg.flashback.settings.NetworkConnectivityManager
import tmg.flashback.settings.SettingsViewModel

var flashbackModule = module {

    viewModel { HomeViewModel(get(), get(), get(), get(), get(), get(), get()) }
    viewModel { SeasonViewModel(get()) }

    viewModel { RaceViewModel(get(), get(), get()) }
    viewModel { SettingsViewModel(get(), get()) }
    viewModel { LockoutViewModel(get()) }

    single<ConnectivityManager> { NetworkConnectivityManager(get()) }
}