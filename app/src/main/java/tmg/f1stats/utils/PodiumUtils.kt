package tmg.f1stats.utils

import android.content.Context
import android.text.Spanned
import android.text.SpannedString
import tmg.f1stats.R
import tmg.utilities.extensions.fromHtml

fun Int.position(podiumOnly: Boolean = false): Spanned {
    return when (this) {
        1 -> "1st".fromHtml()
        2 -> "2nd".fromHtml()
        3 -> "3rd".fromHtml()
        else -> if (podiumOnly) "".fromHtml() else "${this}th".fromHtml()
    }
}

fun Int.podium(): Spanned {
    return when (this) {
        1 -> "1st".fromHtml()
        2 -> "2nd".fromHtml()
        3 -> "3rd".fromHtml()
        else -> "${this}th".fromHtml()
    }
}

fun Int.positionStarted(context: Context): String {
    return context.getString(R.string.round_podium_started, this.position())
}