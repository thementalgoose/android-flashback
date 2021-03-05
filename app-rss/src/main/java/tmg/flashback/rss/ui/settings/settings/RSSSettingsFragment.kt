package tmg.flashback.rss.ui.settings.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.core.ui.BaseFragment
import tmg.flashback.core.ui.settings.SettingsAdapter
import tmg.flashback.rss.R
import tmg.flashback.rss.databinding.FragmentRssSettingsBinding
import tmg.flashback.rss.ui.settings.RSSSettingsViewModel
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent

class RSSSettingsFragment: BaseFragment<FragmentRssSettingsBinding>() {

    private val viewModel: RSSSettingsViewModel by viewModel()

    private lateinit var adapter: SettingsAdapter

    override fun inflateView(inflater: LayoutInflater) = FragmentRssSettingsBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = SettingsAdapter(
            prefClicked = viewModel.inputs::clickPref,
            prefSwitchClicked = viewModel.inputs::updatePref
        )
        binding.settings.adapter = adapter
        binding.settings.layoutManager = LinearLayoutManager(context)

        observe(viewModel.outputs.settings) {
            adapter.list = it
        }

        observeEvent(viewModel.outputs.goToConfigure) {
            findNavController().navigate(R.id.graph_action_configure)
        }
    }
}