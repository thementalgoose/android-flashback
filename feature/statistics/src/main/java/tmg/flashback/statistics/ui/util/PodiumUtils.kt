package tmg.flashback.statistics.ui.util

import android.content.Context
import tmg.flashback.statistics.R
import tmg.utilities.extensions.ordinalAbbreviation

fun Int.position(): String {
    return this.ordinalAbbreviation
}

fun Int.positionStarted(context: Context): String {
    return context.getString(R.string.round_podium_started, this.position())
}