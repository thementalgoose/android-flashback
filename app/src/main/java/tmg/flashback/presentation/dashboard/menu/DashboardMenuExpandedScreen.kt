package tmg.flashback.presentation.dashboard.menu

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import tmg.flashback.R
import tmg.flashback.strings.R.string
import tmg.flashback.sandbox.model.SandboxMenuItem
import tmg.flashback.eastereggs.model.MenuIcons
import tmg.flashback.eastereggs.presentation.snow
import tmg.flashback.eastereggs.presentation.summer
import tmg.flashback.presentation.dashboard.FeaturePrompt
import tmg.flashback.presentation.dashboard.MenuItem
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.input.InputSwitch
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextBody2
import tmg.flashback.ui.components.loading.Fade
import tmg.flashback.ui.components.navigation.NavigationItem
import tmg.flashback.ui.foldables.FoldingConfig

val columnWidthCollapsed: Dp = 72.dp
private val heroSize: Dp = 48.dp
private val itemSize: Dp = 42.dp
private val iconSize: Dp = 24.dp
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
    sandboxMenuItems: List<SandboxMenuItem>,
    menuItemClicked: (MenuItem) -> Unit,
    debugMenuItemClicked: (SandboxMenuItem) -> Unit,
    darkMode: Boolean,
    darkModeClicked: (Boolean) -> Unit,
    featurePromptList: List<FeaturePrompt>,
    featurePromptClicked: (FeaturePrompt) -> Unit,
    appVersion: String,
    easterEggSnow: Boolean,
    easterEggSummer: Boolean,
    easterEggTitleIcon: MenuIcons?,
    easterEggUkraine: Boolean,
    lockExpanded: Boolean,
    initialExpandedState: Boolean = true
) {
    val expanded = remember { mutableStateOf(initialExpandedState) }
    val width = animateDpAsState(targetValue = when {
        lockExpanded -> columnWidthExpandedLocked
        expanded.value -> columnWidthExpanded
        else -> columnWidthCollapsed
    }, label = "MenuWidthDp")

    Column(modifier = modifier
        .width(width.value)
        .fillMaxHeight()
        .shadow(8.dp)
        .background(AppTheme.colors.backgroundPrimary)
        .snow(easterEggSnow)
        .summer(easterEggSummer)
    ) {
        Spacer(Modifier.statusBarsPadding())
        Spacer(Modifier.height(AppTheme.dimens.small))
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
                item {
                    DarkModeToggle(
                        isExpanded = expanded.value,
                        darkMode = darkMode,
                        darkModeClicked = darkModeClicked
                    )
                }
                item { Div() }
                if (featurePromptList.isNotEmpty()) {
                    items(featurePromptList, key = { "menufeature-${it.id}" }) {
                        FeatureItem(
                            isExpanded = expanded.value,
                            featurePrompt = it,
                            onClick = featurePromptClicked
                        )
                    }
                    item { Div() }
                }
                if (sandboxMenuItems.isNotEmpty()) {
                    items(sandboxMenuItems, key = { "menudebug-$it" }) { menuItem ->
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
                item {
                    if (expanded.value) {
                        Fade(visible = expanded.value) {
                            TextBody2(
                                maxLines = 2,
                                text = stringResource(id = string.app_version_placeholder, appVersion),
                                modifier = Modifier.padding(
                                    horizontal = AppTheme.dimens.medium,
                                    vertical = AppTheme.dimens.xsmall
                                )
                            )
                        }
                    } else {
                        TextBody2(
                            text = " \n ",
                            maxLines = 2
                        )
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
        else -> Color.Transparent
    }, label = "navItem-backgroundColor")
    val iconPadding = animateDpAsState(targetValue = when (isExpanded) {
        true -> AppTheme.dimens.nsmall
        false -> (itemSize - iconSize) / 2
    }, label = "navItem-iconPadding")

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
private fun DarkModeToggle(
    isExpanded: Boolean,
    darkMode: Boolean,
    darkModeClicked: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val iconPadding = animateDpAsState(targetValue = when (isExpanded) {
        true -> AppTheme.dimens.nsmall
        false -> (itemSize - iconSize) / 2
    }, label = "navItem-iconPadding")
    val switchScale = animateFloatAsState(targetValue = when (isExpanded) {
        true -> 1f
        false -> 0f
    }, label = "navItem-switchScale")

    Row(modifier = modifier
        .padding(
            horizontal = (columnWidthCollapsed - itemSize) / 2
        )
        .fillMaxWidth()
        .height(itemSize)
        .clip(RoundedCornerShape(AppTheme.dimens.radiusMedium))
        .clickable(
            onClick = {
                darkModeClicked.invoke(!darkMode)
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
            painter = painterResource(id = R.drawable.ic_settings_dark_mode),
            tint = AppTheme.colors.contentPrimary,
            contentDescription = null
        )
        if (isExpanded) {
            Row(modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(
                    start = AppTheme.dimens.small,
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextBody1(
                    maxLines = 1,
                    text = stringResource(id = string.dashboard_links_dark_mode),
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically)
                )
                if (switchScale.value > 0.8f) {
                    InputSwitch(
                        modifier = Modifier
                            .align(Alignment.CenterVertically),
                        isChecked = darkMode
                    )
                }
            }
        }
    }
}

@Composable
private fun FeatureItem(
    isExpanded: Boolean,
    featurePrompt: FeaturePrompt,
    modifier: Modifier = Modifier,
    onClick: (FeaturePrompt) -> Unit
) {
    val textColor = animateColorAsState(
        targetValue = when (isExpanded) {
            true -> AppTheme.colors.contentPrimary
            false -> Color.Transparent
        },
        label = "featureItem-textOpacity"
    )
    Row(modifier = modifier
        .background(AppTheme.colors.backgroundSecondary)
        .clickable(
            onClick = {
                onClick.invoke(featurePrompt)
            }
        )
        .padding(
            vertical = AppTheme.dimens.small,
            horizontal = (columnWidthCollapsed - itemSize) / 2
        )
        .fillMaxWidth()
    ) {
        if (isExpanded) {
            TextBody1(
                maxLines = 2,
                modifier = Modifier
                    .padding(
                        horizontal = AppTheme.dimens.medium,
                        vertical = AppTheme.dimens.small
                    )
                    .align(Alignment.CenterVertically)
                    .fillMaxWidth(),
                text = stringResource(id = featurePrompt.label)
            )
        } else {
            TextBody1(
                text = " \n ",
                textColor = textColor.value,
                modifier = Modifier
                    .padding(
                        vertical = AppTheme.dimens.small
                    )
                    .align(Alignment.CenterVertically)
                    .fillMaxWidth(),
                textAlign= TextAlign.Center
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
        true -> AppTheme.dimens.nsmall
        false -> (itemSize - iconSize) / 2
    }, label = "iconPadding")

    Row(
        modifier = modifier
            .padding(horizontal = (columnWidthCollapsed - itemSize) / 2)
            .height(heroSize)
            .fillMaxWidth()
            .clip(RoundedCornerShape(AppTheme.dimens.radiusMedium))
            .background(Color.Transparent)
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
                painter = painterResource(id = tmg.flashback.ui.R.drawable.ic_menu_expanded),
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
private fun Div() {
    HorizontalDivider(
        modifier = Modifier.padding(
            horizontal = AppTheme.dimens.medium,
            vertical = AppTheme.dimens.xsmall
        ),
        thickness = 2.dp,
        color = AppTheme.colors.backgroundSecondary
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
            sandboxMenuItems = emptyList(),
            debugMenuItemClicked = { },
            menuItemClicked = { },
            darkMode = false,
            darkModeClicked = { },
            featurePromptList = listOf(FeaturePrompt.RuntimeNotifications),
            featurePromptClicked = { },
            appVersion = "version",
            easterEggSnow = false,
            easterEggSummer = false,
            easterEggTitleIcon = null,
            easterEggUkraine = false,
            lockExpanded = false,
            initialExpandedState = false
        )
    }
}