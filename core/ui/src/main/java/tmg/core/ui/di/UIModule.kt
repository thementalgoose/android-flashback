package tmg.core.ui.di

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tmg.core.ui.controllers.ThemeController
import tmg.common.ui.settings.appearance.SettingsThemeViewModel
import tmg.common.ui.settings.appearance.animation.AnimationSpeedViewModel
import tmg.common.ui.settings.appearance.theme.ThemeViewModel

val uiModule = module {

    single { ThemeController(get(), get(), get()) }

    viewModel { tmg.common.ui.settings.appearance.SettingsThemeViewModel() }
    viewModel { tmg.common.ui.settings.appearance.theme.ThemeViewModel(get()) }
    viewModel { tmg.common.ui.settings.appearance.animation.AnimationSpeedViewModel(get()) }

}