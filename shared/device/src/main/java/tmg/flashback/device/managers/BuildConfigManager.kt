package tmg.flashback.device.managers

/**
 * Wrapper around the BuildConfig that's generated at compile time
 * Abstracted for testing
 */
interface BuildConfigManager {

    val applicationId: String

    val versionCode: Int
    val versionName: String

    fun shouldLockoutBasedOnVersion(version: Int?): Boolean {
        return version == null || version >= versionCode || version <= 0
    }
}