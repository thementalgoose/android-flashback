package tmg.flashback.managers.navigation

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import tmg.components.about.AboutThisAppActivity
import tmg.flashback.constants.AboutThisAppConfig
import tmg.flashback.controllers.ReleaseNotesController
import tmg.flashback.core.controllers.AppearanceController
import tmg.flashback.core.extensions.isLightMode
import tmg.flashback.core.managers.NavigationManager
import tmg.flashback.rss.ui.RSSActivity
import tmg.flashback.statistics.manager.StatisticsExternalNavigationManager
import tmg.flashback.ui.admin.maintenance.MaintenanceActivity
import tmg.flashback.ui.settings.SettingsAllActivity
import tmg.flashback.ui.SplashActivity
import tmg.flashback.ui.settings.release.ReleaseBottomSheetFragment

class FlashbackNavigationManager(
    private val appearanceController: AppearanceController,
    private val releaseNotesController: ReleaseNotesController
): NavigationManager, StatisticsExternalNavigationManager {

    //region NavigationManager

    override fun getAboutThisAppIntent(context: Context): Intent {
        return AboutThisAppActivity.intent(
                context = context,
                configuration = AboutThisAppConfig.configuration(context, !appearanceController.currentTheme.isLightMode(context))
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

    //endregion

    override fun getRSSIntent(context: Context): Intent {
        return Intent(context, RSSActivity::class.java)
    }

    //endregion
}