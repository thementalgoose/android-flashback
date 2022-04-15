package tmg.flashback.stats.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tmg.flashback.stats.repository.HomeRepository
import tmg.flashback.stats.repository.NotificationRepository
import tmg.flashback.stats.ui.dashboard.calendar.CalendarViewModel
import tmg.flashback.stats.ui.dashboard.constructors.ConstructorsStandingViewModel
import tmg.flashback.stats.ui.dashboard.drivers.DriversStandingViewModel
import tmg.flashback.stats.ui.weekend.WeekendViewModel
import tmg.flashback.stats.usecases.DefaultSeasonUseCase
import tmg.flashback.stats.usecases.FetchSeasonUseCase

val statsModule = module {

    viewModel { CalendarViewModel(get(), get(), get()) }
    viewModel { ConstructorsStandingViewModel(get(), get(), get()) }
    viewModel { DriversStandingViewModel(get(), get(), get()) }

    viewModel { WeekendViewModel() }

    single { DefaultSeasonUseCase(get()) }
    single { FetchSeasonUseCase(get(), get(), get()) }

    single { HomeRepository(get(), get()) }
    single { NotificationRepository(get()) }

}