package tmg.flashback.presentation.dashboard.menu

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import tmg.flashback.R
import tmg.flashback.strings.R.string
import tmg.flashback.debug.model.DebugMenuItem
import tmg.flashback.eastereggs.model.MenuIcons
import tmg.flashback.eastereggs.ui.snow
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.input.InputSwitch
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextBody2
import tmg.flashback.style.text.TextSection
import tmg.flashback.presentation.dashboard.FeaturePrompt
import tmg.flashback.presentation.dashboard.MenuItem

@Composable
fun DashboardMenuScreen(
    modifier: Modifier = Modifier,
    closeMenu: () -> Unit,
    currentlySelectedItem: MenuItem,
    appFeatureItemsList: List<MenuItem>,
    debugMenuItems: List<DebugMenuItem>,
    menuItemClicked: (MenuItem) -> Unit,
    debugMenuItemClicked: (DebugMenuItem) -> Unit,
    darkMode: Boolean,
    darkModeClicked: (Boolean) -> Unit,
    featurePromptList: List<FeaturePrompt>,
    featurePromptClicked: (FeaturePrompt) -> Unit,
    appVersion: String,
    easterEggSnow: Boolean,
    easterEggTitleIcon: MenuIcons?,
    easterEggUkraine: Boolean
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.colors.backgroundContainer)
            .snow(easterEggSnow),
        content = {
            item(key = "space1") { Spacer(Modifier.height(AppTheme.dimens.small)) }
            item(key = "hero") { Row {
                Spacer(Modifier.width(AppTheme.dimens.nsmall))
                DashboardHero(
                    menuIcons = easterEggTitleIcon,
                    showUkraine = easterEggUkraine
                )
            } }
            item(key = "div1") { Divider() }
            item(key = "label1") { SubHeader(text = stringResource(id = string.dashboard_all_title)) }
            item(key = "button-results") {
                // Add "results" button for phone layout only to signify that we're on the results tab
                //  Actual menu options are on bottom navigation bar and not in this composable
                //  Not needed usually as menu items are exposed on tablet
                val isSelected = currentlySelectedItem in listOf(MenuItem.Constructors, MenuItem.Drivers, MenuItem.Calendar)
                Button(
                    label = string.dashboard_tab_results,
                    icon = R.drawable.dashboard_nav_results,
                    isSelected = isSelected,
                    onClick = {
                        if (isSelected) {
                            closeMenu()
                        } else {
                            menuItemClicked(MenuItem.Calendar)
                            closeMenu()
                        }
                    }
                )
            }
            items(appFeatureItemsList, key = { "button-${it.id}" }) {
                Button(
                    label = it.label,
                    icon = it.icon,
                    isSelected = it == currentlySelectedItem,
                    onClick = {
                        menuItemClicked(it)
                        closeMenu()
                    }
                )
            }
            item(key = "div2") { Divider() }
            item(key = "dark") { 
                Toggle(
                    label = string.dashboard_links_dark_mode,
                    icon = R.drawable.ic_settings_dark_mode,
                    isEnabled = darkMode,
                    modifier = Modifier.clickable {
                        darkModeClicked(!darkMode)
                    }
                )
            }
            item(key = "div3") { Divider() }
            item(key = "features") { 
                if (featurePromptList.isNotEmpty()) {
                    featurePromptList.forEach { feature ->
                        Feature(
                            label = feature.label,
                            modifier = Modifier.clickable {
                                featurePromptClicked(feature)
                            }
                        )
                    }
                    Divider()
                }
            }
            if (debugMenuItems.isNotEmpty()) {
                item(key = "debug") {
                    SubHeader(text = "Debug")
                    debugMenuItems.forEach { debugItem ->
                        Button(
                            label = debugItem.label,
                            icon = debugItem.icon,
                            isSelected = false,
                            onClick = {
                                debugMenuItemClicked(debugItem)
                                closeMenu()
                            }
                        )
                    }
                    Divider()
                }
            }
            item(key = "appversion") {
                Label(msg = stringResource(id = string.app_version_placeholder, appVersion))
            }
        }
    )
}

