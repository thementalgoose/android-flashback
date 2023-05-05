package tmg.flashback.ui.dashboard.menu

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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
import androidx.window.layout.FoldingFeature.State.Companion.HALF_OPENED
import tmg.flashback.R
import tmg.flashback.debug.model.DebugMenuItem
import tmg.flashback.eastereggs.model.MenuIcons
import tmg.flashback.eastereggs.ui.snow
import tmg.flashback.formula1.constants.Formula1
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextBody2
import tmg.flashback.ui.components.loading.Fade
import tmg.flashback.ui.components.navigation.NavigationItem
import tmg.flashback.ui.components.navigation.NavigationTimelineItem
import tmg.flashback.ui.components.navigation.PipeType
import tmg.flashback.ui.dashboard.FeaturePrompt
import tmg.flashback.ui.dashboard.MenuItem
import tmg.flashback.ui.foldables.FoldingConfig

val columnWidthCollapsed: Dp = 56.dp
private val heroSize: Dp = 48.dp
private val itemSize: Dp = 38.dp
private val iconSize: Dp = 20.dp
val columnWidthExpanded: Dp = 240.dp
val columnWidthExpandedLocked: Dp = 260.dp

val headerHeight: Dp = 72.dp

@Composable
fun DashboardMenuExpandedScreen(
    modifier: Modifier = Modifier,
    foldingConfig: FoldingConfig?,
    currentlySelectedItem: MenuItem,
    appFeatureItemsList: List<MenuItem>,
    seasonScreenItemsList: List<MenuItem>,
    debugMenuItems: List<DebugMenuItem>,
    menuItemClicked: (MenuItem) -> Unit,
    debugMenuItemClicked: (DebugMenuItem) -> Unit,
    darkMode: Boolean,
    darkModeClicked: (Boolean) -> Unit,
    featurePromptList: List<FeaturePrompt>,
    featurePromptClicked: (FeaturePrompt) -> Unit,
    seasonItemsList: List<NavigationTimelineItem>,
    seasonClicked: (Int) -> Unit,
    appVersion: String,
    easterEggSnow: Boolean,
    easterEggTitleIcon: MenuIcons?,
    easterEggUkraine: Boolean,
    lockExpanded: Boolean
) {
    val expanded = remember { mutableStateOf(lockExpanded) }
    val width = animateDpAsState(targetValue = when {
        lockExpanded -> columnWidthExpandedLocked
        expanded.value -> {
            when (foldingConfig?.state) {
                HALF_OPENED -> foldingConfig.overrideWidth ?: columnWidthExpanded
                else -> columnWidthExpanded
            }
        }
        else -> columnWidthCollapsed
    })

    Column(modifier = modifier
        .width(width.value)
        .fillMaxHeight()
        .shadow(8.dp)
        .background(AppTheme.colors.backgroundPrimary)
        .padding(
            vertical = AppTheme.dimens.small
        )
        .snow(easterEggSnow)
    ) {
        HeroItem(
            menuIcons = easterEggTitleIcon,
            onClick = when (lockExpanded) {
                false -> { { expanded.value = !expanded.value } }
                true -> null
            },
            showUkraine = easterEggUkraine,
            isExpanded = expanded.value
        )
        Div()
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            content = {
                items(seasonScreenItemsList, key = { "menuseason-${it.id}" }) { menuItem ->
                    NavigationItem(
                        item = menuItem.toNavigationItem(currentlySelectedItem == menuItem),
                        isExpanded = expanded.value,
                        onClick = { item ->
                            menuItemClicked(menuItem)
                        },
                    )
                    Spacer(Modifier.height(AppTheme.dimens.xsmall))
                }
                item { Div() }
                items(appFeatureItemsList, key = { "menuappfeature-${it.id}"}) { menuItem ->
                    NavigationItem(
                        item = menuItem.toNavigationItem(currentlySelectedItem == menuItem),
                        isExpanded = expanded.value,
                        onClick = { item ->
                            menuItemClicked(menuItem)
                        },
                    )
                    Spacer(Modifier.height(AppTheme.dimens.xsmall))
                }
                item { Div() }
                if (debugMenuItems.isNotEmpty()) {
                    items(debugMenuItems, key = { "menudebug-$it" }) { menuItem ->
                        NavigationItem(
                            item = NavigationItem(
                                id = menuItem.id,
                                label = menuItem.label,
                                icon = menuItem.icon,
                                isSelected = false
                            ),
                            isExpanded = expanded.value,
                            onClick = { debugMenuItemClicked(menuItem) }
                        )
                        Spacer(Modifier.height(AppTheme.dimens.xsmall))
                    }
                    item { Div() }
                }
                if (seasonItemsList.isNotEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(AppTheme.dimens.small))
                    }
                    items(seasonItemsList) {
                        NavigationTimelineItem(
                            item = it,
                            isExpanded = expanded.value,
                            onClick = {
                                seasonClicked(it.label.toInt())
                            }
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(AppTheme.dimens.medium))
                    }
                }
                item {
                    if (expanded.value) {
                        Fade(visible = expanded.value) {
                            TextBody2(
                                text = stringResource(id = R.string.app_version_placeholder, appVersion),
                                modifier = Modifier.padding(
                                    horizontal = AppTheme.dimens.medium,
                                    vertical = AppTheme.dimens.xsmall
                                )
                            )
                        }
                    } else {
                        TextBody2(text = "")
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(AppTheme.dimens.large))
                }
            }
        )
    }
}

