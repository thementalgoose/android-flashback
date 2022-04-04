package tmg.flashback.ui.components.progressbar

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.ui.utils.MeasureTextWidth

@Composable
fun ProgressBar(
    endProgress: Float,
    label: (Float) -> String,
    modifier: Modifier = Modifier,
    initialValue: Float = 0f,
    animationDuration: Int = 400,
    textPadding: Dp = AppTheme.dimensions.paddingSmall,
    barColor: Color = AppTheme.colors.primary,
    barOnColor: Color = Color.White,
    backgroundColor: Color = AppTheme.colors.backgroundPrimary,
    backgroundOnColor: Color = AppTheme.colors.contentPrimary,
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(AppTheme.dimensions.radiusSmall))
    ) {
        val progressState = remember { mutableStateOf(initialValue) }
        val progress = animateFloatAsState(
            visibilityThreshold = 0.0f,
            targetValue = progressState.value,
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
                .clip(RoundedCornerShape(AppTheme.dimensions.radiusSmall))
                .background(barColor)
        )

            MeasureTextWidth(
                text = label(endProgress),
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

            LaunchedEffect(endProgress) {
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
