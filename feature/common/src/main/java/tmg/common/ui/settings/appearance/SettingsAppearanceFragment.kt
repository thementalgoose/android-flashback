package tmg.common.ui.settings.appearance

import android.os.Bundle
import android.view.View
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.common.ui.settings.SettingsFragment
import tmg.utilities.extensions.observeEvent

class SettingsAppearanceFragment: SettingsFragment<SettingsAppearanceViewModel>() {

    override val viewModel: SettingsAppearanceViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeEvent(viewModel.outputs.openTheme) {
            TODO("Open the theme bottom sheet")
        }

        observeEvent(viewModel.outputs.openAnimationSpeed) {
            TODO("Open the animation bottom sheet")
        }
    }

}