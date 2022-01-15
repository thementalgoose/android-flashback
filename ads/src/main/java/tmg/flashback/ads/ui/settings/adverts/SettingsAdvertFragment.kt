package tmg.flashback.ads.ui.settings.adverts

import android.os.Bundle
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import tmg.flashback.ui.settings.SettingsFragment

class SettingsAdvertFragment: SettingsFragment<SettingsAdvertViewModel>() {

    override val viewModel: SettingsAdvertViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        logScreenViewed("Settings Adverts")
    }
}