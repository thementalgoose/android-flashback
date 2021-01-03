package tmg.flashback.ui.utils

import android.content.Context
import androidx.annotation.DrawableRes
import tmg.flashback.R
import java.util.*

@DrawableRes
fun Context.getFlagResourceAlpha3(flag: String): Int {
    return getFlagResource(flag.toAlpha2ISO())
}

@DrawableRes
fun Context.getFlagResource(flag: String?): Int {
    if (flag == null) {
        return R.drawable.nationality_not_found
    }
    return resources.getIdentifier(flag.toLowerCase(Locale.UK), "drawable", packageName)
}