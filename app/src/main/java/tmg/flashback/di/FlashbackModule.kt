package tmg.flashback.di

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tmg.flashback.BuildConfig
import tmg.flashback.admin.lockout.LockoutViewModel
import tmg.flashback.circuit.CircuitInfoViewModel
import tmg.flashback.overviews.constructor.ConstructorViewModel
import tmg.flashback.di.async.ViewModelScopeProvider
import tmg.flashback.di.device.AppBuildConfigProvider
import tmg.flashback.di.device.BuildConfigProvider
import tmg.flashback.di.network.AndroidConnectivityManager
import tmg.flashback.overviews.driver.DriverViewModel
import tmg.flashback.overviews.driver.season.DriverSeasonViewModel
import tmg.flashback.firebase.FirebaseCrashManager
import tmg.flashback.home.HomeViewModel
import tmg.flashback.home.season.SeasonViewModel
import tmg.flashback.notifications.FirebasePushNotificationManager
import tmg.flashback.notifications.PushNotificationManager
import tmg.flashback.prefs.SharedPrefsDB
import tmg.flashback.race.RaceViewModel
import tmg.flashback.repo.NetworkConnectivityManager
import tmg.flashback.repo.ScopeProvider
import tmg.flashback.repo.db.CrashManager
import tmg.flashback.repo.pref.PrefsDB
import tmg.flashback.settings.SettingsViewModel
import tmg.flashback.settings.privacy.PrivacyPolicyViewModel

var flashbackModule = module {

    // Home
    viewModel { HomeViewModel(get(), get(), get(), get(), get(), get(), get()) }
    viewModel { SeasonViewModel(get(), get()) }

    // Circuit
    viewModel { CircuitInfoViewModel(get(), get(), get()) }

    // Race
    viewModel { RaceViewModel(get(), get(), get(), get()) }

    // Driver
    viewModel { DriverViewModel(get(), get(), get()) }
    viewModel { DriverSeasonViewModel(get(), get(), get(), get()) }

    // Constructor
    viewModel { ConstructorViewModel(get(), get(), get()) }

    // Settings
    viewModel { SettingsViewModel(get(), get(), get()) }
    viewModel { LockoutViewModel(get(), get(), get()) }
    viewModel { PrivacyPolicyViewModel(get()) }

    // Network connectivity
    single<NetworkConnectivityManager> { AndroidConnectivityManager(get()) }

    // Push notifications
    single<PushNotificationManager> { FirebasePushNotificationManager(get(), get()) }

    // Shared Prefs
    single<PrefsDB> { SharedPrefsDB(get()) }

    // Build Config
    single<BuildConfigProvider> { AppBuildConfigProvider() }

    // Scope
    single<ScopeProvider> { ViewModelScopeProvider() }

    // Crash Reporting
    single<CrashManager> { FirebaseCrashManager(get(), BuildConfig.ENVIRONMENT != 1) }
}