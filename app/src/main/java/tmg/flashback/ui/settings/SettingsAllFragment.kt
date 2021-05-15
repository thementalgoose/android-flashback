package tmg.flashback.ui.settings

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.core.ui.settings.SettingsAdapter
import tmg.core.ui.settings.SettingsFragment
import tmg.flashback.R
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent

class SettingsAllFragment: SettingsFragment<SettingsAllViewModel>() {

    override val vm: SettingsAllViewModel by viewModel()

//    private val viewModel: SettingsAllViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        adapter = SettingsAdapter(
//                clickSwitch = viewModel::clickSwitchPreference,
//                clickPref = viewModel::clickPreference,
//                getState = { it.getState() }
//        )
//        binding.settingsList.adapter = adapter
//        binding.settingsList.layoutManager = LinearLayoutManager(context)

        observe(vm.outputs.openAbout) {
            findNavController().navigate(R.id.graph_action_about)
        }

//        observe(viewModel.settings) {
//            adapter.list = it
//        }
//
//        observeEvent(viewModel.clickPref) {
//            it.onClick?.invoke()
//        }
//
//        observeEvent(viewModel.switchPref) { (pref, newState) ->
//            pref.saveStateNotification?.invoke(newState)
//        }
    }
}