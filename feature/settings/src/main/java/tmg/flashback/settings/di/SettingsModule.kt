package tmg.flashback.settings.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tmg.flashback.settings.ui.privacypolicy.PrivacyPolicyViewModel
import tmg.flashback.settings.ui.settings.about.SettingsAboutViewModel
import tmg.flashback.settings.ui.settings.appearance.SettingsAppearanceViewModel
import tmg.flashback.settings.ui.settings.appearance.animation.AnimationSpeedViewModel
import tmg.flashback.settings.ui.settings.appearance.nightmode.NightMoveViewModel
import tmg.flashback.settings.ui.settings.appearance.theme.ThemeViewModel
import tmg.flashback.settings.ui.settings.support.SettingsSupportViewModel

val settingsModule = module {

    viewModel { PrivacyPolicyViewModel() }

    viewModel { SettingsAppearanceViewModel(get(), get()) }
    viewModel { SettingsAboutViewModel() }
    viewModel { SettingsSupportViewModel(get(), get()) }
    viewModel { NightMoveViewModel(get(), get()) }
    viewModel { ThemeViewModel(get()) }
    viewModel { AnimationSpeedViewModel(get()) }
}