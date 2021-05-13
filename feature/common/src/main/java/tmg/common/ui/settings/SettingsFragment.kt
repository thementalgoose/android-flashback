package tmg.common.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.core.ui.base.BaseFragment
import tmg.core.ui.databinding.FragmentSettingsBinding
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent

abstract class SettingsFragment<T: SettingsViewModel>: BaseFragment<FragmentSettingsBinding>() {

    abstract val viewModel: T

    private lateinit var adapter: SettingsAdapter

    override fun inflateView(inflater: LayoutInflater) =
        FragmentSettingsBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = SettingsAdapter(
            clickSwitch = viewModel::clickSwitchPreference,
            clickPref = viewModel::clickPreference,
            getState = { it.getState(viewModel) }
        )
        binding.settingsList.adapter = adapter
        binding.settingsList.layoutManager = LinearLayoutManager(context)

        observe(viewModel.settings) {
            adapter.list = it
        }

        observeEvent(viewModel.clickPref) {
            it.onClick?.invoke(this)
        }

        observeEvent(viewModel.switchPref) { (pref, newState) ->
            pref.saveStateNotification?.invoke(this, newState)
        }
    }
}