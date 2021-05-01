package tmg.flashback.di

import org.koin.dsl.module
import tmg.analytics.di.analyticsModule
import tmg.crash_reporting.controllers.CrashController
import tmg.crash_reporting.di.crashReportingModule
import tmg.crash_reporting.managers.CrashManager
import tmg.flashback.core.controllers.*
import tmg.flashback.core.managers.*
import tmg.flashback.core.repositories.ConfigurationRepository
import tmg.flashback.core.repositories.CoreRepository
import tmg.flashback.device.controllers.DeviceController
import tmg.flashback.managers.navigation.FlashbackNavigationManager
import tmg.flashback.managers.configuration.FirebaseRemoteConfigManager
import tmg.flashback.repositories.SharedPreferenceRepository
import tmg.flashback.statistics.controllers.UserNotificationController
import tmg.notifications.di.notificationModule
import tmg.utilities.prefs.SharedPrefManager

private val coreModule = module {

    // TODO: Move to shared modules folder
    single<SharedPrefManager> { tmg.flashback.device.repository.SharedPreferenceRepository(get()) }

    // Managers
    single<NavigationManager> { FlashbackNavigationManager(get(), get(), get()) }

    // Controllers
    single { AppearanceController(get(), get()) }
    single { AppHintsController(get()) }
    single { UserNotificationController(get()) }
    single { DeviceController(get(), get()) }
    single { ConfigurationController(get(), get(), get()) }

    // Repositories
    // TODO: Look at removing this to it's own repository
    single<ConfigurationRepository> { FirebaseRemoteConfigManager(get()) }
    single<CoreRepository> { SharedPreferenceRepository(get()) }
}

val coreModules = listOf(
    analyticsModule,
    crashReportingModule,
    notificationModule,
    coreModule
)