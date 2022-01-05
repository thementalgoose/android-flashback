package tmg.flashback.notifications.di

import org.koin.dsl.module
import tmg.flashback.notifications.controllers.NotificationController
import tmg.flashback.notifications.managers.FirebaseRemoteNotificationManager
import tmg.flashback.notifications.managers.RemoteNotificationManager
import tmg.flashback.notifications.managers.SystemAlarmManager
import tmg.flashback.notifications.managers.SystemNotificationManager
import tmg.flashback.notifications.repository.NotificationRepository

val notificationModule = module {

    single<RemoteNotificationManager> { FirebaseRemoteNotificationManager() }
    single { NotificationController(get(), get(), get(), get()) }
    single { NotificationRepository(get()) }

    single { SystemNotificationManager(get(), get(), get()) }
    single { SystemAlarmManager(get(), get()) }
}