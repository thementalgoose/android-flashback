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

    val isDebug: Boolean

    val brand: String

    val hardware: String

    val board: String

    val fingerprint: String

    val model: String

    val manufacturer: String

    val product: String

    val device: String
}