package tmg.flashback.ui.components.layouts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import tmg.flashback.style.AppThemePreview
import tmg.flashback.ui.annotations.PreviewFoldable
import tmg.flashback.ui.annotations.PreviewPhone
import tmg.flashback.ui.annotations.PreviewTablet
import tmg.flashback.ui.utils.fakeCompactWindowSizeClass
import tmg.flashback.ui.utils.fakeExpandedWindowSizeClass
import tmg.flashback.ui.utils.fakeMediumWindowSizeClass

@Composable
fun SplitPane(
    windowSizeClass: WindowSizeClass,
    main: @Composable BoxScope.() -> Unit,
    modifier: Modifier = Modifier,
    contentExpandedFallback: @Composable (BoxScope.() -> Unit) = { },
    content: @Composable (BoxScope.() -> Unit)? = null,
) {
    Box(modifier = modifier) {
        Row(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.weight(4f)) {
                main()
            }
            if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded) {
                Box(modifier = Modifier.weight(5f)) {
                    if (content != null) {
                        content()
                    } else {
                        contentExpandedFallback()
                    }
                }
            }
        }
        if (windowSizeClass.widthSizeClass != WindowWidthSizeClass.Expanded && content != null) {
            Box(modifier = Modifier.fillMaxSize()) {
                content()
            }
        }
    }
}

@PreviewTablet
@Composable
private fun PreviewNoContentExpanded() {
    AppThemePreview {
        SplitPane(
            windowSizeClass = fakeExpandedWindowSizeClass,
            main = {
                Box(Modifier.fillMaxSize().background(Color.Blue))
            }
        )
    }
}

@PreviewFoldable
@Composable
private fun PreviewNoContentMedium() {
    AppThemePreview {
        SplitPane(
            windowSizeClass = fakeMediumWindowSizeClass,
            main = {
                Box(Modifier.fillMaxSize().background(Color.Blue))
            }
        )
    }
}

@PreviewPhone
@Composable
private fun PreviewNoContentCompact() {
    AppThemePreview {
        SplitPane(
            windowSizeClass = fakeCompactWindowSizeClass,
            main = {
                Box(Modifier.fillMaxSize().background(Color.Blue))
            }
        )
    }
}


@PreviewTablet
@Composable
private fun PreviewWithContentExpanded() {
    AppThemePreview {
        SplitPane(
            windowSizeClass = fakeExpandedWindowSizeClass,
            main = {
                Box(Modifier.fillMaxSize().background(Color.Blue))
            },
            content = {
                Box(Modifier.fillMaxSize().background(Color.Magenta))
            }
        )
    }
}


@PreviewFoldable
@Composable
private fun PreviewWithContentMedium() {
    AppThemePreview {
        SplitPane(
            windowSizeClass = fakeMediumWindowSizeClass,
            main = {
                Box(Modifier.fillMaxSize().background(Color.Blue))
            },
            content = {
                Box(Modifier.fillMaxSize().background(Color.Magenta))
            }
        )
    }
}

@PreviewPhone
@Composable
private fun PreviewWithContentCompact() {
    AppThemePreview {
        SplitPane(
            windowSizeClass = fakeCompactWindowSizeClass,
            main = {
                Box(Modifier.fillMaxSize().background(Color.Blue))
            },
            content = {
                Box(Modifier.fillMaxSize().background(Color.Magenta))
            }
        )
    }
}
