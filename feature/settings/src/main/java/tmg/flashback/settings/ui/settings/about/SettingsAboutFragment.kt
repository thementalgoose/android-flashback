package tmg.flashback.settings.ui.settings.about

import android.content.Intent
import android.os.Bundle
import android.view.View
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import tmg.flashback.settings.ui.privacypolicy.PrivacyPolicyActivity
import tmg.flashback.device.managers.BuildConfigManager
import tmg.flashback.releasenotes.ReleaseNotesNavigationComponent
import tmg.flashback.ui.navigation.ApplicationNavigationComponent
import tmg.flashback.ui.settings.SettingsFragment
import tmg.utilities.extensions.observeEvent
import tmg.utilities.extensions.viewUrl

class SettingsAboutFragment: SettingsFragment<SettingsAboutViewModel>() {

    override val viewModel: SettingsAboutViewModel by viewModel()

    private val applicationNavigationComponent: ApplicationNavigationComponent by inject()
    private val buildConfigManager: BuildConfigManager by inject()
    private val releaseNotesNavigationComponent: ReleaseNotesNavigationComponent by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        logScreenViewed("Settings About")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeEvent(viewModel.outputs.openAboutThisApp) {
            context?.let {
                val intent = applicationNavigationComponent.aboutAppIntent(it)
                startActivity(intent)
            }
        }

        observeEvent(viewModel.outputs.openReleaseNotes) {
            releaseNotesNavigationComponent.releaseNotes()
        }

        observeEvent(viewModel.outputs.openPrivacyPolicy) {
            startActivity(Intent(context, PrivacyPolicyActivity::class.java))
        }

        observeEvent(viewModel.outputs.openReview) {
            viewUrl("http://play.google.com/store/apps/details?id=${buildConfigManager.applicationId}")
        }
    }
}