package tmg.flashback.managers.buildconfig

interface BuildConfigManager {

    val versionCode: Int
    val versionName: String

    fun shouldLockoutBasedOnVersion(version: Int?): Boolean {
        return version == null || version >= versionCode || version <= 0
    }
}