@Composable
private fun NavigationItem(
    item: NavigationItem,
    isExpanded: Boolean,
    modifier: Modifier = Modifier,
    onClick: ((NavigationItem) -> Unit)?,
) {
    val backgroundColor = animateColorAsState(targetValue = when (item.isSelected) {
        true -> AppTheme.colors.primary.copy(alpha = 0.2f)
        else -> AppTheme.colors.backgroundPrimary
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
                maxLines = 1,
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
private fun HeroItem(
    isExpanded: Boolean,
    menuIcons: MenuIcons?,
    showUkraine: Boolean,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)?,
) {
    val iconPadding = animateDpAsState(targetValue = when (isExpanded) {
        true -> AppTheme.dimens.medium
        false -> (itemSize - iconSize) / 2
    })

    Row(
        modifier = modifier
            .padding(horizontal = (columnWidthCollapsed - itemSize) / 2)
            .height(heroSize)
            .fillMaxWidth()
            .clip(RoundedCornerShape(AppTheme.dimens.radiusMedium))
            .background(AppTheme.colors.backgroundPrimary)
            .clickable(
                enabled = onClick != null,
                onClick = {
                    onClick?.invoke()
                }
            )
            .padding(horizontal = iconPadding.value,),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (onClick != null) {
            Icon(
                modifier = Modifier
                    .size(iconSize)
                    .align(Alignment.CenterVertically),
                painter = painterResource(id = R.drawable.ic_menu_expanded),
                tint = AppTheme.colors.contentPrimary,
                contentDescription = stringResource(id = R.string.app_name)
            )
            Spacer(Modifier.width(AppTheme.dimens.small))
        }
        if (isExpanded) {
            DashboardHero(
                menuIcons = menuIcons,
                showUkraine = showUkraine,
                modifier = Modifier.weight(1f)
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
    val iconPadding = animateDpAsState(targetValue = when (isExpanded) {
        true -> AppTheme.dimens.medium
        false -> (itemSize - iconSize) / 2
    })

    Box {
        Row(modifier = modifier
            .padding(
                horizontal = (columnWidthCollapsed - itemSize) / 2
            )
            .fillMaxWidth()
            .height(itemSize)
            .clip(RoundedCornerShape(AppTheme.dimens.radiusMedium))
            .background(AppTheme.colors.backgroundPrimary)
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
        if (item.label.toIntOrNull() == Formula1.currentSeasonYear) {
            Icon(
                modifier = Modifier
                    .width(AppTheme.dimens.medium)
                    .align(Alignment.CenterStart)
                    .alpha(0.4f),
                painter = painterResource(id = tmg.flashback.results.R.drawable.ic_current_indicator),
                contentDescription = null,
                tint = AppTheme.colors.contentPrimary
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
                    addOval(
                        Rect(thickness,
                        thickness,
                        size.width - 1 - thickness,
                        size.height - 1 - thickness)
                    )
                }
                val p3 = Path()
                p3.op(p1, p2, PathOperation.Difference)
                return Outline.Generic(p3)
            }
        }
    ) {}
}

@Composable
private fun Div() {
    Divider(
        modifier = Modifier.padding(
            horizontal = AppTheme.dimens.medium,
            vertical = AppTheme.dimens.xsmall
        ),
        color = AppTheme.colors.backgroundTertiary
    )
}

@PreviewTheme
@Composable
private fun PreviewCompactTimeline() {
    AppThemePreview {
        DashboardMenuExpandedScreen(
            foldingConfig = null,
            currentlySelectedItem = MenuItem.Calendar,
            appFeatureItemsList = listOf(MenuItem.Settings, MenuItem.RSS),
            seasonScreenItemsList = listOf(MenuItem.Calendar, MenuItem.Drivers),
            debugMenuItems = emptyList(),
            debugMenuItemClicked = { },
            menuItemClicked = { },
            darkMode = false,
            darkModeClicked = { },
            featurePromptList = listOf(FeaturePrompt.Notifications),
            featurePromptClicked = { },
            seasonItemsList = fakeNavigationTimelineItems,
            seasonClicked = { },
            appVersion = "version",
            easterEggSnow = false,
            easterEggTitleIcon = null,
            easterEggUkraine = false,
            lockExpanded = false
        )
    }
}

@PreviewTheme
@Composable
private fun PreviewExpandedTimeline() {
    AppThemePreview {
        DashboardMenuExpandedScreen(
            foldingConfig = null,
            currentlySelectedItem = MenuItem.Calendar,
            appFeatureItemsList = listOf(MenuItem.Settings, MenuItem.RSS),
            seasonScreenItemsList = listOf(MenuItem.Calendar, MenuItem.Drivers),
            debugMenuItems = emptyList(),
            debugMenuItemClicked = { },
            menuItemClicked = { },
            darkMode = true,
            darkModeClicked = { },
            featurePromptList = listOf(FeaturePrompt.Notifications),
            featurePromptClicked = { },
            seasonItemsList = fakeNavigationTimelineItems,
            seasonClicked = { },
            appVersion = "version",
            easterEggSnow = false,
            easterEggTitleIcon = MenuIcons.CHRISTMAS,
            easterEggUkraine = true,
            lockExpanded = true
        )
    }
}


private val fakeNavigationTimelineItems: List<NavigationTimelineItem> = listOf(
    NavigationTimelineItem(
        id = "2022",
        pipeType = PipeType.START,
        label = "2022",
        color = Color.Magenta,
        isSelected = false
    ),
    NavigationTimelineItem(
        id = "2021",
        pipeType = PipeType.START_END,
        label = "2021",
        color = Color.Magenta,
        isSelected = true
    ),
    NavigationTimelineItem(
        id = "2020",
        pipeType = PipeType.END,
        label = "2020",
        color = Color.Magenta,
        isSelected = false
    )
)