package tmg.flashback.core.ui.settings

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import tmg.flashback.core.databinding.FragmentSettingsBinding
import tmg.flashback.core.ui.BaseFragment

abstract class SettingsFragment: BaseFragment<FragmentSettingsBinding>() {

    abstract val prefClicked: (prefKey: String) -> Unit
    abstract val prefSwitchClicked: (prefKey: String, newState: Boolean) -> Unit

    lateinit var adapter: SettingsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = SettingsAdapter(
            prefClicked = prefClicked,
            prefSwitchClicked = prefSwitchClicked
        )
        binding.settingsList.adapter = adapter
        binding.settingsList.layoutManager = LinearLayoutManager(context)
    }
}