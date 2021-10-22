package tmg.flashback.di

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tmg.flashback.device.managers.BuildConfigManager
import tmg.flashback.prefs.manager.PreferenceManager
import tmg.flashback.ui.managers.StyleManager
import tmg.flashback.ui.navigation.NavigationProvider
import tmg.flashback.DebugController
import tmg.flashback.FlashbackStartup
import tmg.flashback.managers.appshortcuts.AndroidAppShortcutManager
import tmg.flashback.managers.appshortcuts.AppShortcutManager
import tmg.flashback.managers.buildconfig.AppBuildConfigManager
import tmg.flashback.managers.widgets.AppWidgetManager
import tmg.flashback.managers.widgets.WidgetManager
import tmg.flashback.managers.AppPreferencesManager
import tmg.flashback.managers.AppNavigationProvider
import tmg.flashback.managers.AppStyleManager
import tmg.flashback.ui.SyncViewModel
import tmg.flashback.ui.dashboard.DashboardViewModel
import tmg.flashback.ui.dashboard.HomeViewModel
import tmg.flashback.ui.dashboard.list.ListViewModel
import tmg.flashback.ui.settings.SettingsAllViewModel
import tmg.notifications.navigation.NotificationNavigationProvider

val appModule = module {

    viewModel { SettingsAllViewModel(get()) }
    viewModel { DashboardViewModel(get(), get(), get(), get(), get(), get()) }
    viewModel { ListViewModel(get(), get(), get()) }
    viewModel { HomeViewModel(get(), get(), get(), get(), get()) }
    viewModel { SyncViewModel(get(), get(), get(), get(), get()) }

    single { FlashbackStartup(get(), get(), get(), get(), get(), get()) }

    single { DebugController() }

    single<PreferenceManager> { AppPreferencesManager(get()) }
    single<StyleManager> { AppStyleManager() }

    single<NavigationProvider> { AppNavigationProvider(get(), get(), get(), get()) }
    single<NotificationNavigationProvider> { AppNavigationProvider(get(), get(), get(), get()) }

    single<AppShortcutManager> { AndroidAppShortcutManager(get(), get()) }
    single<BuildConfigManager> { AppBuildConfigManager() }
    single<WidgetManager> { AppWidgetManager(get()) }
}