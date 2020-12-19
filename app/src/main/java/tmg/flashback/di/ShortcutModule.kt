package tmg.flashback.di

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tmg.flashback.BuildConfig
import tmg.flashback.SplashViewModel
import tmg.flashback.admin.lockout.LockoutViewModel
import tmg.flashback.circuit.CircuitInfoViewModel
import tmg.flashback.overviews.constructor.ConstructorViewModel
import tmg.flashback.di.device.AppBuildConfigManager
import tmg.flashback.di.device.BuildConfigManager
import tmg.flashback.di.network.AndroidConnectivityManager
import tmg.flashback.overviews.driver.DriverViewModel
import tmg.flashback.overviews.driver.season.DriverSeasonViewModel
import tmg.flashback.firebase.FirebaseCrashManager
import tmg.flashback.home.HomeViewModel
import tmg.flashback.home.season.SeasonViewModel
import tmg.flashback.managers.AppShortcutManager
import tmg.flashback.managers.AndroidAppShortcutManager
import tmg.flashback.notifications.FirebasePushNotificationManager
import tmg.flashback.notifications.PushNotificationManager
import tmg.flashback.prefs.SharedPrefsRepository
import tmg.flashback.race.RaceViewModel
import tmg.flashback.repo.NetworkConnectivityManager
import tmg.flashback.repo.db.CrashManager
import tmg.flashback.repo.pref.PrefCustomisationRepository
import tmg.flashback.repo.pref.PrefDeviceRepository
import tmg.flashback.repo.pref.PrefNotificationRepository
import tmg.flashback.settings.SettingsViewModel
import tmg.flashback.settings.privacy.PrivacyPolicyViewModel

var shortcutModule = module {

    // App shortcuts
    single<AppShortcutManager> { AndroidAppShortcutManager(get()) }
}