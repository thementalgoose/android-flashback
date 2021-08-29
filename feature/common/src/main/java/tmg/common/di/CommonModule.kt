package tmg.common.di

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tmg.common.controllers.ForceUpgradeController
import tmg.common.controllers.ReleaseNotesController
import tmg.common.repository.ForceUpgradeRepository
import tmg.common.repository.ReleaseNotesRepository
import tmg.common.ui.forceupgrade.ForceUpgradeViewModel
import tmg.common.ui.privacypolicy.PrivacyPolicyViewModel
import tmg.common.ui.settings.about.SettingsAboutViewModel
import tmg.common.ui.settings.appearance.SettingsAppearanceViewModel
import tmg.common.ui.settings.appearance.animation.AnimationSpeedViewModel
import tmg.common.ui.settings.appearance.nightmode.NightMoveViewModel
import tmg.common.ui.settings.appearance.theme.ThemeViewModel
import tmg.common.ui.settings.notifications.SettingsNotificationViewModel
import tmg.common.ui.settings.support.SettingsSupportViewModel

val commonModule = module {

    single { ReleaseNotesController(get(), get()) }
    single { ReleaseNotesRepository(get()) }
    single { ForceUpgradeController(get()) }
    single { ForceUpgradeRepository(get()) }

    viewModel { ForceUpgradeViewModel(get(), get()) }

    viewModel { PrivacyPolicyViewModel() }

    viewModel { SettingsAppearanceViewModel() }
    viewModel { SettingsAboutViewModel() }
    viewModel { SettingsSupportViewModel(get(), get()) }
    viewModel { SettingsNotificationViewModel(get()) }
    viewModel { NightMoveViewModel(get()) }
    viewModel { ThemeViewModel(get()) }
    viewModel { AnimationSpeedViewModel(get()) }
}