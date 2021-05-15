package tmg.common.ui.settings.appearance

import android.os.Bundle
import android.view.View
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.common.ui.settings.appearance.animation.AnimationSpeedBottomSheetFragment
import tmg.common.ui.settings.appearance.theme.ThemeBottomSheetFragment
import tmg.core.ui.settings.SettingsFragment
import tmg.utilities.extensions.observeEvent

class SettingsAppearanceFragment: SettingsFragment<SettingsAppearanceViewModel>() {

    override val viewModel: SettingsAppearanceViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeEvent(viewModel.outputs.openTheme) {
            val themeBottomSheetFragment = ThemeBottomSheetFragment()
            themeBottomSheetFragment.show(parentFragmentManager, "THEME")
        }

        observeEvent(viewModel.outputs.openAnimationSpeed) {
            val animationSpeedBottomSheetFragment = AnimationSpeedBottomSheetFragment()
            animationSpeedBottomSheetFragment.show(parentFragmentManager, "ANIMTION")
        }
    }

}