package tmg.flashback.common.ui.settings.support

import android.os.Bundle
import android.view.View
import org.koin.androidx.viewmodel.ext.android.viewModel
import tmg.flashback.ui.settings.SettingsFragment

class SettingsSupportFragment: SettingsFragment<SettingsSupportViewModel>() {

    override val viewModel: SettingsSupportViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        logScreenViewed("Settings Support")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}