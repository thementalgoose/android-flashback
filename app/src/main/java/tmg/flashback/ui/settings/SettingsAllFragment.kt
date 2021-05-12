package tmg.flashback.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import tmg.flashback.R
import tmg.flashback.constants.ViewType
import tmg.core.ui.base.BaseFragment
import tmg.flashback.core.utils.ScreenAnalytics
import tmg.flashback.databinding.FragmentAllSettingsBinding

class SettingsAllFragment: BaseFragment<FragmentAllSettingsBinding>() {

    private val viewModel: SettingsAllViewModel by viewModel()

    override val screenAnalytics = ScreenAnalytics(
        screenName = "Settings - All"
    )

    private lateinit var adapter: SettingsAllAdapter

    override fun inflateView(inflater: LayoutInflater) =
        FragmentAllSettingsBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        analyticsController.logEvent(ViewType.SETTINGS_ALL)

        adapter = SettingsAllAdapter(
                categoryClicked = viewModel.inputs::clickCategory
        )
        binding.list.adapter = adapter
        binding.list.layoutManager = LinearLayoutManager(context)

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