package tmg.flashback.stats.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tmg.flashback.stats.ui.dashboard.calendar.CalendarViewModel
import tmg.flashback.stats.ui.dashboard.constructors.ConstructorsStandingViewModel
import tmg.flashback.stats.ui.dashboard.drivers.DriversStandingViewModel
import tmg.flashback.stats.ui.weekend.WeekendViewModel

val statsModule = module {

    viewModel { CalendarViewModel(get(), get(), get(), get()) }
    viewModel { ConstructorsStandingViewModel(get(), get(), get()) }
    viewModel { DriversStandingViewModel(get(), get(), get()) }

    viewModel { WeekendViewModel() }

}