package tmg.flashback.core.managers

import android.content.Context

/**
 * Delegate manager to be able access device settings screens from parts
 *  of the modules
 * Provided by the app module which will define format of settings etc.
 */
interface AppSettingsManager {
    fun openAboutThisApp(context: Context)
}