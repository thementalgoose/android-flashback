package tmg.flashback.statistics.ui.settings.notifications

import android.os.Bundle
import android.view.View
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.ui.settings.SettingsFragment
import tmg.flashback.statistics.ui.settings.notifications.reminder.UpNextReminderBottomSheetFragment
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
            UpNextReminderBottomSheetFragment()
                .show(parentFragmentManager, "TIME_PICKER")
        }
    }
}