package tmg.flashback.di

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tmg.flashback.FlashbackStartup
import tmg.flashback.controllers.*
import tmg.flashback.core.managers.ConfigurationManager
import tmg.flashback.managers.appshortcuts.AndroidAppShortcutManager
import tmg.flashback.managers.appshortcuts.AppShortcutManager
import tmg.flashback.managers.notifications.FirebasePushNotificationManager
import tmg.flashback.managers.notifications.PushNotificationManager
import tmg.flashback.managers.configuration.FirebaseRemoteConfigManager
import tmg.flashback.repositories.SharedPreferenceRepository
import tmg.flashback.rss.controllers.RSSFeedController
import tmg.flashback.rss.network.RSS
import tmg.flashback.rss.prefs.RSSRepository
import tmg.flashback.rss.repo.RssAPI
import tmg.flashback.rss.ui.RSSViewModel
import tmg.flashback.rss.ui.configure.RSSConfigureViewModel
import tmg.flashback.rss.ui.settings.RSSSettingsViewModel
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

val appModule = module {

    // Managers
    single<AppShortcutManager> { AndroidAppShortcutManager(get()) }
    single<ConfigurationManager> { FirebaseRemoteConfigManager(get()) }
    single<PushNotificationManager> { FirebasePushNotificationManager(get(), get()) }

    // Controllers
    single { FeatureController(get()) }
    single { NotificationController(get(), get()) }
    single { RaceController(get()) }
    single { ReleaseNotesController(get(), get()) }
    single { SeasonController(get(), get()) }
    single { UpNextController(get()) }

    // Repositories

    // UI
    // Startup
    single { FlashbackStartup(get(), get(), get(), get(), get()) }
    // Splash
    viewModel { SplashViewModel(get(), get(), get()) }
    // Dashboard
    viewModel { DashboardViewModel(get(), get(), get(), get(), get()) }
    viewModel { SeasonViewModel(get(), get(), get(), get(), get(), get(), get(), get()) }
    viewModel { ListViewModel(get(), get(), get()) }
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
    viewModel { SettingsViewModel(get(), get(), get(), get(), get(), get(), get(), get()) }
    viewModel { tmg.flashback.ui.admin.LockoutViewModel(get(), get()) }
    viewModel { PrivacyPolicyViewModel() }
}

val appRssModule = module {

    // API
    single<RssAPI> { RSS(get(), get()) }

    // Managers

    // Controllers
    single { RSSFeedController(get()) }

    // Repositories
    single<RSSRepository> { SharedPreferenceRepository(get()) }

    // UI
    viewModel { RSSViewModel(get(), get(), get()) }
    viewModel { RSSSettingsViewModel(get()) }
    viewModel { RSSConfigureViewModel(get(), get()) }
}