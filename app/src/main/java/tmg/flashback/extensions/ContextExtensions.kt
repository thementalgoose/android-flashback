package tmg.flashback.extensions

import android.content.Context
import androidx.annotation.DimenRes

fun Context.dimensionPx(@DimenRes id: Int) = resources.getDimensionPixelSize(id).toFloat()

