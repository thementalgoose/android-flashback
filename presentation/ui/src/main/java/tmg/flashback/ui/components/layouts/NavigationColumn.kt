package tmg.flashback.ui.components.layouts

import android.view.RoundedCorner
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
private val widthExpanded: Dp = 180.dp

@Composable
fun NavigationColumn(
    list: List<DashboardMenuItem>,
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
        .padding(
            vertical = AppTheme.dimensions.paddingSmall
        )
    ) {
        NavigationItem(
            item = DashboardMenuItem(
                id = "menu",
                label = null,
                icon = R.drawable.ic_menu
            ),
            isExpanded = expanded.value
        )
        list.forEach { item ->
            NavigationItem(
                item = item,
                isExpanded = expanded.value
            )
            Spacer(Modifier.height(AppTheme.dimensions.paddingSmall))
        }
        Spacer(modifier = Modifier.weight(1f))
        NavigationItem(
            modifier = Modifier
                .clickable(onClick = {
                    expanded.value = !expanded.value
                }),
            item = DashboardMenuItem(
                id = "expand",
                label = null,
                icon = R.drawable.ic_menu_expanded
            ),
            isExpanded = expanded.value
        )
    }
}

@Composable
private fun NavigationItem(
    item: DashboardMenuItem,
    isExpanded: Boolean,
    modifier: Modifier = Modifier,
) {

    val backgroundColor = animateColorAsState(targetValue = when (item.isSelected) {
        true -> Color.Green
        else -> AppTheme.colors.backgroundPrimary
    })

    Row(modifier = modifier
        .padding(
            horizontal = (widthCollapsed - itemSize) / 2
        )
        .fillMaxWidth()
        .height(itemSize)
        .clip(RoundedCornerShape(AppTheme.dimensions.radiusMedium))
        .background(backgroundColor.value)
        .padding(
            horizontal = AppTheme.dimensions.paddingMedium,
        )
    ) {
        Icon(
            modifier = Modifier
                .size(24.dp)
                .align(Alignment.CenterVertically),
            painter = painterResource(id = item.icon),
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
private fun PreviewCompact() {
    AppThemePreview(isLight = true) {
        NavigationColumn(
            defaultExpanded = false,
            list = listOf(
                DashboardMenuItem(
                    id = "id",
                    label = R.string.ab_menu,
                    icon = R.drawable.ic_menu
                )
            )
        )
    }
}

@Preview
@Composable
private fun PreviewExpanded() {
    AppThemePreview(isLight = true) {
        NavigationColumn(
            defaultExpanded = true,
            list = listOf(
                DashboardMenuItem(
                    id = "id",
                    label = R.string.ab_menu,
                    icon = R.drawable.ic_menu
                ),
                DashboardMenuItem(
                    id = "id",
                    label = R.string.ab_back,
                    icon = R.drawable.ic_back,
                    isSelected = true
                )
            )
        )
    }
}