package tmg.flashback.statistics.ui.settings.home

import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.statistics.R
import tmg.flashback.statistics.ui.settings.statistics.SettingsStatisticsViewModel
import tmg.flashback.ui.settings.SettingsFragment
import tmg.utilities.extensions.observeEvent

class SettingsHomeFragment: SettingsFragment<SettingsHomeViewModel>() {

    override val viewModel: SettingsHomeViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        logScreenViewed("Settings Home")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeEvent(viewModel.outputs.defaultSeasonChanged) {
            Snackbar.make(view, R.string.settings_default_season_updated, Snackbar.LENGTH_SHORT).show()
        }
    }
}