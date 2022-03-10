package tmg.flashback.ui.components.layouts

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextHeadline1
import tmg.flashback.style.text.TextHeadline2
import tmg.flashback.ui.R
import kotlin.math.roundToInt

@Composable
fun Screen(
    title: String,
    modifier: Modifier = Modifier,
    @DrawableRes
    menuIcon: Int = R.drawable.ic_menu,
    @StringRes
    menuContentDescription: Int = R.string.ab_menu,
    menuClicked: () -> Unit = { },
    content: LazyListScope.() -> Unit
) {

    val listState = rememberLazyListState()
    val toolbarExpandedHeight = with(LocalDensity.current) {
        AppTheme.dimensions.toolbarHeightExpanded.roundToPx().toFloat()
    }
    val animationMultiplier = when (listState.firstVisibleItemIndex) {
        0 -> (listState.firstVisibleItemScrollOffset.toFloat() / toolbarExpandedHeight).coerceIn(0f, 1f)
        else -> 1f
    }
    val fadeOutRatio: Float = 0.4f
    val fadeInRatio: Float = 0.6f

    Box(modifier = modifier) {
        LazyColumn(
            modifier = Modifier
                .padding(top = AppTheme.dimensions.toolbarHeight),
            state = listState,
            content = {
                item {
                    ToolbarExpanded(
                        title = title,
                        titleAnimation = (animationMultiplier * (1f / (1f - fadeOutRatio))).coerceIn(0f, 1f)
                    )
                }

                content()
            }
        )
        Toolbar(
            title = title,
            titleAnimation = ((animationMultiplier - fadeInRatio) * (1f / (1f - fadeInRatio))).coerceIn(0f, 1f),
            menuIcon = menuIcon,
            menuContentDescription = menuContentDescription,
            menuClicked = menuClicked
        )
    }
}

@Composable
private fun Toolbar(
    title: String,
    @DrawableRes
    menuIcon: Int,
    @StringRes
    menuContentDescription: Int,
    menuClicked: () -> Unit,
    modifier: Modifier = Modifier,
    titleAnimation: Float = 0f
) {

    // Toolbar height
    val toolbarMinHeight = AppTheme.dimensions.toolbarHeight
    val toolbarTextTranslationAmount = 24.dp

    // Original Toolbar
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(toolbarMinHeight)
            .background(AppTheme.colors.backgroundPrimary)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .align(Alignment.CenterVertically)
                .padding(
                    start = AppTheme.dimensions.paddingSmall,
                    end = AppTheme.dimensions.paddingSmall,
                    top = AppTheme.dimensions.paddingSmall,
                    bottom = AppTheme.dimensions.paddingSmall
                )
                .clickable(
                    onClick = menuClicked
                )
                .clip(RoundedCornerShape(AppTheme.dimensions.radiusSmall))
        ) {
            Icon(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(AppTheme.dimensions.paddingSmall),
                painter = painterResource(id = menuIcon),
                tint = AppTheme.colors.contentPrimary,
                contentDescription = stringResource(id = menuContentDescription)
            )
        }
        TextHeadline2(
            text = title,
            maxLines = 1,
            modifier = Modifier
                .offset {
                    IntOffset(
                        x = ((1.0f - titleAnimation) * -toolbarTextTranslationAmount.toPx()).roundToInt(),
                        y = 0
                    )
                }
                .align(Alignment.CenterVertically)
                .alpha(titleAnimation)
        )
    }
}

@Composable
private fun ToolbarExpanded(
    title: String,
    modifier: Modifier = Modifier,
    titleAnimation: Float = 1f,
) {
    Box(modifier = modifier
        .fillMaxWidth()
        .height(AppTheme.dimensions.toolbarHeightExpanded)
        .background(AppTheme.colors.backgroundPrimary)
    ) {
        TextHeadline1(
            text = title,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomStart)
                .padding(
                    start = AppTheme.dimensions.paddingMedium,
                    end = AppTheme.dimensions.paddingMedium,
                    top = AppTheme.dimensions.paddingSmall,
                    bottom = AppTheme.dimensions.paddingMedium
                )
                .alpha(1f - titleAnimation)
        )
    }
}

@Preview
@Composable
private fun PreviewLight() {
    AppThemePreview(isLight = true) {
        Screen(
            title = "2021"
        ) {
            items(3) { index ->
                TextBody1(
                    text = "Lazy Content $index",
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewDark() {
    AppThemePreview(isLight = false) {
        Screen(
            title = "2021"
        ) {
            items(25) { index ->
                TextBody1(
                    text = "Lazy Content $index",
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}