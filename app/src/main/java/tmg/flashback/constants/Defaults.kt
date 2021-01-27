package tmg.flashback.constants

import tmg.flashback.repo.enums.BarAnimation
import tmg.flashback.repo.enums.ThemePref

internal object Defaults {

    const val showQualifyingDelta: Boolean = false
    const val fadeDNF: Boolean = true
    const val showGridPenaltiesInQualifying: Boolean = true

    val barAnimation: BarAnimation = BarAnimation.MEDIUM
    val theme: ThemePref = ThemePref.AUTO

    const val showListFavourited: Boolean = true
    const val showListAll: Boolean = true

    const val crashReporting: Boolean = true
    const val shakeToReport: Boolean = true
    const val analyticsOptIn: Boolean = true
}