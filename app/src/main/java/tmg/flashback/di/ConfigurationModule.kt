package tmg.flashback.di

import org.koin.dsl.module
import tmg.flashback.controllers.*
import tmg.flashback.managers.buildconfig.AppBuildConfigManager
import tmg.flashback.managers.buildconfig.BuildConfigManager
import tmg.flashback.firebase.config.FirebaseRemoteConfigRepository
import tmg.flashback.managers.sharedprefs.SharedPreferenceManager
import tmg.flashback.repo.config.RemoteConfigRepository
import tmg.flashback.repo.pref.UserRepository
import tmg.flashback.repo.pref.DeviceRepository

val configurationModule = module {

    // Build Config
    single<BuildConfigManager> { AppBuildConfigManager() }

    // Remote Config
    single<RemoteConfigRepository> { FirebaseRemoteConfigRepository(get()) }

    // Shared Prefs
    single<UserRepository> { SharedPreferenceManager(get()) }
    single<DeviceRepository> { SharedPreferenceManager(get()) }

    // Controllers
    single { AppearanceController(get(), get()) }
    single { AppHintsController(get()) }
    single { DeviceController(get()) }
    single { FeatureController(get()) }
    single { NotificationController(get()) }
    single { RaceController(get()) }
    single { ReleaseNotesController(get()) }
    single { SeasonController(get(), get()) }
    single { UpNextController(get()) }
}