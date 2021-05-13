package tmg.common.di

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tmg.common.ui.forceupgrade.ForceUpgradeViewModel
import tmg.common.ui.privacypolicy.PrivacyPolicyViewModel
import tmg.common.ui.settings.appearance.SettingsAppearanceViewModel
import tmg.common.ui.settings.appearance.animation.AnimationSpeedViewModel
import tmg.common.ui.settings.appearance.theme.ThemeViewModel
import tmg.common.ui.settings.notifications.SettingsNotificationViewModel
import tmg.common.ui.settings.support.SettingsSupportViewModel

val commonModule = module {

    viewModel { ForceUpgradeViewModel(get()) }

    viewModel { PrivacyPolicyViewModel() }

    viewModel { SettingsAppearanceViewModel() }
    viewModel { SettingsSupportViewModel(get(), get()) }
    viewModel { SettingsNotificationViewModel(get()) }
    viewModel { ThemeViewModel(get()) }
    viewModel { AnimationSpeedViewModel(get()) }
}