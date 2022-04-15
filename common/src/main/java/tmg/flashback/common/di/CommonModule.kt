package tmg.flashback.common.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tmg.flashback.common.CommonNavigationComponent
import tmg.flashback.common.repository.ForceUpgradeRepository
import tmg.flashback.common.repository.ReleaseNotesRepository
import tmg.flashback.common.ui.forceupgrade.ForceUpgradeViewModel
import tmg.flashback.common.ui.privacypolicy.PrivacyPolicyViewModel
import tmg.flashback.common.ui.settings.about.SettingsAboutViewModel
import tmg.flashback.common.ui.settings.appearance.SettingsAppearanceViewModel
import tmg.flashback.common.ui.settings.appearance.animation.AnimationSpeedViewModel
import tmg.flashback.common.ui.settings.appearance.nightmode.NightMoveViewModel
import tmg.flashback.common.ui.settings.appearance.theme.ThemeViewModel
import tmg.flashback.common.ui.settings.support.SettingsSupportViewModel
import tmg.flashback.common.usecases.NewReleaseNotesUseCase

val commonModule = module {

    single { CommonNavigationComponent(get()) }

    factory { NewReleaseNotesUseCase(get(), get()) }

    single { ReleaseNotesRepository(get(), get()) }
    single { ForceUpgradeRepository(get()) }

    viewModel { ForceUpgradeViewModel(get(), get()) }

    viewModel { PrivacyPolicyViewModel() }

    viewModel { SettingsAppearanceViewModel(get(), get()) }
    viewModel { SettingsAboutViewModel() }
    viewModel { SettingsSupportViewModel(get(), get()) }
    viewModel { NightMoveViewModel(get(), get()) }
    viewModel { ThemeViewModel(get()) }
    viewModel { AnimationSpeedViewModel(get()) }
}