package tmg.flashback.statistics.controllers

import android.os.Build
import tmg.configuration.controllers.ConfigController

/**
 * Control the notifications in the app / letting the user know something
 */
class UserNotificationController(
    private val configurationController: ConfigController
) {

    /**
     * Get the banner that we display to the user
     */
    val banner: String?
        get() = configurationController.banner

    /**
     * Notifications channels supported
     */
    val isNotificationChannelsSupported: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O

}