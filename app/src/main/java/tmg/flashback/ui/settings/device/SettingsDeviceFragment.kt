package tmg.flashback.ui.settings.device

import android.content.Intent
import android.os.Bundle
import android.view.View
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.core.ui.settings.SettingsFragment
import tmg.flashback.ui.settings.release.ReleaseActivity
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent

class SettingsDeviceFragment: SettingsFragment() {

    private val viewModel: SettingsDeviceViewModel by viewModel()

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

        observeEvent(viewModel.outputs.openReleaseNotes) {
            context?.let {
                startActivity(Intent(it, ReleaseActivity::class.java))
            }
        }
    }

}