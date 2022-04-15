package tmg.flashback.di

import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tmg.flashback.DebugController
import tmg.flashback.FlashbackStartup
import tmg.flashback.device.managers.BuildConfigManager
import tmg.flashback.managers.AppApplicationNavigationComponent
import tmg.flashback.managers.AppNetworkConfigManager
import tmg.flashback.managers.AppPreferencesManager
import tmg.flashback.managers.AppStyleManager
import tmg.flashback.managers.buildconfig.AppBuildConfigManager
import tmg.flashback.managers.widgets.AppWidgetManager
import tmg.flashback.managers.widgets.WidgetManager
import tmg.flashback.notifications.navigation.NotificationNavigationProvider
import tmg.flashback.prefs.manager.PreferenceManager
import tmg.flashback.repositories.NetworkConfigRepository
import tmg.flashback.statistics.network.NetworkConfigManager
import tmg.flashback.ui.dashboard.DashboardViewModel
import tmg.flashback.ui.HomeViewModel
import tmg.flashback.ui.dashboard.menu.MenuViewModel
import tmg.flashback.ui2.dashboard.list.ListViewModel
import tmg.flashback.ui.managers.StyleManager
import tmg.flashback.ui.navigation.ApplicationNavigationComponent
import tmg.flashback.ui2.settings.SettingsAllViewModel
import tmg.flashback.ui.sync.SyncViewModel

val appModule = module {

    viewModel { SettingsAllViewModel(get(), get()) }
    viewModel { tmg.flashback.ui2.dashboard.DashboardViewModel(androidContext(), get(), get(), get(), get(), get()) }
    viewModel { ListViewModel(get(), get(), get(), get(), get(), get(), get(), get(), get()) }
    viewModel { HomeViewModel(get(), get(), get(), get(), get(), get(), get(), get()) }
    viewModel { SyncViewModel(get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get()) }

    viewModel { DashboardViewModel(get(), get(), get(), get(), get(), get(), get()) }
    viewModel { MenuViewModel(get(), get(), get(), get(), get(), get()) }

    single { FlashbackStartup(get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get()) }

    single { DebugController(get()) }

    single<PreferenceManager> { AppPreferencesManager(get()) }
    single<StyleManager> { AppStyleManager(get(), get()) }

    single<ApplicationNavigationComponent> { AppApplicationNavigationComponent(get(), get(), get(), get(), get()) }
    single<NotificationNavigationProvider> { AppApplicationNavigationComponent(get(), get(), get(), get(), get()) }

    single<BuildConfigManager> { AppBuildConfigManager() }
    single<WidgetManager> { AppWidgetManager(get()) }

    single { NetworkConfigRepository(get()) }
    single<NetworkConfigManager> { AppNetworkConfigManager(get()) }
}