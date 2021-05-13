package tmg.flashback.statistics.constants

import tmg.core.analytics.manager.AnalyticsManager

enum class ViewType(
    val key: String
) {
    DRIVER("view_driver"),
    DRIVER_SEASON("view_driver_season"),
    CONSTRUCTOR("view_constructor"),
    CIRCUIT("view_circuit"),
    DASHBOARD_SEASON_CALENDAR("view_season_calendar"),
    DASHBOARD_SEASON_SCHEDULE("view_season_schedule"),
    DASHBOARD_SEASON_DRIVER("view_season_driver"),
    DASHBOARD_SEASON_CONSTRUCTOR("view_season_constructor"),
    SETTINGS_STATISTICS("view_settings_statistics")
}

fun AnalyticsManager.logEvent(type: ViewType, attributes: Map<String, String> = emptyMap()) {
    this.logEvent(type.key, attributes)
}