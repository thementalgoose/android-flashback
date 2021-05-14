package tmg.flashback.ui.settings

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.core.ui.settings.SettingsFragment
import tmg.flashback.R
import tmg.utilities.extensions.observe

class SettingsAllFragment: SettingsFragment<SettingsAllViewModel>() {

    override val viewModel: SettingsAllViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        analyticsController.logEvent(ViewType.SETTINGS_ALL)

        observe(viewModel.outputs.openAbout) {
            findNavController().navigate(R.id.graph_action_about)
        }

//        observeEvent(viewModel.outputs.navigateToo) {
//            when (it) {
//                Category.CUSTOMISATION -> findNavController().navigate(R.id.graph_action_customisation)
//                Category.STATISTICS -> findNavController().navigate(R.id.graph_action_statistics)
//                Category.NOTIFICATIONS -> findNavController().navigate(R.id.graph_action_notifications)
//                Category.RSS -> findNavController().navigate(R.id.graph_action_rss)
//                Category.WIDGETS -> findNavController().navigate(R.id.graph_action_widgets)
//                Category.DEVICE -> findNavController().navigate(R.id.graph_action_device)
//                Category.ABOUT -> findNavController().navigate(R.id.graph_action_about)
//            }
//        }
    }
}