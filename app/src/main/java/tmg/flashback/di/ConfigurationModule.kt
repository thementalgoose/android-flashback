package tmg.flashback.di

import org.koin.dsl.module
import tmg.flashback.di.device.AppBuildConfigManager
import tmg.flashback.di.device.BuildConfigManager
import tmg.flashback.firebase.config.FirebaseRemoteConfigRepository
import tmg.flashback.prefs.SharedPrefsRepository
import tmg.flashback.repo.config.RemoteConfigRepository
import tmg.flashback.repo.pref.UserRepository
import tmg.flashback.repo.pref.DeviceRepository
import tmg.flashback.repo.pref.NotificationRepository

val configurationModule = module {

    // Build Config
    single<BuildConfigManager> { AppBuildConfigManager() }

    // Remote Config
    single<RemoteConfigRepository> { FirebaseRemoteConfigRepository(get()) }

    // Shared Prefs
    single<UserRepository> { SharedPrefsRepository(get()) }
    single<DeviceRepository> { SharedPrefsRepository(get()) }
    single<NotificationRepository> { SharedPrefsRepository(get()) }
}