package tmg.common.ui.settings.about

import android.os.Bundle
import android.view.View
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.common.ui.settings.SettingsFragment

class SettingsAboutFragment: SettingsFragment<SettingsAboutViewModel>() {

    override val viewModel: SettingsAboutViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}