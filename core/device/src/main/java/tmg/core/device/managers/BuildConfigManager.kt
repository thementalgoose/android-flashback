package tmg.core.device.managers

/**
 * Wrapper around the BuildConfig that's generated at compile time
 * Abstracted for testing
 */
interface BuildConfigManager {

    val applicationId: String

    val versionCode: Int

    val versionName: String

    val isNotificationChannelsSupported: Boolean

    val isMonetThemeSupported: Boolean

    val isAppShortcutsSupported: Boolean
}