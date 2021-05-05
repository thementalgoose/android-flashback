package tmg.crash_reporting.ui.settings

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.crash_reporting.R
import tmg.flashback.shared.ui.settings.SettingsFragment
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent

class SettingsCrashReportingFragment: SettingsFragment() {

    private val viewModel: SettingsCrashReportingViewModel by viewModel()

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

        observeEvent(viewModel.outputs.notifyPreferencesAppliedAfterRestart) {
            Snackbar
                .make(binding.settingsList, getString(R.string.settings_crash_reporting_notify_after_restart), Snackbar.LENGTH_LONG)
                .show()
        }
    }
}