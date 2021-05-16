package tmg.common.ui.settings.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.common.ui.privacypolicy.PrivacyPolicyActivity
import tmg.common.ui.releasenotes.ReleaseActivity
import tmg.core.device.managers.BuildConfigManager
import tmg.core.ui.navigation.NavigationProvider
import tmg.core.ui.settings.SettingsFragment
import tmg.utilities.extensions.observeEvent

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
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=${buildConfigManager.applicationId}"))
            startActivity(intent)
        }
    }
}