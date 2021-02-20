package tmg.flashback.rss.ui.settings.settings

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_rss_settings.*
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.core.ui.BaseFragment
import tmg.flashback.core.ui.settings.SettingsAdapter
import tmg.flashback.rss.R
import tmg.flashback.rss.ui.settings.RSSSettingsViewModel
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent

class RSSSettingsFragment: BaseFragment() {

    private val viewModel: RSSSettingsViewModel by viewModel()

    private lateinit var adapter: SettingsAdapter

    override fun layoutId() = R.layout.fragment_rss_settings

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = SettingsAdapter(
            prefClicked = viewModel.inputs::clickPref,
            prefSwitchClicked = viewModel.inputs::updatePref
        )
        settings.adapter = adapter
        settings.layoutManager = LinearLayoutManager(context)

        observe(viewModel.outputs.settings) {
            adapter.list = it
        }

        observeEvent(viewModel.outputs.goToConfigure) {
            findNavController().navigate(R.id.graph_action_configure)
        }
    }
}