package tmg.flashback.ui.settings

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import tmg.flashback.R
import tmg.flashback.core.ui.BaseFragment

class SettingsAllFragment: BaseFragment() {

    private lateinit var adapter: SettingsAllAdapter

    override fun layoutId() = R.layout.fragment_all_settings

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter = SettingsAllAdapter(
            categoryClicked = {
                when (it) {
                    Category.RSS -> findNavController().navigate(R.id.graph_action_about)
                    Category.ABOUT -> findNavController().navigate(R.id.graph_action_customisation)
                }
            }
        )
    }
}