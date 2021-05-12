package tmg.flashback.rss.constants

import tmg.analytics.manager.AnalyticsManager

enum class ViewType(
    val key: String
) {
    RSS("view_rss"),
    SETTINGS_RSS("view_settings_rss"),
    SETTINGS_RSS_CONFIGURE("view_settings_rss_configure")
}

fun AnalyticsManager.logEvent(type: ViewType, attributes: Map<String, String> = emptyMap()) {
    this.logEvent(type.key, attributes)
}