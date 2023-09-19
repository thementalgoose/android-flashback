package tmg.flashback.device.managers

/**
 * Wrapper around the BuildConfig that's generated at compile time
 * Abstracted for testing
 */
interface BuildConfigManager {

    val applicationId: String

    val versionCode: Int

    val versionName: String

    val isMonetThemeSupported: Boolean

    val isRuntimeNotificationsSupported: Boolean

    val isEmulator: Boolean
}