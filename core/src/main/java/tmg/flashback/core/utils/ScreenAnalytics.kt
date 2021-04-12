package tmg.flashback.core.utils

/**
 * Data class to hold potential data reported while logging
 * screen viewership analytics
 */
data class ScreenAnalytics(
    val screenName: String? = null,
    val attributes: Map<String, String> = emptyMap()
)