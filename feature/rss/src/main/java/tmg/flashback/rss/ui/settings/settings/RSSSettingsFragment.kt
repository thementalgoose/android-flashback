package tmg.flashback.rss.ui.settings.settings

import android.os.Bundle
import android.view.View
import org.koin.androidx.viewmodel.ext.android.viewModel
import tmg.flashback.rss.ui.settings.SettingsRSSViewModel
import tmg.flashback.ui.settings.SettingsFragment

internal class RSSSettingsFragment: SettingsFragment<SettingsRSSViewModel>() {

    override val viewModel: SettingsRSSViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        logScreenViewed("Settings RSS", emptyMap())
    }
}