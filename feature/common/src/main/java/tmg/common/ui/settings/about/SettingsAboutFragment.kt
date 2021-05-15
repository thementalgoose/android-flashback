package tmg.common.ui.settings.about

import android.os.Bundle
import android.view.View
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.core.ui.settings.SettingsFragment
import tmg.utilities.extensions.observeEvent

class SettingsAboutFragment: SettingsFragment<SettingsAboutViewModel>() {

    override val vm: SettingsAboutViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeEvent(vm.outputs.openAboutThisApp) {
            TODO("Event")
        }

        observeEvent(vm.outputs.openReleaseNotes) {
            TODO("Event")
        }

        observeEvent(vm.outputs.openPrivacyPolicy) {
            TODO("Event")
        }

        observeEvent(vm.outputs.openReview) {
            TODO("Event")
        }
    }
}