package tmg.flashback.utils

import android.content.Context
import android.text.Spanned
import tmg.flashback.R
import tmg.utilities.extensions.fromHtml

fun Int.position(podiumOnly: Boolean = false): Spanned {
    if (podiumOnly) {
        return when (this) {
            1 -> "1st".fromHtml()
            2 -> "2nd".fromHtml()
            3 -> "3rd".fromHtml()
            else -> "".fromHtml()
        }
    }
    else {
        return when (this) {
            11 -> "11th".fromHtml()
            12 -> "12th".fromHtml()
            13 -> "13th".fromHtml()
            else -> {
                when (this.toString().lastOrNull()) {
                    '1' -> "${this}st".fromHtml()
                    '2' -> "${this}nd".fromHtml()
                    '3' -> "${this}rd".fromHtml()
                    null -> "".fromHtml()
                    else -> "${this}th".fromHtml()
                }
            }
        }
    }
}

fun Int.podium(): Spanned {
    return this.position(false)
}

fun Int.positionStarted(context: Context): String {
    return context.getString(R.string.round_podium_started, this.position())
}