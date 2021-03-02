package tmg.flashback.constants

import org.threeten.bp.Year
import tmg.flashback.BuildConfig

object App {

    /**
     * Get the current year according to the device.
     * Used as a fallback if remote config fails
     */
    val currentYear: Int
        get() = Year.now().value

    /**
     * Play store URL
     */
    const val playStoreUrl: String = "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID
}