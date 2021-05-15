package tmg.common.ui.settings.about

import android.os.Bundle
import android.view.View
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.core.ui.settings.SettingsFragment
import tmg.utilities.extensions.observeEvent

class SettingsAboutFragment: SettingsFragment<SettingsAboutViewModel>() {

    override val viewModel: SettingsAboutViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeEvent(viewModel.outputs.openAboutThisApp) {
            TODO("Event")
        }

        observeEvent(viewModel.outputs.openReleaseNotes) {
            TODO("Event")
        }

        observeEvent(viewModel.outputs.openPrivacyPolicy) {
            TODO("Event")
        }

        observeEvent(viewModel.outputs.openReview) {
            TODO("Event")
        }
    }
}