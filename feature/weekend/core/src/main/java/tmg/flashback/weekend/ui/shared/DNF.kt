package tmg.flashback.weekend.ui.shared

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import tmg.flashback.formula1.enums.RaceStatus
import tmg.flashback.formula1.enums.isStatusFinished

fun Modifier.status(status: RaceStatus): Modifier {
    val alphaValue = when (status.isStatusFinished()) {
        true -> 1f
        else -> 0.7f
    }
    return this.alpha(alphaValue)
}