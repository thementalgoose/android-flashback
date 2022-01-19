package tmg.flashback.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import tmg.flashback.ui.base.BaseFragment
import tmg.flashback.ui.databinding.FragmentSettingsBinding
import tmg.utilities.extensions.observe
import tmg.utilities.lifecycle.viewInflateBinding

abstract class SettingsFragment<T: SettingsViewModel>: BaseFragment() {

    abstract val viewModel: T
    private val binding by viewInflateBinding(FragmentSettingsBinding::inflate)

    lateinit var adapter: SettingsAdapter

    override fun onCreateView() = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = SettingsAdapter(
            clickSwitch = viewModel::clickSwitchPreference,
            clickPref = viewModel::clickPreference,
            getState = { it.getState() }
        )
        binding.settingsList.adapter = adapter
        binding.settingsList.layoutManager = LinearLayoutManager(context)

        observe(viewModel.settings) {
            adapter.list = it
        }

        viewModel.loadSettings()
    }
}