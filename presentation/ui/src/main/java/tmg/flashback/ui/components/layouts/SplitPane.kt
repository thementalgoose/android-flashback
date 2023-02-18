@file:OptIn(ExperimentalMaterial3WindowSizeClassApi::class)

package tmg.flashback.ui.components.layouts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.window.layout.WindowLayoutInfo
import tmg.flashback.style.AppThemePreview

@Composable
fun SplitPane(
    windowSizeClass: WindowSizeClass,
    windowLayoutInfo: WindowLayoutInfo,
    modifier: Modifier = Modifier,
    master: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact,
        WindowWidthSizeClass.Medium -> {
            content()
        }
        WindowWidthSizeClass.Expanded -> {
            Row {
                Box(Modifier.weight(1f)) {
                    master?.invoke()
                }
                Box(Modifier.weight(1f)) {
                    content()
                }
            }
        }
    }
}

@Composable
fun SplitPane(
    windowSizeClass: WindowSizeClass,
    windowLayoutInfo: WindowLayoutInfo,
    modifier: Modifier = Modifier,
    master: @Composable () -> Unit
) {
    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact,
        WindowWidthSizeClass.Medium -> {
            master()
        }
        WindowWidthSizeClass.Expanded -> {
            Row {
                Box(Modifier.weight(1f)) {
                    master()
                }
                Box(Modifier.weight(1f))
            }
        }
    }
}

@Preview
@Composable
private fun PreviewCompact() {
    AppThemePreview {
        SplitPane(
            windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(400.dp, 400.dp)),
            windowLayoutInfo = WindowLayoutInfo(emptyList()),
            master = { Box(Modifier.fillMaxSize().background(Color.Green)) },
            content = { Box(Modifier.fillMaxSize().background(Color.Cyan)) },
        )
    }
}

@Preview
@Composable
private fun PreviewMedium() {
    AppThemePreview {
        SplitPane(
            windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(680.dp, 400.dp)),
            windowLayoutInfo = WindowLayoutInfo(emptyList()),
            master = { Box(Modifier.fillMaxSize().background(Color.Green)) },
            content = { Box(Modifier.fillMaxSize().background(Color.Cyan)) },
        )
    }
}

@Preview
@Composable
private fun PreviewExpanded() {
    AppThemePreview {
        SplitPane(
            windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(900.dp, 400.dp)),
            windowLayoutInfo = WindowLayoutInfo(emptyList()),
            master = { Box(Modifier.fillMaxSize().background(Color.Green)) },
            content = { Box(Modifier.fillMaxSize().background(Color.Cyan)) },
        )
    }
}

@Preview
@Composable
private fun PreviewCompactMaster() {
    AppThemePreview {
        SplitPane(
            windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(400.dp, 400.dp)),
            windowLayoutInfo = WindowLayoutInfo(emptyList()),
            master = { Box(Modifier.fillMaxSize().background(Color.Green)) },
        )
    }
}

@Preview
@Composable
private fun PreviewMediumMaster() {
    AppThemePreview {
        SplitPane(
            windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(680.dp, 400.dp)),
            windowLayoutInfo = WindowLayoutInfo(emptyList()),
            master = { Box(Modifier.fillMaxSize().background(Color.Green)) },
        )
    }
}

@Preview
@Composable
private fun PreviewExpandedMaster() {
    AppThemePreview {
        SplitPane(
            windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(900.dp, 400.dp)),
            windowLayoutInfo = WindowLayoutInfo(emptyList()),
            master = { Box(Modifier.fillMaxSize().background(Color.Green)) },
        )
    }
}