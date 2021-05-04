package tmg.flashback.ui.settings.device

import android.content.Intent
import android.os.Bundle
import android.view.View
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.constants.ViewType
import tmg.flashback.shared.ui.settings.SettingsFragment
import tmg.flashback.core.utils.ScreenAnalytics
import tmg.flashback.ui.settings.release.ReleaseActivity
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent

class SettingsDeviceFragment: SettingsFragment() {

    private val viewModel: SettingsDeviceViewModel by viewModel()

    override val screenAnalytics = ScreenAnalytics(
        screenName = "Settings - Device"
    )

    override val prefClicked: (prefKey: String) -> Unit = { prefKey ->
        viewModel.inputs.preferenceClicked(prefKey, null)
    }
    override val prefSwitchClicked: (prefKey: String, newState: Boolean) -> Unit = { prefKey, newState ->
        viewModel.inputs.preferenceClicked(prefKey, newState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        analyticsController.logEvent(ViewType.SETTINGS_DEVICE)

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