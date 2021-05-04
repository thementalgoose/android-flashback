package tmg.flashback.shared.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import tmg.flashback.shared.ui.base.BaseFragment
import tmg.flashback.shared.ui.databinding.FragmentSettingsBinding

abstract class SettingsFragment: BaseFragment<FragmentSettingsBinding>() {

    abstract val prefClicked: (prefKey: String) -> Unit
    abstract val prefSwitchClicked: (prefKey: String, newState: Boolean) -> Unit

    lateinit var adapter: SettingsAdapter

    override fun inflateView(inflater: LayoutInflater) =
        FragmentSettingsBinding.inflate(inflater)

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