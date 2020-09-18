package tmg.flashback.utils

import android.content.Context
import android.text.Spanned
import tmg.flashback.R
import tmg.utilities.extensions.fromHtml
import tmg.utilities.extensions.ordinalAbbreviation

fun Int.position(): String {
    return this.ordinalAbbreviation
}

fun Int.positionStarted(context: Context): String {
    return context.getString(R.string.round_podium_started, this.position())
}