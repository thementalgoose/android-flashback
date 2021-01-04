package tmg.flashback.constants

import androidx.annotation.StringRes
import tmg.flashback.R

/**
 * Release notes
 */
val releaseNotes: List<ReleaseNotes> = listOf(
        ReleaseNotes(1, R.string.release_1),
        ReleaseNotes(2, R.string.release_2),
        ReleaseNotes(4, R.string.release_4),
        ReleaseNotes(6, R.string.release_6),
        ReleaseNotes(7, R.string.release_7),
        ReleaseNotes(8, R.string.release_8),
        ReleaseNotes(10, R.string.release_10),
        ReleaseNotes(13, R.string.release_13),
        ReleaseNotes(14, R.string.release_14),
        ReleaseNotes(15, R.string.release_15),
        ReleaseNotes(16, R.string.release_16),
        ReleaseNotes(18, R.string.release_18),
        ReleaseNotes(20, R.string.release_20),
        ReleaseNotes(21, R.string.release_21),
        ReleaseNotes(22, R.string.release_22),
        ReleaseNotes(23, R.string.release_23),
        ReleaseNotes(24, R.string.release_24),
        ReleaseNotes(25, R.string.release_25),
        ReleaseNotes(26, R.string.release_26),
        ReleaseNotes(27, R.string.release_27),
        ReleaseNotes(28, R.string.release_28),
        ReleaseNotes(30, R.string.release_30),
        ReleaseNotes(31, R.string.release_31),
        ReleaseNotes(32, R.string.release_32),
        ReleaseNotes(33, R.string.release_33),
        ReleaseNotes(34, R.string.release_34),
        ReleaseNotes(36, R.string.release_36),
        ReleaseNotes(38, R.string.release_38),
        ReleaseNotes(40, R.string.release_40),
)

data class ReleaseNotes(
        val versionCode: Int,
        @StringRes
        val releaseText: Int,
        val worthShowingDialogFor: Boolean = false
)