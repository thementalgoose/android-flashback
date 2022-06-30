package tmg.flashback.notifications.di

import org.koin.dsl.module
import tmg.flashback.notifications.managers.FirebaseRemoteNotificationManager
import tmg.flashback.notifications.managers.RemoteNotificationManager
import tmg.flashback.notifications.managers.SystemAlarmManager
import tmg.flashback.notifications.managers.SystemNotificationManager
import tmg.flashback.notifications.repository.NotificationRepository
import tmg.flashback.notifications.usecases.LocalNotificationCancelUseCase
import tmg.flashback.notifications.usecases.LocalNotificationScheduleUseCase
import tmg.flashback.notifications.usecases.RemoteNotificationSubscribeUseCase
import tmg.flashback.notifications.usecases.RemoteNotificationUnsubscribeUseCase

val notificationModule = module {

    single<RemoteNotificationManager> { FirebaseRemoteNotificationManager() }
    single { NotificationRepository(get()) }

    single { SystemNotificationManager(get(), get(), get()) }
    single { SystemAlarmManager(get(), get()) }

    factory { RemoteNotificationSubscribeUseCase(get(), get()) }
    factory { RemoteNotificationUnsubscribeUseCase(get(), get()) }
    factory { LocalNotificationScheduleUseCase(get(), get()) }
    factory { LocalNotificationCancelUseCase(get(), get()) }
}