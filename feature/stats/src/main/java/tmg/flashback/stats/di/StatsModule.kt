package tmg.flashback.stats.di

import androidx.work.WorkerParameters
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module
import tmg.flashback.stats.StatsNavigationComponent
import tmg.flashback.stats.repository.HomeRepository
import tmg.flashback.stats.repository.NotificationRepository
import tmg.flashback.stats.ui.circuits.CircuitViewModel
import tmg.flashback.stats.ui.constructors.overview.ConstructorOverviewViewModel
import tmg.flashback.stats.ui.dashboard.calendar.CalendarViewModel
import tmg.flashback.stats.ui.dashboard.constructors.ConstructorsStandingViewModel
import tmg.flashback.stats.ui.dashboard.drivers.DriversStandingViewModel
import tmg.flashback.stats.ui.drivers.overview.DriverOverviewViewModel
import tmg.flashback.stats.ui.drivers.season.DriverSeasonViewModel
import tmg.flashback.stats.ui.feature.notificationonboarding.NotificationOnboardingViewModel
import tmg.flashback.stats.ui.search.SearchViewModel
import tmg.flashback.stats.ui.settings.home.SettingsHomeViewModel
import tmg.flashback.stats.ui.settings.notifications.SettingsNotificationViewModel
import tmg.flashback.stats.ui.settings.notifications.reminder.UpNextReminderViewModel
import tmg.flashback.stats.ui.weekend.WeekendViewModel
import tmg.flashback.stats.ui.weekend.constructor.ConstructorViewModel
import tmg.flashback.stats.ui.weekend.qualifying.QualifyingViewModel
import tmg.flashback.stats.ui.weekend.race.RaceViewModel
import tmg.flashback.stats.ui.weekend.details.DetailsViewModel
import tmg.flashback.stats.ui.weekend.sprint.SprintViewModel
import tmg.flashback.stats.usecases.*
import tmg.flashback.stats.workmanager.ContentSyncJob
import tmg.flashback.stats.workmanager.ScheduleNotificationsJob

val statsModule = module {

    viewModel { CalendarViewModel(get(), get(), get(), get(), get()) }
    viewModel { ConstructorsStandingViewModel(get(), get(), get()) }
    viewModel { DriversStandingViewModel(get(), get(), get()) }

    viewModel { WeekendViewModel(get(), get()) }
    viewModel { DetailsViewModel(get(), get(), get()) }
    viewModel { QualifyingViewModel(get(), get()) }
    viewModel { SprintViewModel(get(), get()) }
    viewModel { RaceViewModel(get(), get()) }
    viewModel { ConstructorViewModel(get(), get(), get()) }

    viewModel { SearchViewModel(get(), get(), get(), get(), get(), get(), get()) }

    viewModel { CircuitViewModel(get(), get(), get(), get()) }

    viewModel { SettingsHomeViewModel(get(), get()) }
    viewModel { SettingsNotificationViewModel(get(), get(), get()) }
    viewModel { UpNextReminderViewModel(get()) }

    viewModel { DriverOverviewViewModel(get(), get(), get(), get()) }
    viewModel { DriverSeasonViewModel(get(), get(), get()) }
    viewModel { ConstructorOverviewViewModel(get(), get(), get()) }

    viewModel { NotificationOnboardingViewModel(get()) }

    single { StatsNavigationComponent(get()) }

    single { DefaultSeasonUseCase(get()) }
    single { FetchSeasonUseCase(get(), get(), get()) }
    single { ResubscribeNotificationsUseCase(get(), get(), get()) }

    single { HomeRepository(get(), get()) }
    single { NotificationRepository(get()) }

    single { ContentSyncJobScheduleUseCase(get()) }
    single { ScheduleNotificationsUseCase(get()) }

    worker { (worker: WorkerParameters) ->
        ContentSyncJob(
            defaultSeasonUseCase = get(),
            overviewRepository = get(),
            scheduleNotificationsUseCase = get(),
            context = androidContext(),
            params = worker
        )
    }
    worker { (worker: WorkerParameters) ->
        ScheduleNotificationsJob(
            scheduleRepository = get(),
            notificationRepository = get(),
            localNotificationCancelUseCase = get(),
            localNotificationScheduleUseCase = get(),
            notificationConfigRepository = get(),
            context = androidContext(),
            parameters = worker
        )
    }

}