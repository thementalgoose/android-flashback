package tmg.flashback.stats.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import tmg.flashback.stats.R
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.text.TextBody1

private val dotDiameter: Dp = 22.dp
private val cutoutDiameter: Dp = 10.dp
private val pipeWidth: Dp = 10.dp

@Composable
fun Timeline(
    timelineColor: Color,
    isEnabled: Boolean,
    modifier: Modifier = Modifier,
    backgroundColor: Color = AppTheme.colors.backgroundContainer,
    showTop: Boolean = true,
    showBottom: Boolean = true,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = modifier.height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier
            .width(dotDiameter)
            .defaultMinSize(minWidth = dotDiameter, minHeight = dotDiameter + 16.dp)
            .fillMaxHeight()) {

            Column(modifier = Modifier
                .width(pipeWidth)
                .fillMaxHeight()
                .offset((dotDiameter - pipeWidth) / 2)) {
                Box(modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(if (showTop) timelineColor else Color.Transparent))
                Box(modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(if (showBottom) timelineColor else Color.Transparent))
            }
            Box(modifier = Modifier
                .width(dotDiameter)
                .height(dotDiameter)
                .clip(CircleShape)
                .background(timelineColor)
                .align(Alignment.Center))
            if (!isEnabled) {
                Box(modifier = Modifier
                    .width(cutoutDiameter)
                    .height(cutoutDiameter)
                    .clip(CircleShape)
                    .background(backgroundColor)
                    .align(Alignment.Center)
                )
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        content()
    }
}

@Preview
@Composable
private fun PreviewLight() {
    AppThemePreview(isLight = true) {
        PreviewTimelines()
    }
}

@Preview
@Composable
private fun PreviewDark() {
    AppThemePreview(isLight = false) {
        PreviewTimelines()
    }
}

@Composable
private fun PreviewTimelines() {
    Column(modifier = Modifier.width(250.dp)) {
        Timeline(
            timelineColor = Color.Magenta,
            isEnabled = false,
            showTop = false,
            content = {
                TextBody1(text = "2022")
            }
        )
        Timeline(
            timelineColor = Color.Magenta,
            isEnabled = false,
            content = {
                TextBody1(
                    text = "2021",
                    modifier = Modifier.fillMaxWidth()
                )
            }
        )
        Timeline(
            timelineColor = Color.Magenta,
            isEnabled = true,
            content = {
                TextBody1(text = "2020")
            }
        )
        Timeline(
            timelineColor = Color.Blue,
            isEnabled = false,
            showBottom = false,
            content = {
                TextBody1(text = "2019")
            }
        )
        Timeline(
            timelineColor = Color.Blue,
            isEnabled = false,
            showTop = false,
            content = {
                TextBody1(text = "2018")
            }
        )
        Timeline(
            timelineColor = Color.Blue,
            isEnabled = false,
            showBottom = false,
            content = {
                TextBody1(text = "2017")
            }
        )
    }
}