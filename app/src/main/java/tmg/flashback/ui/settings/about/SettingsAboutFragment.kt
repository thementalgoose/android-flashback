package tmg.flashback.ui.settings.about

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.constants.App.playStoreUrl
import tmg.flashback.constants.ViewType
import tmg.flashback.core.managers.NavigationManager
import tmg.flashback.shared.ui.settings.SettingsFragment
import tmg.flashback.core.utils.ScreenAnalytics
import tmg.flashback.ui.settings.privacy.PrivacyPolicyActivity
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent

class SettingsAboutFragment: SettingsFragment() {

    private val viewModel: SettingsAboutViewModel by viewModel()

    private val navigationManager: NavigationManager by inject()
//    override val screenAnalytics = ScreenAnalytics(
//        screenName = "Settings - About"
//    )

    override val prefClicked: (prefKey: String) -> Unit = { prefKey ->
        viewModel.inputs.preferenceClicked(prefKey, null)
    }
    override val prefSwitchClicked: (prefKey: String, newState: Boolean) -> Unit = { prefKey, newState ->
        viewModel.inputs.preferenceClicked(prefKey, newState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        analyticsController.logEvent(ViewType.SETTINGS_ABOUT)

        observe(viewModel.outputs.settings) {
            adapter.list = it
        }

        observeEvent(viewModel.outputs.openAboutThisApp) {
            showAbout()
        }

        observeEvent(viewModel.outputs.openReview) {
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(playStoreUrl))
                startActivity(intent)
            } catch (e: ActivityNotFoundException) { }
        }

        observeEvent(viewModel.outputs.openPrivacyPolicy) {
            context?.let {
                startActivity(Intent(it, PrivacyPolicyActivity::class.java))
            }
        }
    }

    private fun showAbout() {
        context?.let {
            startActivity(navigationManager.getAboutThisAppIntent(it))
        }
    }
}