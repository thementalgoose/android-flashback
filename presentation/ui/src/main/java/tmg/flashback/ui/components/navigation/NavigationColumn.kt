package tmg.flashback.ui.components.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
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
        Spacer(modifier = Modifier.height(AppTheme.dimensions.paddingMedium))
        list.forEach { item ->
            NavigationItem(
                item = item,
                isExpanded = expanded.value,
                onClick = itemClicked,
            )
            Spacer(Modifier.height(AppTheme.dimensions.paddingSmall))
        }
        Spacer(modifier = Modifier.weight(1f))
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

@Preview
@Composable
private fun PreviewCompactLight() {
    AppThemePreview(isLight = true) {
        NavigationColumn(
            defaultExpanded = false,
            itemClicked = { },
            menuClicked = { },
            list = fakeNavigationItems
        )
    }
}

@Preview
@Composable
private fun PreviewExpandedLight() {
    AppThemePreview(isLight = true) {
        NavigationColumn(
            defaultExpanded = true,
            itemClicked = { },
            menuClicked = { },
            list = fakeNavigationItems
        )
    }
}

@Preview
@Composable
private fun PreviewCompactDark() {
    AppThemePreview(isLight = false) {
        NavigationColumn(
            defaultExpanded = false,
            itemClicked = { },
            menuClicked = { },
            list = fakeNavigationItems
        )
    }
}

@Preview
@Composable
private fun PreviewExpandedDark() {
    AppThemePreview(isLight = false) {
        NavigationColumn(
            defaultExpanded = true,
            itemClicked = { },
            menuClicked = { },
            list = fakeNavigationItems
        )
    }
}