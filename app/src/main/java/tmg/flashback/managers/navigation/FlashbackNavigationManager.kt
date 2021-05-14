package tmg.flashback.managers.navigation

import android.content.Context
import android.content.Intent
import androidx.fragment.app.FragmentActivity
import tmg.aboutthisapp.AboutThisAppActivity
import tmg.common.constants.AboutThisAppConfig
import tmg.common.controllers.ReleaseNotesController
import tmg.flashback.core.managers.NavigationManager
import tmg.flashback.rss.ui.RSSActivity
import tmg.flashback.ui.settings.SettingsAllActivity
import tmg.flashback.ui.SplashActivity
import tmg.common.ui.releasenotes.ReleaseBottomSheetFragment
import tmg.flashback.statistics.ui.admin.maintenance.MaintenanceActivity

class FlashbackNavigationManager(
    private val releaseNotesController: ReleaseNotesController,
): NavigationManager {

    //region NavigationManager

    override fun getAboutThisAppIntent(context: Context): Intent {
        // TODO
        return AboutThisAppActivity.intent(
                context = context,
                configuration = AboutThisAppConfig.configuration(
                    context = context,
                    appVersion = "1.0",
                    appName = "Flashback",
                    isDarkMode = false,
                    deviceUdid = "test-guid"
//                    deviceUdid = coreRepository.deviceUdid
                )
        )
    }

    override fun getSettingsIntent(context: Context): Intent {
        return Intent(context, SettingsAllActivity::class.java)
    }

    override fun getMaintenanceIntent(context: Context): Intent {
        return Intent(context, MaintenanceActivity::class.java)
    }

    override fun getAppStartupIntent(context: Context): Intent {
        return Intent(context, SplashActivity::class.java)
    }

    override fun openContextualReleaseNotes(activity: FragmentActivity) {
        if (releaseNotesController.pendingReleaseNotes) {
            ReleaseBottomSheetFragment()
                .show(activity.supportFragmentManager, "RELEASE_NOTES")
        }
    }
}