package tmg.flashback.di

import org.koin.dsl.module
import tmg.flashback.managers.analytics.FirebaseUserPropertiesManager
import tmg.flashback.managers.analytics.UserPropertiesManager
import tmg.flashback.managers.appshortcuts.AndroidAppShortcutManager
import tmg.flashback.managers.appshortcuts.AppShortcutManager
import tmg.flashback.managers.buildconfig.AppBuildConfigManager
import tmg.flashback.managers.buildconfig.BuildConfigManager
import tmg.flashback.managers.networkconnectivity.AndroidNetworkConnectivityManager
import tmg.flashback.managers.networkconnectivity.NetworkConnectivityManager
import tmg.flashback.notifications.FirebasePushNotificationManager
import tmg.flashback.notifications.PushNotificationManager

val managerModule = module {

    single<AppShortcutManager> { AndroidAppShortcutManager(get()) }
    single<BuildConfigManager> { AppBuildConfigManager() }
    single<NetworkConnectivityManager> { AndroidNetworkConnectivityManager(get()) }
    single<PushNotificationManager> { FirebasePushNotificationManager(get(), get()) }
    single<UserPropertiesManager> { FirebaseUserPropertiesManager(get()) }
}