package tmg.flashback.ui.settings.about

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import tmg.flashback.constants.App.playStoreUrl
import tmg.flashback.core.managers.NavigationManager
import tmg.core.ui.settings.SettingsFragment
import tmg.common.ui.privacypolicy.PrivacyPolicyActivity

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