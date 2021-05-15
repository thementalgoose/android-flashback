package tmg.flashback.rss.ui.settings.settings

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.core.ui.settings.SettingsFragment
import tmg.flashback.rss.R
import tmg.utilities.extensions.observeEvent

class RSSSettingsFragment: SettingsFragment<RSSSettingsViewModel>() {

    override val viewModel: RSSSettingsViewModel by viewModel()

//    override val screenAnalytics = ScreenAnalytics(
//        screenName = "Settings - RSS"
//    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        analyticsController.logEvent(ViewType.SETTINGS_RSS)

        observeEvent(viewModel.outputs.goToConfigure) {
            findNavController().navigate(R.id.graph_action_configure)
        }
    }
}