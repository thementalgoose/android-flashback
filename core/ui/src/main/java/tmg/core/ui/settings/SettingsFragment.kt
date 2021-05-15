package tmg.core.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import tmg.core.ui.base.BaseFragment
import tmg.core.ui.databinding.FragmentSettingsBinding
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent

abstract class SettingsFragment<T: SettingsViewModel>: BaseFragment<FragmentSettingsBinding>() {

    abstract val vm: T

    lateinit var adapter: SettingsAdapter

    override fun inflateView(inflater: LayoutInflater) =
        FragmentSettingsBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = SettingsAdapter(
            clickSwitch = vm::clickSwitchPreference,
            clickPref = vm::clickPreference,
            getState = { it.getState() }
        )
        binding.settingsList.adapter = adapter
        binding.settingsList.layoutManager = LinearLayoutManager(context)

        observe(vm.settings) {
            adapter.list = it
        }

        observeEvent(vm.clickPref) {
            it.onClick?.invoke()
        }

        observeEvent(vm.switchPref) { (pref, newState) ->
            pref.saveStateNotification?.invoke(newState)
        }

        vm.loadSettings()
    }
}