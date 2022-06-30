package tmg.flashback.statistics.ui.settings.statistics

import android.os.Bundle
import android.view.View
import org.koin.androidx.viewmodel.ext.android.viewModel
import tmg.flashback.ui.settings.SettingsFragment

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