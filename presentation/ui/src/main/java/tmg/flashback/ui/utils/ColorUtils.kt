package tmg.flashback.ui.utils

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt

fun Int.toColor() = Color(this)

fun String.hexToColor(): Color? = try {
    Color(this.toColorInt())
} catch (e: IllegalArgumentException) {
    null
}