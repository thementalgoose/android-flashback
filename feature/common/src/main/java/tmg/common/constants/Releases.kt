package tmg.common.constants

import androidx.annotation.StringRes
import tmg.common.R

enum class Releases(
        val version: Int,
        val isMajor: Boolean = false,
        @StringRes
        val release: Int
) {
        VERSION_1(version = 1, isMajor = true, release = R.string.release_1),
        VERSION_2(version = 2, isMajor = false, release = R.string.release_2),
        VERSION_4(version = 4, isMajor = true, release = R.string.release_4),
        VERSION_6(version = 6, isMajor = true, release = R.string.release_6),
        VERSION_7(version = 7, isMajor = true, release = R.string.release_7),
        VERSION_8(version = 8, isMajor = true, release = R.string.release_8),
        VERSION_10(version = 10, isMajor = true, release = R.string.release_10),
        VERSION_13(version = 13, isMajor = true, release = R.string.release_13),
        VERSION_14(version = 14, isMajor = false, release = R.string.release_14),
        VERSION_15(version = 15, isMajor = true, release = R.string.release_15),
        VERSION_16(version = 16, isMajor = true, release = R.string.release_16),
        VERSION_18(version = 18, isMajor = false, release = R.string.release_18),
        VERSION_20(version = 20, isMajor = false, release = R.string.release_20),
        VERSION_21(version = 21, isMajor = true, release = R.string.release_21),
        VERSION_22(version = 22, isMajor = false, release = R.string.release_22),
        VERSION_23(version = 23, isMajor = false, release = R.string.release_23),
        VERSION_24(version = 24, isMajor = false, release = R.string.release_24),
        VERSION_25(version = 25, isMajor = false, release = R.string.release_25),
        VERSION_26(version = 26, isMajor = false, release = R.string.release_26),
        VERSION_27(version = 27, isMajor = false, release = R.string.release_27),
        VERSION_28(version = 28, isMajor = true, release = R.string.release_28),
        VERSION_30(version = 30, isMajor = false, release = R.string.release_30),
        VERSION_31(version = 31, isMajor = true, release = R.string.release_31),
        VERSION_32(version = 32, isMajor = true, release = R.string.release_32),
        VERSION_33(version = 33, isMajor = false, release = R.string.release_33),
        VERSION_34(version = 34, isMajor = false, release = R.string.release_34),
        VERSION_36(version = 36, isMajor = false, release = R.string.release_36),
        VERSION_38(version = 38, isMajor = false, release = R.string.release_38),
        VERSION_40(version = 40, isMajor = true, release = R.string.release_40),
        VERSION_45(version = 45, isMajor = false, release = R.string.release_45),
        VERSION_47(version = 47, isMajor = true, release = R.string.release_47),
        VERSION_48(version = 48, isMajor = false, release = R.string.release_48),
        VERSION_50(version = 50, isMajor = false, release = R.string.release_50),
        VERSION_53(version = 53, isMajor = false, release = R.string.release_53),
        VERSION_54(version = 54, isMajor = false, release = R.string.release_54),
        VERSION_57(version = 57, isMajor = false, release = R.string.release_57),
        VERSION_65(version = 65, isMajor = true, release = R.string.release_65);
}