package tmg.flashback.di

import org.koin.dsl.module
import tmg.flashback.controllers.*
import tmg.flashback.managers.sharedprefs.SharedPreferenceManager
import tmg.flashback.repo.config.RemoteConfigRepository
import tmg.flashback.repo.pref.UserRepository
import tmg.flashback.repo.pref.DeviceRepository

val controllerModule = module {

    // Shared Prefs
    single<UserRepository> { SharedPreferenceManager(get()) }
    single<DeviceRepository> { SharedPreferenceManager(get()) }

    // Controllers
    single { AppearanceController(get(), get()) }
    single { AppHintsController(get()) }
    single { CrashController(get(), get()) }
    single { DeviceController(get()) }
    single { FeatureController(get()) }
    single { NotificationController(get(), get()) }
    single { RaceController(get()) }
    single { ReleaseNotesController(get(), get()) }
    single { SeasonController(get(), get()) }
    single { UpNextController(get()) }
}