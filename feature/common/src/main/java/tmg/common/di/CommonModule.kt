package tmg.common.di

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tmg.common.ui.settings.appearance.SettingsAppearanceViewModel
import tmg.common.ui.settings.appearance.animation.AnimationSpeedViewModel
import tmg.common.ui.settings.appearance.theme.ThemeViewModel
import tmg.common.ui.settings.notifications.SettingsNotificationViewModel
import tmg.crash_reporting.ui.settings.SettingsCrashReportingViewModel

val commonModule = module {

    viewModel { SettingsAppearanceViewModel() }
    viewModel { SettingsCrashReportingViewModel(get()) }
    viewModel { SettingsNotificationViewModel(get()) }
    viewModel { ThemeViewModel(get()) }
    viewModel { AnimationSpeedViewModel(get()) }
}