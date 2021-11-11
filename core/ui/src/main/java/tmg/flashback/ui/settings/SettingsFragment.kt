package tmg.flashback.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import tmg.flashback.ui.base.BaseFragment
import tmg.flashback.ui.databinding.FragmentSettingsBinding
import tmg.utilities.extensions.observe

abstract class SettingsFragment<T: SettingsViewModel>: BaseFragment<FragmentSettingsBinding>() {

    abstract val viewModel: T

    lateinit var adapter: SettingsAdapter

    override fun inflateView(inflater: LayoutInflater) =
        FragmentSettingsBinding.inflate(inflater)

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