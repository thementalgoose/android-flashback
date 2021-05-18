package tmg.flashback.di

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tmg.core.device.managers.BuildConfigManager
import tmg.core.prefs.manager.PreferenceManager
import tmg.core.ui.managers.StyleManager
import tmg.core.ui.navigation.NavigationProvider
import tmg.flashback.FlashbackStartup
import tmg.flashback.managers.appshortcuts.AndroidAppShortcutManager
import tmg.flashback.managers.appshortcuts.AppShortcutManager
import tmg.flashback.managers.buildconfig.AppBuildConfigManager
import tmg.flashback.managers.widgets.AppWidgetManager
import tmg.flashback.managers.widgets.WidgetManager
import tmg.flashback.managers.AppPreferencesManager
import tmg.flashback.managers.AppNavigationProvider
import tmg.flashback.managers.AppStyleManager
import tmg.flashback.ui.SplashViewModel
import tmg.flashback.ui.dashboard.DashboardViewModel
import tmg.flashback.ui.dashboard.list.ListViewModel
import tmg.flashback.ui.settings.SettingsAllViewModel
import tmg.notifications.navigation.NotificationNavigationProvider

val appModule = module {

    viewModel { SplashViewModel(get(), get(), get(), get()) }
    viewModel { SettingsAllViewModel(get()) }
    viewModel { DashboardViewModel(get(), get(), get(), get(), get()) }
    viewModel { ListViewModel(get(), get(), get()) }

    single { FlashbackStartup(get(), get(), get(), get(), get(), get()) }

    single<PreferenceManager> { AppPreferencesManager(get()) }
    single<StyleManager> { AppStyleManager() }

    single<NavigationProvider> { AppNavigationProvider(get(), get(), get(), get()) }
    single<NotificationNavigationProvider> { AppNavigationProvider(get(), get(), get(), get()) }

    single<AppShortcutManager> { AndroidAppShortcutManager(get()) }
    single<BuildConfigManager> { AppBuildConfigManager() }
    single<WidgetManager> { AppWidgetManager(get()) }
}