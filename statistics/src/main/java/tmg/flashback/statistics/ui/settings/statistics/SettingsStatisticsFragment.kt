package tmg.flashback.statistics.ui.settings.statistics

import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.ui.settings.SettingsFragment
import tmg.flashback.statistics.R
import tmg.utilities.extensions.observeEvent

class SettingsStatisticsFragment: SettingsFragment<SettingsStatisticsViewModel>() {

    override val viewModel: SettingsStatisticsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        logScreenViewed("Settings Statistics")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}