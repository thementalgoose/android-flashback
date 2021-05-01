package tmg.notifications.di

import org.koin.dsl.module
import tmg.notifications.controllers.NotificationController
import tmg.notifications.managers.FirebasePushNotificationManager
import tmg.notifications.managers.PushNotificationManager
import tmg.notifications.repository.NotificationRepository

val notificationModule = module {
    single<PushNotificationManager> { FirebasePushNotificationManager(get()) }
    single { NotificationController(get(), get()) }
    single { NotificationRepository(get()) }
}