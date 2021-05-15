package tmg.flashback.data.models

data class AppLockout(
    private val show: Boolean,
    val title: String,
    val message: String,
    val linkText: String?,
    val link: String?,
    val version: Int?
) {

    private fun shouldLockoutBasedOnVersion(versionCode: Int): Boolean {
        return version == null || version >= versionCode || version <= 0
    }

    fun showLockout(versionCode: Int): Boolean {
        return show && shouldLockoutBasedOnVersion(versionCode)
    }
}