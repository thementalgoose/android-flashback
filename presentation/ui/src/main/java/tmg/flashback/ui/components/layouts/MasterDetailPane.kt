package tmg.flashback.ui.components.layouts

import android.view.RoundedCorner
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.text.TextHeadline1
import tmg.flashback.ui.annotations.PreviewFoldable
import tmg.flashback.ui.annotations.PreviewPhone
import tmg.flashback.ui.annotations.PreviewTablet
import tmg.flashback.ui.utils.fakeCompactWindowSizeClass
import tmg.flashback.ui.utils.fakeExpandedWindowSizeClass
import tmg.flashback.ui.utils.fakeMediumWindowSizeClass

private val backgroundColor: Color
    @Composable get() = AppTheme.colors.backgroundPrimary

@Composable
fun MasterDetailPane(
    windowSizeClass: WindowSizeClass,
    main: @Composable BoxScope.() -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (BoxScope.() -> Unit)? = null,
    showContent: Boolean = content != null,
) {
    Box(modifier = modifier
        .background(backgroundColor)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.weight(4f)) {
                main()
            }
            if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded) {
                Box(modifier = Modifier
                    .weight(5f)
                    .padding(
                        vertical = AppTheme.dimens.medium,
                        horizontal = AppTheme.dimens.medium
                    )
                    .clip(RoundedCornerShape(AppTheme.dimens.radiusMedium))
                ) {
                    if (showContent) {
                        content?.invoke(this)
                    }
                }
            }
        }
        if (windowSizeClass.widthSizeClass != WindowWidthSizeClass.Expanded && showContent) {
            Box(modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
            ) {
                content?.invoke(this)
            }
        }
    }
}

@PreviewTablet
@Composable
private fun PreviewNoContentExpanded() {
    AppThemePreview {
        MasterDetailPane(
            windowSizeClass = fakeExpandedWindowSizeClass,
            main = {
                Box(Modifier.fillMaxSize()) {
                    TextHeadline1(text = "Main")
                }
            }
        )
    }
}

@PreviewFoldable
@Composable
private fun PreviewNoContentMedium() {
    AppThemePreview {
        MasterDetailPane(
            windowSizeClass = fakeMediumWindowSizeClass,
            main = {
                Box(Modifier.fillMaxSize()) {
                    TextHeadline1(text = "Main")
                }
            }
        )
    }
}

@PreviewPhone
@Composable
private fun PreviewNoContentCompact() {
    AppThemePreview {
        MasterDetailPane(
            windowSizeClass = fakeCompactWindowSizeClass,
            main = {
                Box(Modifier.fillMaxSize()) {
                    TextHeadline1(text = "Main")
                }
            }
        )
    }
}


@PreviewTablet
@Composable
private fun PreviewWithContentExpanded() {
    AppThemePreview {
        MasterDetailPane(
            windowSizeClass = fakeExpandedWindowSizeClass,
            main = {
                Box(Modifier.fillMaxSize()) {
                    TextHeadline1(text = "Main")
                }
            },
            content = {
                Box(
                    Modifier
                        .fillMaxSize()) {
                    TextHeadline1(text = "Detail")
                }
            }
        )
    }
}


@PreviewFoldable
@Composable
private fun PreviewWithContentMedium() {
    AppThemePreview {
        MasterDetailPane(
            windowSizeClass = fakeMediumWindowSizeClass,
            main = {
                Box(Modifier.fillMaxSize()) {
                    TextHeadline1(text = "Main")
                }
            },
            content = {
                Box(
                    Modifier
                        .fillMaxSize()) {
                    TextHeadline1(text = "Detail")
                }
            }
        )
    }
}

@PreviewPhone
@Composable
private fun PreviewWithContentCompact() {
    AppThemePreview {
        MasterDetailPane(
            windowSizeClass = fakeCompactWindowSizeClass,
            main = {
                Box(Modifier.fillMaxSize()) {
                    TextHeadline1(text = "Main")
                }
            },
            content = {
                Box(
                    Modifier
                        .fillMaxSize()) {
                    TextHeadline1(text = "Detail")
                }
            }
        )
    }
}
