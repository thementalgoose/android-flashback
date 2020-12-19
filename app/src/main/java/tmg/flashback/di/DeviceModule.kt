package tmg.flashback.di

import org.koin.dsl.module
import tmg.flashback.BuildConfig
import tmg.flashback.di.network.AndroidConnectivityManager
import tmg.flashback.firebase.FirebaseCrashManager
import tmg.flashback.notifications.FirebasePushNotificationManager
import tmg.flashback.notifications.PushNotificationManager
import tmg.flashback.repo.NetworkConnectivityManager
import tmg.flashback.repo.db.CrashManager

val deviceModule = module {

    // Network connectivity
    single<NetworkConnectivityManager> { AndroidConnectivityManager(get()) }

    // Managers
    single<PushNotificationManager> { FirebasePushNotificationManager(get(), get()) }

    // Crash Reporting
    single<CrashManager> { FirebaseCrashManager(get(), BuildConfig.ENVIRONMENT != 1) }
}