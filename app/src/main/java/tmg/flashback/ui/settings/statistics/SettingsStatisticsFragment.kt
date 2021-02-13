package tmg.flashback.ui.settings.statistics

import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.R
import tmg.flashback.core.ui.BaseFragment
import tmg.flashback.core.ui.settings.SettingsFragment
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent

class SettingsStatisticsFragment: SettingsFragment() {

    private val viewModel: SettingsStatisticsViewModel by viewModel()

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

        observeEvent(viewModel.outputs.defaultSeasonChanged) {
            Snackbar.make(view, R.string.settings_default_season_updated, Snackbar.LENGTH_SHORT).show()
        }
    }
}