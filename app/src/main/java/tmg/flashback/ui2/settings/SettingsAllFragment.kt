package tmg.flashback.ui2.settings

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import tmg.flashback.R
import tmg.flashback.RssNavigationComponent
import tmg.flashback.ui.settings.SettingsFragment
import tmg.utilities.extensions.observeEvent

class SettingsAllFragment: SettingsFragment<SettingsAllViewModel>() {

    override val viewModel: SettingsAllViewModel by viewModel()
    private val rssNavigationComponent: RssNavigationComponent by inject()

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
            rssNavigationComponent.settingsRSS()
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