package tmg.flashback.ui.settings.about

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.components.about.AboutThisAppActivity
import tmg.flashback.constants.AboutThisAppConfig
import tmg.flashback.constants.App.playStoreUrl
import tmg.flashback.core.controllers.AppearanceController
import tmg.flashback.core.managers.AppSettingsManager
import tmg.flashback.core.ui.settings.SettingsFragment
import tmg.flashback.ui.settings.privacy.PrivacyPolicyActivity
import tmg.flashback.ui.settings.release.ReleaseActivity
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent

class SettingsAboutFragment: SettingsFragment() {

    private val viewModel: SettingsAboutViewModel by viewModel()

    private val appSettingsManager: AppSettingsManager by inject()

    override val prefClicked: (prefKey: String) -> Unit = { prefKey ->
        viewModel.inputs.preferenceClicked(prefKey, null)
    }
    override val prefSwitchClicked: (prefKey: String, newState: Boolean) -> Unit = { prefKey, newState ->
        viewModel.inputs.preferenceClicked(prefKey, newState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
            appSettingsManager.openAboutThisApp(it)
        }
    }
}