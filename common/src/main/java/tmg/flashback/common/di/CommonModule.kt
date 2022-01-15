package tmg.flashback.common.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tmg.flashback.common.controllers.ForceUpgradeController
import tmg.flashback.common.controllers.ReleaseNotesController
import tmg.flashback.common.repository.ForceUpgradeRepository
import tmg.flashback.common.repository.ReleaseNotesRepository
import tmg.flashback.common.ui.forceupgrade.ForceUpgradeViewModel
import tmg.flashback.common.ui.privacypolicy.PrivacyPolicyViewModel
import tmg.flashback.common.ui.settings.about.SettingsAboutViewModel
import tmg.flashback.common.ui.settings.appearance.SettingsAppearanceViewModel
import tmg.flashback.common.ui.settings.appearance.animation.AnimationSpeedViewModel
import tmg.flashback.common.ui.settings.appearance.nightmode.NightMoveViewModel
import tmg.flashback.common.ui.settings.appearance.theme.ThemeViewModel
import tmg.flashback.common.ui.settings.notifications.SettingsNotificationViewModel
import tmg.flashback.common.ui.settings.support.SettingsSupportViewModel

val commonModule = module {

    single { ReleaseNotesController(get(), get()) }
    single { ReleaseNotesRepository(get()) }
    single { ForceUpgradeController(get()) }
    single { ForceUpgradeRepository(get()) }

    viewModel { ForceUpgradeViewModel(get(), get()) }

    viewModel { PrivacyPolicyViewModel() }

    viewModel { SettingsAppearanceViewModel(get(), get()) }
    viewModel { SettingsAboutViewModel() }
    viewModel { SettingsSupportViewModel(get(), get()) }
    viewModel { SettingsNotificationViewModel(get()) }
    viewModel { NightMoveViewModel(get()) }
    viewModel { ThemeViewModel(get()) }
    viewModel { AnimationSpeedViewModel(get()) }
}