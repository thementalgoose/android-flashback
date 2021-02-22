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

    /**
     * Colours for the decades for the menu
     */
    val coloursDecade: Map<String, String> = mapOf(
            "1950" to "#9fa8da",
            "1960" to "#80deea",
            "1970" to "#ef9a9a",
            "1980" to "#90caf9",
            "1990" to "#a5d6a7",
            "2000" to "#81d4fa",
            "2010" to "#ce93d8",
            "2020" to "#f48fb1",
            "2030" to "#b39ddb",
            "2040" to "#c5e1a5",
            "2050" to "#80cbc4",
            "2060" to "#b0bec5",
            "2070" to "#ffcc80",
            "2080" to "#ffab91",
            "2090" to "#b0bec5"
    )
}