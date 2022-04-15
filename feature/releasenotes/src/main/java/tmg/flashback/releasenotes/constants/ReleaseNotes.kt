package tmg.flashback.releasenotes.constants

import androidx.annotation.StringRes
import tmg.flashback.releasenotes.R

/** 
 * This file is auto generated! Please do not modify! 
 */
enum class ReleaseNotes(
        val version: Int,
        val isMajor: Boolean = false,
        val versionName: String,
        @StringRes
        val title: Int,
        @StringRes
        val release: Int
) {
    VERSION_2(version = 2, isMajor = true, versionName = "1.x.x", title = R.string.release_2_title, release = R.string.release_2),
    VERSION_4(version = 4, isMajor = true, versionName = "2.x.x", title = R.string.release_4_title, release = R.string.release_4),
    VERSION_27(version = 27, isMajor = false, versionName = "4.x.x", title = R.string.release_27_title, release = R.string.release_27),
    VERSION_57(version = 57, isMajor = false, versionName = "5.x.x", title = R.string.release_57_title, release = R.string.release_57),
    VERSION_110(version = 110, isMajor = false, versionName = "6.x.x", title = R.string.release_110_title, release = R.string.release_110),
    VERSION_131(version = 131, isMajor = false, versionName = "7.x.x", title = R.string.release_131_title, release = R.string.release_131),
    VERSION_154(version = 154, isMajor = false, versionName = "8.0.154", title = R.string.release_154_title, release = R.string.release_154),
    VERSION_160(version = 160, isMajor = false, versionName = "8.1.160", title = R.string.release_160_title, release = R.string.release_160),
    VERSION_180(version = 180, isMajor = false, versionName = "8.2.183", title = R.string.release_180_title, release = R.string.release_180),

}
