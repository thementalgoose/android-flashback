package tmg.flashback.ui.components.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.text.TextBody1
import tmg.flashback.ui.R

val columnWidthCollapsed: Dp = 56.dp
private val itemSize: Dp = 38.dp
private val iconSize: Dp = 20.dp
val columnWidthExpanded: Dp = 240.dp

@Composable
fun NavigationColumn(
    list: List<NavigationItem>,
    itemClicked: (NavigationItem) -> Unit,
    modifier: Modifier = Modifier,
    timelineItemClicked: (NavigationTimelineItem) -> Unit = {},
    timelineList: List<NavigationTimelineItem> = emptyList(),
    lockExpanded: Boolean = false,
    contentHeader: @Composable ColumnScope.() -> Unit = {}
) {
    val expanded = remember { mutableStateOf(lockExpanded) }
    val width = animateDpAsState(targetValue = when (expanded.value) {
        true -> columnWidthExpanded
        false -> columnWidthCollapsed
    })

    Column(modifier = modifier
        .width(width.value)
        .fillMaxHeight()
        .background(AppTheme.colors.backgroundNav)
        .padding(
            vertical = AppTheme.dimens.small
        )
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            content = {
                item {
                    Column(Modifier.fillMaxWidth()) {
                        Spacer(modifier = Modifier.height(AppTheme.dimens.small))
//                        contentHeader()
//                        Spacer(modifier = Modifier.height(AppTheme.dimens.small))
                    }
                }
                items(list) {
                    NavigationItem(
                        item = it,
                        isExpanded = expanded.value,
                        onClick = itemClicked,
                    )
                    Spacer(Modifier.height(AppTheme.dimens.small))
                }
                if (timelineList.isNotEmpty()) {
                    item {
                        Divider(
                            modifier = Modifier.padding(horizontal = AppTheme.dimens.medium),
                            color = AppTheme.colors.backgroundTertiary
                        )
                        Spacer(modifier = Modifier.height(AppTheme.dimens.small))
                    }
                    items(timelineList) {
                        NavigationTimelineItem(
                            item = it,
                            isExpanded = expanded.value,
                            onClick = timelineItemClicked
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(AppTheme.dimens.medium))
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(AppTheme.dimens.small))
                }
            }
        )
        if (!lockExpanded) {
            NavigationItem(
                item = NavigationItem(
                    id = "menu",
                    label = R.string.empty,
                    icon = R.drawable.ic_menu_expanded
                ),
                onClick = {
                    expanded.value = !expanded.value
                },
                isExpanded = expanded.value
            )
        }
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
        true -> AppTheme.dimens.medium
        false -> (itemSize - iconSize) / 2
    })

    Row(modifier = modifier
        .padding(
            horizontal = (columnWidthCollapsed - itemSize) / 2
        )
        .fillMaxWidth()
        .height(itemSize)
        .clip(RoundedCornerShape(AppTheme.dimens.radiusMedium))
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
            contentDescription = stringResource(id = item.label)
        )
        if (isExpanded) {
            TextBody1(
                modifier = Modifier
                    .padding(start = AppTheme.dimens.small)
                    .align(Alignment.CenterVertically)
                    .fillMaxWidth(),
                text = item.label.let { stringResource(id = it) }
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
        true -> AppTheme.dimens.medium
        false -> (itemSize - iconSize) / 2
    })

    Row(modifier = modifier
        .padding(
            horizontal = (columnWidthCollapsed - itemSize) / 2
        )
        .fillMaxWidth()
        .height(itemSize)
        .clip(RoundedCornerShape(AppTheme.dimens.radiusMedium))
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
            Column(
                Modifier
                    .fillMaxHeight()
                    .width(8.dp)
                    .align(Alignment.Center)
            ) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(bottom = (iconSize / 2f) - 2.dp)
                        .background(
                            when (item.pipeType) {
                                PipeType.SINGLE -> Color.Transparent
                                PipeType.START -> Color.Transparent
                                PipeType.START_END -> item.color
                                PipeType.SINGLE_PIPE -> Color.Transparent
                                PipeType.END -> item.color
                            }
                        )
                )
                Box(
                    Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(top = (iconSize / 2f) - 2.dp)
                        .background(
                            when (item.pipeType) {
                                PipeType.SINGLE -> Color.Transparent
                                PipeType.START -> item.color
                                PipeType.START_END -> item.color
                                PipeType.SINGLE_PIPE -> Color.Transparent
                                PipeType.END -> Color.Transparent
                            }
                        )
                )
            }
            if (item.isSelected) {
                Box(
                    Modifier
                        .size(iconSize)
                        .align(Alignment.Center)
                        .clip(CircleShape)
                        .background(item.color)
                )
            } else {
                Donut(
                    color = item.color,
                    modifier = Modifier
                        .size(iconSize)
                        .align(Alignment.Center)
                )
            }
        }
        if (isExpanded) {
            TextBody1(
                modifier = Modifier
                    .padding(start = AppTheme.dimens.small)
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
            lockExpanded = false,
            itemClicked = { },
            list = fakeNavigationItems
        )
    }
}

@PreviewTheme
@Composable
private fun PreviewCompactTimeline() {
    AppThemePreview {
        NavigationColumn(
            lockExpanded = false,
            itemClicked = { },
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
            lockExpanded = true,
            itemClicked = { },
            list = fakeNavigationItems
        )
    }
}

@PreviewTheme
@Composable
private fun PreviewExpandedTimeline() {
    AppThemePreview {
        NavigationColumn(
            lockExpanded = true,
            itemClicked = { },
            timelineList = fakeNavigationTimelineItems,
            list = fakeNavigationItems
        )
    }
}