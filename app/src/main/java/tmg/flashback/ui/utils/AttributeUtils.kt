package tmg.flashback.ui.utils

import android.content.res.Resources
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt

@ColorInt
fun Resources.Theme.getColor(@AttrRes attribute: Int): Int {
    val typedValue = TypedValue()
    resolveAttribute(attribute, typedValue, true)
    return typedValue.data
}