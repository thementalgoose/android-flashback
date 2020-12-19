package tmg.flashback.di

import org.koin.dsl.module
import tmg.flashback.di.device.AppBuildConfigManager
import tmg.flashback.di.device.BuildConfigManager
import tmg.flashback.firebase.config.FirebaseRemoteConfigRepository
import tmg.flashback.prefs.SharedPrefsRepository
import tmg.flashback.repo.config.RemoteConfigRepository
import tmg.flashback.repo.pref.PrefCustomisationRepository
import tmg.flashback.repo.pref.PrefDeviceRepository
import tmg.flashback.repo.pref.PrefNotificationRepository

val configurationModule = module {

    // Build Config
    single<BuildConfigManager> { AppBuildConfigManager() }

    // Remote Config
    single<RemoteConfigRepository> { FirebaseRemoteConfigRepository(get()) }

    // Shared Prefs
    single<PrefCustomisationRepository> { SharedPrefsRepository(get()) }
    single<PrefDeviceRepository> { SharedPrefsRepository(get()) }
    single<PrefNotificationRepository> { SharedPrefsRepository(get()) }
}