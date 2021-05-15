package tmg.common.ui.settings.support

import android.os.Bundle
import android.view.View
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.core.ui.settings.SettingsFragment

class SettingsSupportFragment: SettingsFragment<SettingsSupportViewModel>() {

    override val vm: SettingsSupportViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}