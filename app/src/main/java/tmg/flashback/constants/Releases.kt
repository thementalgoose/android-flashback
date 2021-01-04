package tmg.flashback.constants

import androidx.annotation.StringRes
import tmg.flashback.R

enum class Releases(
        val version: Int,
        @StringRes
        val release: Int,
        val isMajor: Boolean = false
) {
        VERSION_1(version = 1, release = R.string.release_1, isMajor = true),
        VERSION_2(version = 2, release = R.string.release_2, isMajor = false),
        VERSION_4(version = 4, release = R.string.release_4, isMajor = true),
        VERSION_6(version = 6, release = R.string.release_6, isMajor = true),
        VERSION_7(version = 7, release = R.string.release_7, isMajor = true),
        VERSION_8(version = 8, release = R.string.release_8, isMajor = true),
        VERSION_10(version = 10, release = R.string.release_10, isMajor = true),
        VERSION_13(version = 13, release = R.string.release_13, isMajor = true),
        VERSION_14(version = 14, release = R.string.release_14, isMajor = false),
        VERSION_15(version = 15, release = R.string.release_15, isMajor = true),
        VERSION_16(version = 16, release = R.string.release_16, isMajor = true),
        VERSION_18(version = 18, release = R.string.release_18, isMajor = false),
        VERSION_20(version = 20, release = R.string.release_20, isMajor = false),
        VERSION_21(version = 21, release = R.string.release_21, isMajor = true),
        VERSION_22(version = 22, release = R.string.release_22, isMajor = false),
        VERSION_23(version = 23, release = R.string.release_23, isMajor = false),
        VERSION_24(version = 24, release = R.string.release_24, isMajor = false),
        VERSION_25(version = 25, release = R.string.release_25, isMajor = false),
        VERSION_26(version = 26, release = R.string.release_26, isMajor = false),
        VERSION_27(version = 27, release = R.string.release_27, isMajor = false),
        VERSION_28(version = 28, release = R.string.release_28, isMajor = true),
        VERSION_30(version = 30, release = R.string.release_30, isMajor = false),
        VERSION_31(version = 31, release = R.string.release_31, isMajor = true),
        VERSION_32(version = 32, release = R.string.release_32, isMajor = true),
        VERSION_33(version = 33, release = R.string.release_33, isMajor = false),
        VERSION_34(version = 34, release = R.string.release_34, isMajor = false),
        VERSION_36(version = 36, release = R.string.release_36, isMajor = false),
        VERSION_38(version = 38, release = R.string.release_38, isMajor = false),
        VERSION_40(version = 40, release = R.string.release_40, isMajor = true);
}