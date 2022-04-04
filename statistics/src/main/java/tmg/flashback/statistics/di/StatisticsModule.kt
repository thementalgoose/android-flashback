package tmg.flashback.statistics.di

import androidx.work.WorkerParameters
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.workmanager.dsl.worker
import org.koin.core.qualifier.named
import org.koin.dsl.module
import tmg.flashback.statistics.controllers.HomeController
import tmg.flashback.statistics.controllers.ScheduleController
import tmg.flashback.statistics.repo.di.repoModule
import tmg.flashback.statistics.repository.HomeRepository
import tmg.flashback.statistics.repository.UpNextRepository
import tmg.flashback.statistics.ui.circuit.CircuitViewModel
import tmg.flashback.statistics.ui.dashboard.calendar.CalendarViewModel
import tmg.flashback.statistics.ui.dashboard.constructors.ConstructorsStandingViewModel
import tmg.flashback.statistics.ui.dashboard.drivers.DriversStandingViewModel
import tmg.flashback.statistics.ui.dashboard.events.EventListViewModel
import tmg.flashback.statistics.ui.dashboard.onboarding.OnboardingNotificationViewModel
import tmg.flashback.statistics.ui.dashboard.racepreview.RacePreviewViewModel
import tmg.flashback.statistics.ui.dashboard.season.SeasonViewModel
import tmg.flashback.statistics.ui.overview.constructor.stats.ConstructorViewModel
import tmg.flashback.statistics.ui.overview.driver.season.DriverSeasonViewModel
import tmg.flashback.statistics.ui.overview.driver.stats.DriverViewModel
import tmg.flashback.statistics.ui.race.RaceViewModel
import tmg.flashback.statistics.ui.search.SearchViewModel
import tmg.flashback.statistics.ui.search.category.CategoryViewModel
import tmg.flashback.statistics.ui.settings.home.SettingsHomeViewModel
import tmg.flashback.statistics.ui.settings.notifications.UpNextSettingsViewModel
import tmg.flashback.statistics.ui.settings.notifications.reminder.UpNextReminderViewModel
import tmg.flashback.statistics.ui.settings.statistics.SettingsStatisticsViewModel
import tmg.flashback.statistics.ui.weekend.WeekendViewModel
import tmg.flashback.statistics.ui.weekend.qualifying.QualifyingViewModel
import tmg.flashback.statistics.ui.weekend.schedule.ScheduleViewModel
import tmg.flashback.statistics.ui.weekend.sprint.SprintViewModel
import tmg.flashback.statistics.usecases.*
import tmg.flashback.statistics.workmanager.ContentSyncWorker
import tmg.flashback.statistics.workmanager.NotificationScheduleWorker
import tmg.flashback.statistics.workmanager.WorkerProvider

val statisticsModule = repoModule + module {

    viewModel { CircuitViewModel(get(), get()) }
    viewModel { SeasonViewModel(get(), get(), get(), get(), get(), get(), get(), get(), get(), get()) }
    viewModel { RacePreviewViewModel(get()) }
    viewModel { EventListViewModel(get()) }
    viewModel { ConstructorViewModel(get(), get()) }
    viewModel { DriverViewModel(get(), get()) }
    viewModel { DriverSeasonViewModel(get(), get(), get()) }
    viewModel { RaceViewModel(get(), get(), get(), get()) }
    viewModel { SearchViewModel(get(), get(), get(), get(), get()) }
    viewModel { CategoryViewModel() }

    viewModel { CalendarViewModel(get(), get(), get(), get()) }
    viewModel { DriversStandingViewModel(get(), get(), get()) }
    viewModel { ConstructorsStandingViewModel(get(), get()) }
    viewModel { WeekendViewModel(get()) }
    viewModel { ScheduleViewModel(get(), get()) }
    viewModel { SprintViewModel(get()) }
    viewModel { tmg.flashback.statistics.ui.weekend.race.RaceViewModel(get()) }
    viewModel { QualifyingViewModel(get()) }
    viewModel { tmg.flashback.statistics.ui.weekend.constructor.ConstructorViewModel(get()) }

    viewModel { UpNextSettingsViewModel(get()) }
    viewModel { OnboardingNotificationViewModel(get()) }
    viewModel { UpNextReminderViewModel(get()) }
    viewModel { SettingsHomeViewModel(get(), get()) }
    viewModel { SettingsStatisticsViewModel() }

    single { HomeController(get()) }
    single { ScheduleController(androidContext(), get(), get(), get(), get(), get()) }

    single { HomeRepository(get(), get()) }
    single { UpNextRepository(get()) }

    // Use Cases
    factory { SearchAppShortcutUseCase(get(), get()) }
    factory { DefaultSeasonUseCase(get()) }
    factory { FetchSeasonUseCase(get(), get(), get()) }

    // Worker
    //  https://github.com/InsertKoinIO/koin/issues/992
    worker { (worker: WorkerParameters) -> NotificationScheduleWorker(
        scheduleRepository = get(),
        notificationRepository = get(),
        localNotificationCancelUseCase = get(),
        localNotificationScheduleUseCase = get(),
        upNextRepository = get(),
        context = androidContext(),
        parameters = worker)
    }
    worker { (worker: WorkerParameters) -> ContentSyncWorker(
        defaultSeasonUseCase = get(),
        fetchConfigUseCase = get(),
        overviewRepository = get(),
        workerProvider = get(),
        context = androidContext(),
        parameters = worker
    ) }
    single { WorkerProvider(androidContext()) }

    // CoroutineDispatcher
    // TODO: Add qualifiers for this!
    single { Dispatchers.IO }
}