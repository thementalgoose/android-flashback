package tmg.flashback.ui.settings

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import tmg.flashback.R
import tmg.utilities.extensions.observeEvent

class SettingsAllFragment: SettingsFragment<SettingsAllViewModel>() {

    override val viewModel: SettingsAllViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        logScreenViewed("Settings all")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeEvent(viewModel.outputs.openAppearance) {
            findNavController().navigate(R.id.graph_action_appearance)
        }
        observeEvent(viewModel.outputs.openHome) {
            findNavController().navigate(R.id.graph_action_home)
        }
        observeEvent(viewModel.outputs.openRss) {
            findNavController().navigate(R.id.graph_action_rss)
        }
        observeEvent(viewModel.outputs.openNotifications) {
            findNavController().navigate(R.id.graph_action_notifications)
        }
        observeEvent(viewModel.outputs.openSupport) {
            findNavController().navigate(R.id.graph_action_support)
        }
        observeEvent(viewModel.outputs.openAbout) {
            findNavController().navigate(R.id.graph_action_about)
        }
        observeEvent(viewModel.outputs.openAds) {
            findNavController().navigate(R.id.graph_action_ads)
        }
    }
}