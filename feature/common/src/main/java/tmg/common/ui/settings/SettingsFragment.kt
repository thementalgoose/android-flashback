package tmg.common.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.core.ui.base.BaseFragment
import tmg.core.ui.databinding.FragmentSettingsBinding

abstract class SettingsFragment<T: Fragment>: BaseFragment<FragmentSettingsBinding>() {

    abstract val models: List<SettingsModel<T>>

    private lateinit var adapter: SettingsAdapter<T>

    override fun inflateView(inflater: LayoutInflater) =
        FragmentSettingsBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = SettingsAdapter(
            getFragmentContext = {
                this as T
            }
        )
        binding.settingsList.adapter = adapter
        binding.settingsList.layoutManager = LinearLayoutManager(context)

        adapter.list = models
    }
}