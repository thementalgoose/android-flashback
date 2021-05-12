package tmg.flashback.managers.navigation

import android.content.Context
import android.content.Intent
import androidx.fragment.app.FragmentActivity
import tmg.components.about.AboutThisAppActivity
import tmg.common.constants.AboutThisAppConfig
import tmg.common.controllers.ReleaseNotesController
import tmg.flashback.core.extensions.isLightMode
import tmg.flashback.core.managers.NavigationManager
import tmg.core.device.repositories.DeviceRepository
import tmg.flashback.rss.ui.RSSActivity
import tmg.flashback.statistics.manager.StatisticsExternalNavigationManager
import tmg.flashback.ui.admin.maintenance.MaintenanceActivity
import tmg.flashback.ui.settings.SettingsAllActivity
import tmg.flashback.ui.SplashActivity
import tmg.common.ui.releasenotes.ReleaseBottomSheetFragment

class FlashbackNavigationManager(
    private val appearanceController: AppearanceController,
    private val releaseNotesController: ReleaseNotesController,
    private val coreRepository: tmg.core.device.repositories.DeviceRepository
): NavigationManager, StatisticsExternalNavigationManager {

    //region NavigationManager

    override fun getAboutThisAppIntent(context: Context): Intent {
        return AboutThisAppActivity.intent(
                context = context,
                configuration = AboutThisAppConfig.configuration(
                    context = context,
                    isDarkMode = !appearanceController.currentTheme.isLightMode(context),
                    deviceUdid = coreRepository.deviceUdid
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

    //endregion

    override fun getRSSIntent(context: Context): Intent {
        return Intent(context, RSSActivity::class.java)
    }

    //endregion
}