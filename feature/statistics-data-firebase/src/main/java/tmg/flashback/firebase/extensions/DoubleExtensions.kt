package tmg.flashback.firebase.extensions

import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import kotlin.math.roundToInt

/**
 * Print points as a legible string
 */
fun Double.pointsDisplay(): String {
    val formatter = DecimalFormatSymbols.getInstance()
    formatter.decimalSeparator = '.'
    formatter.monetaryDecimalSeparator = '.'
    val df = DecimalFormat("#.#")
    df.roundingMode = RoundingMode.CEILING
    var result = df.format(this)
    if (result.contains(',')) {
        result = result.replace(',', '.')
    }
    return if (result.endsWith(".5")) {
        result
    }
    else {
        result.toDoubleOrNull()?.roundToInt()?.toString() ?: this.toString()
    }
}