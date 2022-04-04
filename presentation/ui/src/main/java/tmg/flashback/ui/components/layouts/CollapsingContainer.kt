package tmg.flashback.ui.components.layouts

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.text.TextTitle
import tmg.flashback.ui.R

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CollapsingContainer(
    title: String,
    modifier: Modifier = Modifier,
    initiallyExpanded: Boolean = false,
    content: @Composable () -> Unit
) {
    val expanded = remember { mutableStateOf(initiallyExpanded) }
    val rotation = animateFloatAsState(targetValue = if (expanded.value) 180f else 0f)

    Container(
        modifier = modifier,
        internalPadding = 0.dp,
        externalPadding = 0.dp
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .clickable(onClick = {
                        expanded.value = !expanded.value
                    })
                    .padding(
                        horizontal = AppTheme.dimensions.paddingMedium,
                        vertical = AppTheme.dimensions.paddingNSmall
                    )
                    .clip(RoundedCornerShape(AppTheme.dimensions.radiusSmall))
                    .background(AppTheme.colors.backgroundPrimary)
            ) {
                TextTitle(
                    text = title,
                    modifier = Modifier.weight(1f)
                )
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(AppTheme.colors.backgroundSecondary)
                        .padding(4.dp)
                ) {
                    Icon(
                        tint = AppTheme.colors.contentPrimary,
                        painter = painterResource(id = R.drawable.arrow_down),
                        contentDescription = null,
                        modifier = Modifier
                            .rotate(degrees = rotation.value)
                    )
                }
            }
            AnimatedContent(
                modifier = modifier,
                targetState = expanded,
                content = {
                    if (expanded.value) {
                        content()
                    }
                }
            )
        }
    }
}

@Preview
@Composable
private fun PreviewLightCollapsed() {
    AppThemePreview(isLight = true) {
        CollapsingContainer(
            title = "Schedule",
            initiallyExpanded = false
        ) {
            Box(modifier = Modifier
                .background(Color.Green)
                .size(100.dp)
            )
        }
    }
}

@Preview
@Composable
private fun PreviewLightExpanded() {
    AppThemePreview(isLight = true) {
        CollapsingContainer(
            title = "Schedule",
            initiallyExpanded = true
        ) {
            Box(modifier = Modifier
                .background(Color.Green)
                .size(100.dp)
            )
        }
    }
}

@Preview
@Composable
private fun PreviewDarkCollapsed() {
    AppThemePreview(isLight = false) {
        CollapsingContainer(
            title = "Schedule",
            initiallyExpanded = false
        ) {
            Box(modifier = Modifier
                .background(Color.Green)
                .size(100.dp)
            )
        }
    }
}

@Preview
@Composable
private fun PreviewDarkExpanded() {
    AppThemePreview(isLight = false) {
        CollapsingContainer(
            title = "Schedule",
            initiallyExpanded = true
        ) {
            Box(modifier = Modifier
                .background(Color.Green)
                .size(100.dp)
            )
        }
    }
}