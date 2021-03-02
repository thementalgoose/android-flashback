package tmg.flashback.ui.settings.widgets

import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.R
import tmg.flashback.core.ui.settings.SettingsFragment
import tmg.flashback.statistics.extensions.updateAllWidgets
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent

class SettingsWidgetFragment: SettingsFragment() {

    private val viewModel: SettingsWidgetViewModel by viewModel()

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

        observeEvent(viewModel.outputs.refreshWidget) {
            context?.updateAllWidgets()
            Snackbar.make(view, R.string.settings_widgets_update_all_updated, Snackbar.LENGTH_SHORT).show()
        }
    }

}