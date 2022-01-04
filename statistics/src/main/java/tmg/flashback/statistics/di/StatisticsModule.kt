package tmg.flashback.statistics.di

import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module
import tmg.flashback.statistics.controllers.RaceController
import tmg.flashback.statistics.controllers.SearchController
import tmg.flashback.statistics.controllers.HomeController
import tmg.flashback.statistics.controllers.ScheduleController
import tmg.flashback.statistics.repo.di.repoModule
import tmg.flashback.statistics.repository.HomeRepository
import tmg.flashback.statistics.repository.UpNextRepository
import tmg.flashback.statistics.ui.circuit.CircuitViewModel
import tmg.flashback.statistics.ui.dashboard.onboarding.OnboardingNotificationViewModel
import tmg.flashback.statistics.ui.dashboard.racepreview.RacePreviewViewModel
import tmg.flashback.statistics.ui.dashboard.season.SeasonViewModel
import tmg.flashback.statistics.ui.overview.constructor.stats.ConstructorViewModel
import tmg.flashback.statistics.ui.overview.driver.stats.DriverViewModel
import tmg.flashback.statistics.ui.overview.driver.season.DriverSeasonViewModel
import tmg.flashback.statistics.ui.race.RaceViewModel
import tmg.flashback.statistics.ui.search.SearchViewModel
import tmg.flashback.statistics.ui.search.category.CategoryViewModel
import tmg.flashback.statistics.ui.settings.home.SettingsHomeViewModel
import tmg.flashback.statistics.ui.settings.statistics.SettingsStatisticsViewModel
import tmg.flashback.statistics.ui.settings.notifications.UpNextSettingsViewModel
import tmg.flashback.statistics.ui.settings.notifications.reminder.UpNextReminderViewModel
import tmg.flashback.statistics.workmanager.NotificationScheduler

val statisticsModule = repoModule + module {

    viewModel { CircuitViewModel(get(), get()) }
    viewModel { SeasonViewModel(get(), get(), get(), get(), get(), get(), get(), get()) }
    viewModel { RacePreviewViewModel(get()) }
    viewModel { ConstructorViewModel(get(), get()) }
    viewModel { DriverViewModel(get(), get()) }
    viewModel { DriverSeasonViewModel(get(), get(), get()) }
    viewModel { RaceViewModel(get(), get(), get(), get(), get()) }
    viewModel { SearchViewModel(get(), get(), get(), get(), get()) }
    viewModel { CategoryViewModel() }

    viewModel { SettingsHomeViewModel(get()) }
    viewModel { SettingsStatisticsViewModel() }

    single { RaceController(get()) }
    single { HomeController(get()) }
    single { SearchController(get(), get()) }

    // App
    single { HomeRepository(get(), get()) }

    viewModel { UpNextSettingsViewModel(get()) }
    viewModel { OnboardingNotificationViewModel(get()) }
    viewModel { UpNextReminderViewModel(get()) }

    single { ScheduleController(androidContext(), get(), get(), get()) }
    single { UpNextRepository(get()) }

    // Worker
    worker { NotificationScheduler(get(), get(), get(), androidContext(), get()) }
}