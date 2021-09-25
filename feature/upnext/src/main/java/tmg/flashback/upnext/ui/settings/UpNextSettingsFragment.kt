package tmg.flashback.upnext.ui.settings

import android.os.Bundle
import android.view.View
import android.widget.Toast
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.core.ui.settings.SettingsFragment
import tmg.utilities.extensions.observeEvent

class UpNextSettingsFragment: SettingsFragment<UpNextSettingsViewModel>() {

    override val viewModel: UpNextSettingsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        logScreenViewed("Settings Up Next Notifications")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeEvent(viewModel.outputs.openTimePicker) {
            Toast.makeText(context, "Open Time Picker", Toast.LENGTH_LONG).show()
        }
    }
}