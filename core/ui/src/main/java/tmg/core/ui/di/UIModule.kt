package tmg.core.ui.di

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tmg.core.ui.controllers.ThemeController

val uiModule = module {

    single { ThemeController(get(), get(), get()) }
}