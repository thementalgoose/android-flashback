package tmg.f1stats.di

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tmg.f1stats.home.season.SeasonRaceViewModel
import tmg.f1stats.home.season.SeasonViewModel

var f1Module = module {
    viewModel { SeasonViewModel() }
    viewModel { SeasonRaceViewModel() }
}