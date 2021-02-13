package tmg.flashback.ui.settings.customisation

import android.os.Bundle
import android.view.View
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.core.ui.settings.SettingsFragment
import tmg.flashback.ui.settings.customisation.animation.AnimationSpeedBottomSheetFragment
import tmg.flashback.ui.settings.customisation.theme.ThemeBottomSheetFragment
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent

class SettingsCustomisationFragment: SettingsFragment() {

    private val viewModel: SettingsCustomisationViewModel by viewModel()

    override val prefClicked: (prefKey: String) -> Unit = { prefKey ->
        viewModel.inputs.preferenceClicked(prefKey, null)
    }
    override val prefSwitchClicked: (prefKey: String, newState: Boolean) -> Unit = { prefKey, newState ->
        viewModel.inputs.preferenceClicked(prefKey, newState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observe(viewModel.outputs.settings) {
            adapter.list = it
        }

        observeEvent(viewModel.outputs.openTheme) {
            activity?.let {
                ThemeBottomSheetFragment().show(it.supportFragmentManager, "THEME")
            }
        }

        observeEvent(viewModel.outputs.openAnimationSpeed) {
            activity?.let {
                AnimationSpeedBottomSheetFragment().show(it.supportFragmentManager, "ANIMATION_SPEED")
            }
        }
    }

}