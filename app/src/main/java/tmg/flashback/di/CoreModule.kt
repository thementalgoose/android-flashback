package tmg.flashback.di

import org.koin.dsl.module
import tmg.flashback.core.controllers.*
import tmg.flashback.core.managers.*
import tmg.flashback.core.repositories.ConfigurationRepository
import tmg.flashback.core.repositories.CoreRepository
import tmg.flashback.managers.analytics.FirebaseAnalyticsManager
import tmg.flashback.managers.navigation.FlashbackNavigationManager
import tmg.flashback.managers.buildconfig.AppBuildConfigManager
import tmg.flashback.managers.configuration.FirebaseRemoteConfigManager
import tmg.flashback.managers.crash.FirebaseCrashManager
import tmg.flashback.managers.networkconnectivity.AndroidNetworkConnectivityManager
import tmg.flashback.repositories.SharedPreferenceRepository

val coreModule = module {

    // Managers
    single<AnalyticsManager> { FirebaseAnalyticsManager(get()) }
    single<BuildConfigManager> { AppBuildConfigManager() }
    single<CrashManager> { FirebaseCrashManager() }
    single<NavigationManager> { FlashbackNavigationManager(get(), get()) }
    single<NetworkConnectivityManager> { AndroidNetworkConnectivityManager(get()) }

    // Controllers
    single { AnalyticsController(get(), get()) }
    single { AppearanceController(get(), get()) }
    single { AppHintsController(get()) }
    single { CrashController(get(), get()) }
    single { DeviceController(get(), get()) }
    single { ConfigurationController(get(), get(), get()) }

    // Repositories
    // TODO: Look at removing this to it's own repository
    single<ConfigurationRepository> { FirebaseRemoteConfigManager(get()) }
    single<CoreRepository> { SharedPreferenceRepository(get()) }
}