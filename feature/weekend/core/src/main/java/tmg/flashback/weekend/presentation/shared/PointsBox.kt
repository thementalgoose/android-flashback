package tmg.flashback.weekend.presentation.shared

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import tmg.flashback.formula1.extensions.pointsDisplay
import tmg.flashback.style.AppTheme
import tmg.flashback.style.text.TextBody1
import tmg.flashback.ui.utils.pluralResource
import tmg.flashback.weekend.R
import kotlin.math.roundToInt


internal val pointsWidth: Dp = 64.dp

@Composable
fun PointsBox(
    points: Double,
    colour: Color,
    modifier: Modifier = Modifier,
    maxPoints: Double = 60.0
) {
    val pointsLabel = pluralResource(resId = R.plurals.race_points, quantity = points.roundToInt(), points.pointsDisplay())
    Box(modifier = modifier
        .width(pointsWidth)
        .padding(top = AppTheme.dimens.medium)
        .clearAndSetSemantics {
            this.contentDescription = pointsLabel
        }
    ) {
        TextBody1(
            modifier = modifier.align(Alignment.Center),
            text = points.pointsDisplay(),
        )
    }
//    val progress = (points / maxPoints).toFloat().coerceIn(0f, 1f)
//
//    ProgressBar(
//        modifier = modifier
//            .width(maxWidth)
//            .height(maxHeight),
//        endProgress = progress,
//        barColor = colour,
//        label = {
//            when (it) {
//                0f -> "0"
//                progress -> points.pointsDisplay()
//                else -> (it * maxPoints).takeIf { !it.isNaN() }?.roundToInt()?.toString() ?: points.pointsDisplay()
//            }
//        }
//    )
}