package tmg.flashback.googleanalytics

enum class UserProperty(
    val key: String
) {
    APP_VERSION("app_version"),
    OS_VERSION("os_version"),
    DEVICE_MODEL("device_model"),
    DEVICE_BOARD("device_board"),
    DEVICE_BRAND("device_brand"),
    DEVICE_MANUFACTURER("device_manufacturer"),
    DEVICE_THEME("device_theme"),
    WIDGET_USAGE("using_widget")
}