package tmg.notifications.di

import org.koin.dsl.module
import tmg.notifications.controllers.NotificationController
import tmg.notifications.managers.FirebaseRemoteNotificationManager
import tmg.notifications.managers.RemoteNotificationManager
import tmg.notifications.managers.SystemAlarmManager
import tmg.notifications.managers.SystemNotificationManager
import tmg.notifications.repository.NotificationRepository

val notificationModule = module {

    single<RemoteNotificationManager> { FirebaseRemoteNotificationManager(get()) }
    single { NotificationController(get(), get(), get(), get()) }
    single { NotificationRepository(get()) }

    single { SystemNotificationManager(get(), get(), get()) }
    single { SystemAlarmManager(get(), get()) }
}