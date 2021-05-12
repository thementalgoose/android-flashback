package tmg.flashback.ui.settings.widgets

import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import tmg.flashback.R
import tmg.core.ui.settings.SettingsFragment

class SettingsWidgetFragment: SettingsFragment() {

    private val viewModel: SettingsWidgetViewModel by viewModel()

//    override val screenAnalytics = ScreenAnalytics(
//        screenName = "Settings - Widget"
//    )

    override val prefClicked: (prefKey: String) -> Unit = { prefKey ->
        viewModel.inputs.preferenceClicked(prefKey, null)
    }
    override val prefSwitchClicked: (prefKey: String, newState: Boolean) -> Unit = { prefKey, newState ->
        viewModel.inputs.preferenceClicked(prefKey, newState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        analyticsController.logEvent(ViewType.SETTINGS_WIDGET)

        observe(viewModel.outputs.settings) {
            adapter.list = it
        }

        observeEvent(viewModel.outputs.refreshWidget) {
            context?.updateAllWidgets()
            Snackbar.make(view, R.string.settings_widgets_update_all_updated, Snackbar.LENGTH_SHORT).show()
        }
    }

}