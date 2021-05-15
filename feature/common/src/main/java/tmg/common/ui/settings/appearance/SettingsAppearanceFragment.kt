package tmg.common.ui.settings.appearance

import android.os.Bundle
import android.view.View
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.core.ui.settings.SettingsFragment
import tmg.utilities.extensions.observeEvent

class SettingsAppearanceFragment: SettingsFragment<SettingsAppearanceViewModel>() {

    override val vm: SettingsAppearanceViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeEvent(vm.outputs.openTheme) {
            TODO("Event")
        }

        observeEvent(vm.outputs.openAnimationSpeed) {
            TODO("Event")
        }
    }

}