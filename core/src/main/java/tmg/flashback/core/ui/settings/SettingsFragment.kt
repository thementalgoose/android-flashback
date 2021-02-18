package tmg.flashback.core.ui.settings

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_settings.*
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.core.R
import tmg.flashback.core.ui.BaseFragment

abstract class SettingsFragment: BaseFragment() {

    abstract val prefClicked: (prefKey: String) -> Unit
    abstract val prefSwitchClicked: (prefKey: String, newState: Boolean) -> Unit

    lateinit var adapter: SettingsAdapter

    override fun layoutId() = R.layout.fragment_settings

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = SettingsAdapter(
            prefClicked = prefClicked,
            prefSwitchClicked = prefSwitchClicked
        )
        settingsList.adapter = adapter
        settingsList.layoutManager = LinearLayoutManager(context)
    }
}