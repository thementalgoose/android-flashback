package tmg.flashback.core.managers

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

/**
 * Delegate manager to be able access device settings screens from parts
 *  of the modules
 * Provided by the app module which will define format of settings etc.
 */
interface NavigationManager {
    // Splash Activity
    fun getAppStartupIntent(context: Context): Intent

    // Open about this app activity
    fun getAboutThisAppIntent(context: Context): Intent

    // Open the settings scren
    fun getSettingsIntent(context: Context): Intent

    // When there is a maintenance event, launch the maintenance window for the app
    fun getMaintenanceIntent(context: Context): Intent

    // When there is pending release notes, launch the release notes
    fun openContextualReleaseNotes(activity: AppCompatActivity)
}