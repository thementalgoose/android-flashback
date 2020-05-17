package tmg.flashback.di

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tmg.flashback.admin.lockout.LockoutViewModel
import tmg.flashback.dashboard.DashboardViewModel
import tmg.flashback.dashboard.season.DashboardSeasonViewModel
import tmg.flashback.gallery.GalleryViewModel
import tmg.flashback.prefs.SharedPrefsDB
import tmg.flashback.race.RaceViewModel
import tmg.flashback.repo.db.*
import tmg.flashback.repo_firebase.CrashReporterFirebase
import tmg.flashback.repo_firebase.repos.CircuitFirestore
import tmg.flashback.repo_firebase.repos.DataFirestore
import tmg.flashback.repo_firebase.repos.HistoryFirestore
import tmg.flashback.repo_firebase.repos.SeasonOverviewFirestore
import tmg.flashback.settings.ConnectivityManager
import tmg.flashback.settings.NetworkConnectivityManager
import tmg.flashback.settings.SettingsViewModel

var flashbackModule = module {

    viewModel { DashboardSeasonViewModel(get()) }
    viewModel { DashboardViewModel(get(), get(), get()) }

    viewModel { RaceViewModel(get(), get(), get()) }

    viewModel { SettingsViewModel(get(), get()) }

    viewModel { GalleryViewModel() }
    viewModel { LockoutViewModel(get()) }

    single<SeasonOverviewDB> { SeasonOverviewFirestore(get()) }
    single<HistoryDB> { HistoryFirestore(get()) }
    single<PrefsDB> { SharedPrefsDB(get()) }
    single<CircuitDB> { CircuitFirestore() }
    single<CrashReporter> { CrashReporterFirebase(get(), get()) }
    single<DataDB> { DataFirestore(get()) }
    single<ConnectivityManager> { NetworkConnectivityManager(get()) }
}