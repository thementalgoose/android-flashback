package tmg.flashback.ui.components.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.text.TextBody1
import tmg.flashback.ui.R

private val widthCollapsed: Dp = 64.dp
private val itemSize: Dp = 48.dp
private val iconSize: Dp = 24.dp
private val widthExpanded: Dp = 180.dp

@Composable
fun NavigationColumn(
    list: List<NavigationItem>,
    menuClicked: () -> Unit,
    itemClicked: (NavigationItem) -> Unit,
    modifier: Modifier = Modifier,
    timelineItemClicked: (NavigationTimelineItem) -> Unit = {},
    timelineList: List<NavigationTimelineItem> = emptyList(),
    defaultExpanded: Boolean = false,
) {
    val expanded = remember { mutableStateOf(defaultExpanded) }
    val width = animateDpAsState(targetValue = when (expanded.value) {
        true -> widthExpanded
        false -> widthCollapsed
    })

    Column(modifier = modifier
        .width(width.value)
        .fillMaxHeight()
        .background(AppTheme.colors.backgroundNav)
        .padding(
            vertical = AppTheme.dimensions.paddingSmall
        )
    ) {
        NavigationItem(
            item = NavigationItem(
                id = "menu",
                label = null,
                icon = R.drawable.ic_menu
            ),
            onClick = { menuClicked() },
            isExpanded = expanded.value
        )
        Column(modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(AppTheme.dimensions.paddingMedium))
            list.forEach { item ->
                NavigationItem(
                    item = item,
                    isExpanded = expanded.value,
                    onClick = itemClicked,
                )
                Spacer(Modifier.height(AppTheme.dimensions.paddingSmall))
            }
            Spacer(modifier = Modifier.height(AppTheme.dimensions.paddingMedium))
            if (timelineList.isNotEmpty()) {
                Divider(
                    modifier = Modifier.padding(horizontal = AppTheme.dimensions.paddingMedium),
                    color = AppTheme.colors.backgroundTertiary
                )
                timelineList.forEach { item ->
                    NavigationTimelineItem(
                        item = item,
                        isExpanded = expanded.value,
                        onClick = timelineItemClicked
                    )
                }
                Spacer(modifier = Modifier.height(AppTheme.dimensions.paddingMedium))
            }

        }
        NavigationItem(
            item = NavigationItem(
                id = "expand",
                label = null,
                icon = R.drawable.ic_menu_expanded
            ),
            onClick = {
                expanded.value = !expanded.value
            },
            isExpanded = expanded.value
        )
    }
}

@Composable
private fun NavigationItem(
    item: NavigationItem,
    isExpanded: Boolean,
    onClick: ((NavigationItem) -> Unit)?,
    modifier: Modifier = Modifier,
) {
    val backgroundColor = animateColorAsState(targetValue = when (item.isSelected) {
        true -> AppTheme.colors.primary.copy(alpha = 0.2f)
        else -> AppTheme.colors.backgroundNav
    })
    val iconPadding = animateDpAsState(targetValue = when (isExpanded) {
        true -> AppTheme.dimensions.paddingMedium
        false -> (itemSize - iconSize) / 2
    })

    Row(modifier = modifier
        .padding(
            horizontal = (widthCollapsed - itemSize) / 2
        )
        .fillMaxWidth()
        .height(itemSize)
        .clip(RoundedCornerShape(AppTheme.dimensions.radiusMedium))
        .background(backgroundColor.value)
        .clickable(
            enabled = onClick != null,
            onClick = {
                onClick?.invoke(item)
            }
        )
        .padding(
            horizontal = iconPadding.value,
        )
    ) {
        Icon(
            modifier = Modifier
                .size(iconSize)
                .align(Alignment.CenterVertically),
            painter = painterResource(id = item.icon),
            tint = AppTheme.colors.contentPrimary,
            contentDescription = item.label?.let { stringResource(id = it) }
        )
        if (isExpanded) {
            TextBody1(
                modifier = Modifier
                    .padding(start = AppTheme.dimensions.paddingSmall)
                    .align(Alignment.CenterVertically)
                    .fillMaxWidth(),
                text = item.label?.let { stringResource(id = it) } ?: ""
            )
        }
    }
}

