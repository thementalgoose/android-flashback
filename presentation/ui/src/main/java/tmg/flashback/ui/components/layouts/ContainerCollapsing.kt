package tmg.flashback.ui.components.layouts

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.text.TextTitle
import tmg.flashback.ui.R

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ContainerCollapsing(
    title: String,
    modifier: Modifier = Modifier,
    boxRadius: Dp = AppTheme.dimensions.radiusSmall,
    initiallyExpanded: Boolean = false,
    content: @Composable () -> Unit
) {
    val expanded = remember { mutableStateOf(initiallyExpanded) }
    val rotation = animateFloatAsState(targetValue = if (expanded.value) 180f else 0f)

    Container(
        modifier = modifier,
        boxRadius = boxRadius,
        isOutlined = true
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

@PreviewTheme
@Composable
private fun PreviewCollapsed() {
    AppThemePreview {
        ContainerCollapsing(
            modifier = Modifier.padding(16.dp),
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

@PreviewTheme
@Composable
private fun PreviewExpanded() {
    AppThemePreview {
        ContainerCollapsing(
            modifier = Modifier.padding(16.dp),
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