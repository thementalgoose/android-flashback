package tmg.notifications.di

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tmg.notifications.controllers.NotificationController
import tmg.notifications.managers.FirebasePushNotificationManager
import tmg.notifications.managers.PushNotificationManager
import tmg.notifications.repository.NotificationRepository
import tmg.notifications.ui.settings.SettingsNotificationViewModel

val notificationModule = module {

    viewModel { SettingsNotificationViewModel(get()) }

    single<PushNotificationManager> { FirebasePushNotificationManager(get()) }
    single { NotificationController(get(), get()) }
    single { NotificationRepository(get()) }
}