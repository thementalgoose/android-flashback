package tmg.flashback.constants

import tmg.core.analytics.manager.AnalyticsManager

enum class ViewType(
    val key: String
) {
    FORCE_UPGRADE("view_force_upgrade"),
    MAINTENANCE("view_maintenance"),
    SETTINGS_ALL("view_settings_all"),
    SETTINGS_ABOUT("view_settings_about"),
    SETTINGS_CUSTOMISATION_ANIMATION("view_settings_customisation_animation"),
    SETTINGS_CUSTOMISATION_THEME("view_settings_customisation_theme"),
    SETTINGS_CUSTOMISATION("view_settings_customisation"),
    SETTINGS_DEVICE("view_settings_device"),
    SETTINGS_NOTIFICATIONS("view_settings_notifications"),
    SETTINGS_RELEASE_NOTES("view_settings_release_notes"),
    SETTINGS_PRIVACY_POLICY("view_settings_privacy_policy"),
    SETTINGS_WIDGET("view_settings_widget"),
}

fun AnalyticsManager.logEvent(type: ViewType, attributes: Map<String, String> = emptyMap()) {
    this.logEvent(type.key, attributes)
}