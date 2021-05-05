package tmg.flashback.constants

import tmg.flashback.shared.ui.model.AnimationSpeed
import tmg.flashback.shared.ui.model.Theme

internal object Defaults {

    const val showQualifyingDelta: Boolean = false
    const val fadeDNF: Boolean = true
    const val showGridPenaltiesInQualifying: Boolean = true

    val animationSpeed: AnimationSpeed = AnimationSpeed.MEDIUM
    val theme: Theme = Theme.DEFAULT

    const val showListFavourited: Boolean = true
    const val showListAll: Boolean = true

    const val widgetOpenApp: Boolean = true

    const val crashReporting: Boolean = true
    const val shakeToReport: Boolean = true
    const val analyticsOptIn: Boolean = true
}