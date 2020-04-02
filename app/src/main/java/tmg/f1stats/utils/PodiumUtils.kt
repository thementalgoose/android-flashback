package tmg.f1stats.utils

import android.content.Context
import android.text.Spanned
import android.text.SpannedString
import tmg.f1stats.R
import tmg.utilities.extensions.fromHtml

fun Int.position(podiumOnly: Boolean = false): Spanned {
    return when (this) {
        1 -> "1<sup>st</sup>".fromHtml()
        2 -> "2<sup>nd</sup>".fromHtml()
        3 -> "3<sup>rd</sup>".fromHtml()
        else -> if (podiumOnly) "".fromHtml() else "$this<sup>th</sup>".fromHtml()
    }
}

fun Int.podium(): Spanned {
    return when (this) {
        1 -> "1<sup>st</sup>".fromHtml()
        2 -> "2<sup>nd</sup>".fromHtml()
        3 -> "3<sup>rd</sup>".fromHtml()
        else -> "$this<sup>th</sup>".fromHtml()
    }
}

fun Int.positionStarted(context: Context): String {
    return context.getString(R.string.round_podium_started, this.position())
}