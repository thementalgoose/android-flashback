package tmg.f1stats.utils

import android.content.Context
import android.content.res.Resources
import androidx.annotation.DrawableRes
import tmg.f1stats.R

@DrawableRes
fun Context.getFlagResourceAlpha3(flag: String): Int {
    return getFlagResource(flag.toAlpha2ISO())
}

@DrawableRes
fun Context.getFlagResource(flag: String?): Int {
    if (flag == null) {
        return R.drawable.nationality_not_found
    }
    return resources.getIdentifier(flag.toLowerCase(), "drawable", packageName)
}