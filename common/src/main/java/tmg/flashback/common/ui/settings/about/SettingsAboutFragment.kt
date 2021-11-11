package tmg.flashback.common.ui.settings.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.common.ui.privacypolicy.PrivacyPolicyActivity
import tmg.flashback.common.ui.releasenotes.ReleaseActivity
import tmg.flashback.device.managers.BuildConfigManager
import tmg.flashback.ui.navigation.NavigationProvider
import tmg.flashback.ui.settings.SettingsFragment
import tmg.utilities.extensions.observeEvent
import tmg.utilities.extensions.viewUrl

class SettingsAboutFragment: SettingsFragment<SettingsAboutViewModel>() {

    override val viewModel: SettingsAboutViewModel by viewModel()

    private val navigationProvider: NavigationProvider by inject()
    private val buildConfigManager: BuildConfigManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        logScreenViewed("Settings About")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeEvent(viewModel.outputs.openAboutThisApp) {
            context?.let {
                val intent = navigationProvider.aboutAppIntent(it)
                startActivity(intent)
            }
        }

        observeEvent(viewModel.outputs.openReleaseNotes) {
            startActivity(Intent(context, ReleaseActivity::class.java))
        }

        observeEvent(viewModel.outputs.openPrivacyPolicy) {
            startActivity(Intent(context, PrivacyPolicyActivity::class.java))
        }

        observeEvent(viewModel.outputs.openReview) {
            viewUrl("http://play.google.com/store/apps/details?id=${buildConfigManager.applicationId}")
        }
    }
}