package tmg.flashback.regulations.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tmg.flashback.regulations.ui.FormatOverviewViewModel

val regulationsModule = module {

    viewModel { FormatOverviewViewModel() }
}