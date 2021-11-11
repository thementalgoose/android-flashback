package tmg.flashback.upnext.di

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tmg.flashback.upnext.controllers.UpNextController
import tmg.flashback.upnext.repository.UpNextRepository
import tmg.flashback.upnext.ui.dashboard.UpNextViewModel
import tmg.flashback.upnext.ui.onboarding.OnboardingNotificationViewModel
import tmg.flashback.upnext.ui.settings.UpNextSettingsViewModel
import tmg.flashback.upnext.ui.settings.reminder.UpNextReminderViewModel

val upNextModule = module {

    viewModel { UpNextViewModel(get()) }
    viewModel { UpNextSettingsViewModel(get()) }
    viewModel { OnboardingNotificationViewModel(get()) }
    viewModel { UpNextReminderViewModel(get()) }

    single { UpNextController(get(), get(), get()) }
    single { UpNextRepository(get(), get()) }
}