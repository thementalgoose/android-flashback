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
import tmg.flashback.strings.R.string

val columnWidthCollapsed: Dp = 64.dp
private val itemSize: Dp = 48.dp
private val iconSize: Dp = 24.dp
val columnWidthExpanded: Dp = 240.dp

@Composable
fun NavigationColumn(
    list: List<NavigationItem>,
    itemClicked: (NavigationItem) -> Unit,
    modifier: Modifier = Modifier,
    lockExpanded: Boolean = false,
    contentHeader: @Composable ColumnScope.() -> Unit = {}
) {
    val expanded = remember { mutableStateOf(lockExpanded) }
    val width = animateDpAsState(targetValue = when (expanded.value) {
        true -> columnWidthExpanded
        false -> columnWidthCollapsed
    }, label = "width")

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
                item {
                    Spacer(modifier = Modifier.height(AppTheme.dimens.small))
                }
            }
        )
        if (!lockExpanded) {
            NavigationItem(
                item = NavigationItem(
                    id = "menu",
                    label = string.empty,
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
    }, label = "backgroundColor")
    val iconPadding = animateDpAsState(targetValue = when (isExpanded) {
        true -> AppTheme.dimens.medium
        false -> (itemSize - iconSize) / 2
    }, label = "iconPadding")

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
private fun PreviewExpanded() {
    AppThemePreview {
        NavigationColumn(
            lockExpanded = true,
            itemClicked = { },
            list = fakeNavigationItems
        )
    }
}