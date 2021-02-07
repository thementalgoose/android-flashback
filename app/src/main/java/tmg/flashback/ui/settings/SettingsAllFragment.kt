package tmg.flashback.ui.settings

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_all_settings.*
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.R
import tmg.flashback.core.ui.BaseFragment
import tmg.utilities.extensions.observe

class SettingsAllFragment: BaseFragment() {

    private val viewModel: SettingsAllViewModel by viewModel()

    private lateinit var adapter: SettingsAllAdapter

    override fun layoutId() = R.layout.fragment_all_settings

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter = SettingsAllAdapter(
            categoryClicked = viewModel.inputs::clickCategory
        )
        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(context)
    }
}