@Composable
private fun NavigationTimelineItem(
    item: NavigationTimelineItem,
    isExpanded: Boolean,
    onClick: ((NavigationTimelineItem) -> Unit)?,
    modifier: Modifier = Modifier
) {
    val backgroundColor = animateColorAsState(targetValue = when (item.isSelected) {
        true -> AppTheme.colors.primary.copy(alpha = 0.2f)
        else -> AppTheme.colors.backgroundNav
    })
    val iconPadding = animateDpAsState(targetValue = when (isExpanded) {
        true -> AppTheme.dimensions.paddingMedium
        false -> (itemSize - iconSize) / 2
    })

    Row(modifier = modifier
        .padding(
            horizontal = (widthCollapsed - itemSize) / 2
        )
        .fillMaxWidth()
        .height(itemSize)
        .clip(RoundedCornerShape(AppTheme.dimensions.radiusMedium))
        .background(backgroundColor.value)
        .clickable(
            enabled = onClick != null,
            onClick = {
                onClick?.invoke(item)
            }
        )
        .padding(
            horizontal = iconPadding.value,
        )
    ) {
        Box(Modifier.fillMaxHeight()) {
            Box(Modifier
                .width(8.dp)
                .fillMaxHeight()
                .align(Alignment.Center)
                .background(item.color)
            )
            Donut(
                color = item.color,
                modifier = Modifier
                    .size(iconSize)
                    .align(Alignment.Center)
            )
        }
//        Box(modifier = Modifier
//            .size(iconSize)
//            .align(Alignment.CenterVertically)
//            .clip(CircleShape)
//            .background(item.color)
//        ) {
//            Box(modifier = Modifier
//                .size(iconSize / 2)
//                .align(Alignment.Center)
//                .clip(CircleShape)
//                .background(backgroundColor.value)
//            )
//        }
        if (isExpanded) {
            TextBody1(
                modifier = Modifier
                    .padding(start = AppTheme.dimensions.paddingSmall)
                    .align(Alignment.CenterVertically)
                    .fillMaxWidth(),
                text = item.label
            )
        }
    }
}

@Composable
fun Donut(
    modifier: Modifier = Modifier,
    color: Color,
) {
    Surface(modifier = modifier,
        color = color,
        shape = object : Shape {
            override fun createOutline(
                size: Size,
                layoutDirection: LayoutDirection,
                density: Density
            ): Outline {
                val thickness = size.height / 4
                val p1 = Path().apply {
                    addOval(Rect(0f, 0f, size.width - 1, size.height - 1))
                }
                val p2 = Path().apply {
                    addOval(Rect(thickness,
                        thickness,
                        size.width - 1 - thickness,
                        size.height - 1 - thickness))
                }
                val p3 = Path()
                p3.op(p1, p2, PathOperation.Difference)
                return Outline.Generic(p3)
            }
        }
    ) {}
}

@PreviewTheme
@Composable
private fun PreviewCompact() {
    AppThemePreview {
        NavigationColumn(
            defaultExpanded = false,
            itemClicked = { },
            menuClicked = { },
            list = fakeNavigationItems
        )
    }
}

@PreviewTheme
@Composable
private fun PreviewCompactTimeline() {
    AppThemePreview {
        NavigationColumn(
            defaultExpanded = false,
            itemClicked = { },
            menuClicked = { },
            timelineList = fakeNavigationTimelineItems,
            list = fakeNavigationItems
        )
    }
}

@PreviewTheme
@Composable
private fun PreviewExpanded() {
    AppThemePreview {
        NavigationColumn(
            defaultExpanded = true,
            itemClicked = { },
            menuClicked = { },
            list = fakeNavigationItems
        )
    }
}

@PreviewTheme
@Composable
private fun PreviewExpandedTimeline() {
    AppThemePreview {
        NavigationColumn(
            defaultExpanded = true,
            itemClicked = { },
            menuClicked = { },
            timelineList = fakeNavigationTimelineItems,
            list = fakeNavigationItems
        )
    }
}