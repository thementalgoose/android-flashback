package tmg.flashback.constants

import tmg.flashback.core.enums.AnimationSpeed
import tmg.flashback.core.enums.Theme

internal object Defaults {

    const val showQualifyingDelta: Boolean = false
    const val fadeDNF: Boolean = true
    const val showGridPenaltiesInQualifying: Boolean = true

    val animationSpeed: AnimationSpeed = AnimationSpeed.MEDIUM
    val theme: Theme = Theme.AUTO

    const val showListFavourited: Boolean = true
    const val showListAll: Boolean = true

    const val crashReporting: Boolean = true
    const val shakeToReport: Boolean = true
    const val analyticsOptIn: Boolean = true
}