@Composable
private fun SubHeader(
    text: String,
    modifier: Modifier = Modifier
) {
    TextSection(
        text = text,
        modifier = modifier
            .padding(
                start = AppTheme.dimens.medium,
                end = AppTheme.dimens.medium,
                top = AppTheme.dimens.nsmall,
                bottom = AppTheme.dimens.small
            )
            .alpha(0.8f)
    )
}

@Composable
private fun Label(
    msg: String,
    modifier: Modifier = Modifier
) {
    TextBody2(
        modifier = modifier
            .padding(
                start = AppTheme.dimens.medium,
                end = AppTheme.dimens.medium,
                top = AppTheme.dimens.nsmall,
                bottom = AppTheme.dimens.large
            ),
        text = msg
    )
}

@Composable
private fun Divider(
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier
        .fillMaxWidth()
        .padding(vertical = AppTheme.dimens.xsmall)
        .height(2.dp)
        .alpha(0.3f)
        .background(AppTheme.colors.backgroundSecondary)
    )
}

@Composable
private fun Button(
    @StringRes
    label: Int,
    @DrawableRes
    icon: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier
        .fillMaxWidth()
        .padding(
            end = AppTheme.dimens.medium / 2
        )
        .clip(
            RoundedCornerShape(
                topEnd = AppTheme.dimens.radiusLarge,
                bottomEnd = AppTheme.dimens.radiusLarge
            )
        )
        .background(
            if (isSelected) AppTheme.colors.primary.copy(alpha = 0.2f) else Color.Transparent
        )
        .clickable(onClick = onClick)
        .padding(
            top = AppTheme.dimens.nsmall,
            bottom = AppTheme.dimens.nsmall,
            start = AppTheme.dimens.medium,
            end = AppTheme.dimens.medium / 2
        )
    ) {
        Icon(
            painter = painterResource(id = icon),
            modifier = Modifier.size(20.dp),
            tint = AppTheme.colors.contentPrimary,
            contentDescription = null
        )
        Spacer(Modifier.width(AppTheme.dimens.medium))
        TextBody1(
            text = stringResource(id = label),
            bold = true,
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
        )
    }
}

@Composable
private fun Toggle(
    @StringRes
    label: Int,
    @DrawableRes
    icon: Int,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
) {
    Row(modifier = modifier
        .fillMaxWidth()
        .padding(
            vertical = AppTheme.dimens.nsmall,
            horizontal = AppTheme.dimens.medium
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(20.dp),
            painter = painterResource(id = icon),
            tint = AppTheme.colors.contentPrimary,
            contentDescription = null
        )
        Spacer(Modifier.width(AppTheme.dimens.medium))
        TextBody1(
            text = stringResource(id = label),
            bold = true,
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
        )
        InputSwitch(
            isChecked = isEnabled
        )
    }
}

@Composable
private fun Feature(
    @StringRes
    label: Int,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier
        .fillMaxWidth()
        .background(AppTheme.colors.backgroundSecondary)
        .padding(
            vertical = AppTheme.dimens.nsmall,
            horizontal = AppTheme.dimens.medium
        )
    ) {
        TextBody1(text = stringResource(id = label))
    }
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        DashboardMenuScreen(
            closeMenu = { },
            currentlySelectedItem = MenuItem.Calendar,
            appFeatureItemsList = listOf(MenuItem.Search, MenuItem.Settings, MenuItem.Contact),
            debugMenuItems = emptyList(),
            menuItemClicked = { },
            debugMenuItemClicked = { },
            darkMode = false,
            darkModeClicked = { },
            featurePromptList = listOf(FeaturePrompt.RuntimeNotifications),
            featurePromptClicked = { },
            appVersion = "version",
            easterEggSnow = false,
            easterEggTitleIcon = null,
            easterEggUkraine = false
        )
    }
}