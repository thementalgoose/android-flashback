package tmg.flashback.ui.components.progressbar

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.ui.R
import tmg.flashback.ui.utils.MeasureTextWidth
import tmg.flashback.ui.utils.pointsDisplay
import tmg.utilities.utils.ColorUtils
import tmg.utilities.utils.ColorUtils.Companion.contrastTextLight
import kotlin.math.roundToInt

@Composable
fun ProgressBar(
    endProgress: Float,
    label: (Float) -> String,
    modifier: Modifier = Modifier,
    initialValue: Float = 0f,
    animationDuration: Int = 400,
    textPadding: Dp = AppTheme.dimens.small,
    barColor: Color = AppTheme.colors.primary,
    barOnColor: Color =  when (contrastTextLight(barColor.toArgb())) {
        true -> Color.White
        false -> Color.Black
    },
    backgroundColor: Color = AppTheme.colors.backgroundPrimary,
    backgroundOnColor: Color = AppTheme.colors.contentPrimary,
) {
    val endProgress = if (endProgress.isNaN()) { 0f } else { endProgress }

    val contentDescription = pluralStringResource(id = R.plurals.race_points, count = endProgress.roundToInt(), endProgress.toDouble().pointsDisplay())
    BoxWithConstraints(
        modifier = modifier
            .defaultMinSize(minHeight = 48.dp)
            .fillMaxWidth()
            .clearAndSetSemantics {
                this.contentDescription = contentDescription
            }
            .clip(RoundedCornerShape(AppTheme.dimens.radiusSmall))
    ) {
        val progressState = remember { mutableStateOf(initialValue) }
        val progress = animateFloatAsState(
            visibilityThreshold = 0.0f,
            targetValue = progressState.value.coerceIn(0f, 1f),
            animationSpec = tween(
                durationMillis = animationDuration,
                easing = FastOutSlowInEasing,
                delayMillis = 0
            )
        ).value

        Box(
            modifier = Modifier
                .size(
                    width = maxWidth,
                    height = maxHeight
                )
                .background(backgroundColor)
        )

        Box(
            modifier = Modifier
                .size(
                    width = maxWidth * progress,
                    height = maxHeight
                )
                .clip(RoundedCornerShape(AppTheme.dimens.radiusSmall))
                .background(barColor)
        )

        MeasureTextWidth(
            text = label(endProgress.coerceIn(0f, 1f)),
            modifier = Modifier
                .align(Alignment.CenterStart)
        ) { textWidth ->

            val onBar = when {
                maxWidth * progress > ((textPadding * 2) + textWidth) -> true
                else -> false
            }

            Text(
                text = label(progress),
                style = AppTheme.typography.body1.copy(
                    color = when (onBar) {
                        true -> barOnColor
                        false -> backgroundOnColor
                    }
                ),
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .offset(
                        x = when (onBar) {
                            true -> (maxWidth * progress) - (textWidth + textPadding)
                            false -> (maxWidth * progress) + textPadding
                        }
                    )
            )
        }

        LaunchedEffect(endProgress.coerceIn(0f, 1f)) {
            progressState.value = endProgress
        }
    }
}

@Preview
@Composable
private fun Preview10() {
    AppThemePreview(isLight = true) {
        Box(modifier = Modifier.size(width = 180.dp, height = 40.dp)) {
            ProgressBar(
                endProgress = 0.1f,
                initialValue = 0.1f,
                label = { "$it" }
            )
        }
    }
}

@Preview
@Composable
private fun Preview50() {
    AppThemePreview(isLight = true) {
        Box(modifier = Modifier.size(width = 100.dp, height = 50.dp)) {
            ProgressBar(
                endProgress = 0.5f,
                initialValue = 0.5f,
                label = { "$it" }
            )
        }
    }
}


@Preview
@Composable
private fun Preview95() {
    AppThemePreview(isLight = true) {
        Box(modifier = Modifier.size(width = 100.dp, height = 30.dp)) {
            ProgressBar(
                endProgress = 0.95f,
                initialValue = 0.95f,
                label = { "$it" }
            )
        }
    }
}