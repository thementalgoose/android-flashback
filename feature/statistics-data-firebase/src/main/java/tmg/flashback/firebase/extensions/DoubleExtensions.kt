package tmg.flashback.firebase.extensions

import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.roundToInt

/**
 * Print points as a legible string
 */
fun Double.pointsDisplay(): String {
    val df = DecimalFormat("#.##")
    df.roundingMode = RoundingMode.CEILING
    val result = df.format(this)
    return if (result.endsWith(".5")) {
        result
    }
    else {
        result.toDouble().roundToInt().toString()
    }
}