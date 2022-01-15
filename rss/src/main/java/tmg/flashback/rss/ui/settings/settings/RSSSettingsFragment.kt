package tmg.flashback.rss.ui.settings.settings

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import tmg.flashback.ui.settings.SettingsFragment
import tmg.flashback.rss.R
import tmg.utilities.extensions.observeEvent

class RSSSettingsFragment: SettingsFragment<RSSSettingsViewModel>() {

    override val viewModel: RSSSettingsViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        logScreenViewed("Settings RSS", emptyMap())

        observeEvent(viewModel.outputs.goToConfigure) {
            findNavController().navigate(R.id.graph_action_configure)
        }
    }
}