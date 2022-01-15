package tmg.flashback.common.ui.settings.appearance

import android.os.Bundle
import android.view.View
import org.koin.androidx.viewmodel.ext.android.viewModel
import tmg.flashback.common.ui.settings.appearance.animation.AnimationSpeedBottomSheetFragment
import tmg.flashback.common.ui.settings.appearance.nightmode.NightModeBottomSheetFragment
import tmg.flashback.common.ui.settings.appearance.theme.ThemeBottomSheetFragment
import tmg.flashback.ui.settings.SettingsFragment
import tmg.utilities.extensions.observeEvent

class SettingsAppearanceFragment: SettingsFragment<SettingsAppearanceViewModel>() {

    override val viewModel: SettingsAppearanceViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        logScreenViewed("Settings Appearance")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeEvent(viewModel.outputs.openNightMode) {
            val themeBottomSheetFragment = NightModeBottomSheetFragment()
            themeBottomSheetFragment.show(parentFragmentManager, "NIGHT_MODE")
        }

        observeEvent(viewModel.outputs.openTheme) {
            val themeBottomSheetFragment = ThemeBottomSheetFragment()
            themeBottomSheetFragment.show(parentFragmentManager, "THEME")
        }

        observeEvent(viewModel.outputs.openAnimationSpeed) {
            val animationSpeedBottomSheetFragment = AnimationSpeedBottomSheetFragment()
            animationSpeedBottomSheetFragment.show(parentFragmentManager, "ANIMTION")
        }
    }

    companion object {
        val requestKey: String = "themeKey"
        val bundleKey: String = "resetApp"
    }
}