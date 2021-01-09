package tmg.flashback.extensions

import android.graphics.Color
import kotlin.math.roundToInt

fun Int.brightness(factor: Float = 0.9f): Int {
    val a: Int = Color.alpha(this)
    val r = (Color.red(this) * factor).roundToInt()
    val g = (Color.green(this) * factor).roundToInt()
    val b = (Color.blue(this) * factor).roundToInt()
    return Color.argb(a, r.coerceAtMost(255), g.coerceAtMost(255), b.coerceAtMost(255))
}