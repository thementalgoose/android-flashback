package tmg.flashback.di.device

interface BuildConfigProvider {

    val autoEnrolPushNotifications: Boolean

    val versionCode: Int
    val versionName: String

    fun shouldLockoutBasedOnVersion(version: Int?): Boolean {
        return version != null && (version >= versionCode)
    }

}