package tmg.flashback.shared.ui.di

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tmg.flashback.shared.ui.ui.SettingsThemeViewModel
import tmg.flashback.shared.ui.ui.animation.AnimationSpeedViewModel
import tmg.flashback.shared.ui.ui.theme.ThemeViewModel

val uiModule = module {

    viewModel { SettingsThemeViewModel() }
    viewModel { ThemeViewModel(get()) }
    viewModel { AnimationSpeedViewModel(get()) }

}