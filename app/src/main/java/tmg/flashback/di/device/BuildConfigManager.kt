package tmg.flashback.di.device

interface BuildConfigManager {

    val versionCode: Int
    val versionName: String

    fun shouldLockoutBasedOnVersion(version: Int?): Boolean {
        return version != null && (version >= versionCode)
    }

}