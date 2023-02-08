package tmg.flashback.ui.dashboard

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import tmg.flashback.R
import tmg.flashback.formula1.constants.Formula1
import tmg.flashback.stats.components.Timeline
import tmg.flashback.style.AppTheme
import tmg.flashback.style.input.InputSwitch
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextBody2
import tmg.flashback.style.text.TextSection
import tmg.flashback.ui.components.navigation.NavigationItem
import tmg.flashback.ui.components.navigation.NavigationTimelineItem

@Composable
fun DashboardMenuScreen(
    modifier: Modifier = Modifier,
    appFeatureItemsList: List<MenuItem>,
    menuItemClicked: (MenuItem) -> Unit,
    darkMode: Boolean,
    darkModeClicked: (Boolean) -> Unit,
    featurePromptList: List<DashboardFeaturePrompt>,
    featurePromptClicked: (DashboardFeaturePrompt) -> Unit,
    seasonItemsList: List<NavigationTimelineItem>,
    seasonClicked: (Int) -> Unit,
    appVersion: String
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.colors.backgroundPrimary),
        content = {
            item(key = "hero") { DashboardHero(menuIcons = null) }
            item(key = "div1") { Divider() }
            item(key = "label1") { SubHeader(text = stringResource(id = R.string.dashboard_all_title))}
            items(appFeatureItemsList, key = { "button-${it.id}" }) {
                Button(
                    label = it.label,
                    icon = it.icon,
                    modifier = Modifier.clickable {
                        menuItemClicked(it)
                    }
                )
            }
            item(key = "div2") { Divider() }
            item(key = "dark") { 
                Toggle(
                    label = R.string.dashboard_links_dark_mode, 
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
            item(key = "label2") { SubHeader(text = stringResource(id = R.string.dashboard_all_title))}
            items(seasonItemsList, key = { "season-$it" }) {
                TimelineItem(
                    model = it,
                    seasonClicked = seasonClicked
                )
            }
            item(key = "appversion") {
                Label(msg = stringResource(id = R.string.app_version_placeholder, appVersion))
            }
        }
    )
}

@Composable
private fun TimelineItem(
    model: NavigationTimelineItem,
    seasonClicked: (Int) -> Unit,
) {
    val season = model.id.toInt()
    Row(modifier = Modifier
        .height(IntrinsicSize.Min)
        .clickable(onClick = {
            seasonClicked(season)
        })
    ) {
        Box(modifier = Modifier
            .width(AppTheme.dimens.medium)
            .fillMaxHeight()
        ) {
            if (season == Formula1.currentSeasonYear) {
                Icon(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .alpha(0.4f),
                    painter = painterResource(id = tmg.flashback.stats.R.drawable.ic_current_indicator),
                    contentDescription = null,
                    tint = AppTheme.colors.contentPrimary
                )
            }
        }
        Timeline(
            timelineColor = model.color,
            isEnabled = model.isSelected,
            showTop = model.isFirst,
            showBottom = model.isLast,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    end = AppTheme.dimens.medium
                )
        ) {
            TextBody1(text = model.label, bold = true)
        }
    }
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
                bottom = AppTheme.dimens.small
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
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier
        .fillMaxWidth()
        .padding(
            vertical = AppTheme.dimens.nsmall,
            horizontal = AppTheme.dimens.medium
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