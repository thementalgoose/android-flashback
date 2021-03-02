package tmg.flashback.ui.settings

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_all_settings.*
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.R
import tmg.flashback.core.ui.BaseFragment
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent

class SettingsAllFragment: BaseFragment() {

    private val viewModel: SettingsAllViewModel by viewModel()

    private lateinit var adapter: SettingsAllAdapter

    override fun layoutId() = R.layout.fragment_all_settings

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = SettingsAllAdapter(
                categoryClicked = viewModel.inputs::clickCategory
        )
        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(context)

        observe(viewModel.outputs.categories) {
            adapter.list = it
        }

        observeEvent(viewModel.outputs.navigateToo) {
            when (it) {
                Category.CUSTOMISATION -> findNavController().navigate(R.id.graph_action_customisation)
                Category.STATISTICS -> findNavController().navigate(R.id.graph_action_statistics)
                Category.NOTIFICATIONS -> findNavController().navigate(R.id.graph_action_notifications)
                Category.RSS -> findNavController().navigate(R.id.graph_action_rss)
                Category.WIDGETS -> findNavController().navigate(R.id.graph_action_widgets)
                Category.DEVICE -> findNavController().navigate(R.id.graph_action_device)
                Category.ABOUT -> findNavController().navigate(R.id.graph_action_about)
            }
        }
    }
}