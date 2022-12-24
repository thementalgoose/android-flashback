package tmg.flashback.ui.dashboard.compact.menu

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import tmg.flashback.R
import tmg.flashback.formula1.constants.Formula1
import tmg.flashback.snow.snow
import tmg.flashback.stats.components.Timeline
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextBody2
import tmg.flashback.style.text.TextHeadline2
import tmg.flashback.style.text.TextSection
import tmg.flashback.ui.dashboard.MenuSeasonItem

@Composable
fun MenuScreenVM(
    seasonClicked: (season: Int) -> Unit
) {
    val viewModel = hiltViewModel<MenuViewModel>()

    val links = viewModel.outputs.links.observeAsState(emptyList())
    val seasons = viewModel.outputs.season.observeAsState(emptyList())
    val appVersion = viewModel.outputs.appVersion.observeAsState("")

    MenuScreen(
        seasonClicked = { season ->
            viewModel.inputs.clickSeason(season)
            seasonClicked(season)
        },
        buttonClicked = viewModel.inputs::clickButton,
        toggleClicked = viewModel.inputs::clickToggle,
        featureClicked = viewModel.inputs::clickFeature,
        links = links.value,
        season = seasons.value,
        appVersion = appVersion.value,
        isSnowEnabled = viewModel.outputs.isSnowEasterEggEnabled
    )
}

@Composable
fun MenuScreen(
    seasonClicked: (season: Int) -> Unit,
    links: List<MenuItems>,
    buttonClicked: (MenuItems.Button) -> Unit,
    toggleClicked: (MenuItems.Toggle) -> Unit,
    featureClicked: (MenuItems.Feature) -> Unit,
    season: List<MenuSeasonItem>,
    appVersion: String,
    isSnowEnabled: Boolean
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .snow(isSnowEnabled),
        content = {
            item { Hero() }
            item { Divider() }
            item { SubHeader(text = stringResource(id = R.string.dashboard_links_title)) }
            items(links, key = { it.id }) {
                when (it) {
                    is MenuItems.Button -> Button(
                        label = it.label,
                        icon = it.icon,
                        modifier = Modifier.clickable(onClick = {
                            buttonClicked(it)
                        })
                    )
                    is MenuItems.Divider -> Divider()
                    is MenuItems.Toggle -> Toggle(
                        label = it.label,
                        icon = it.icon,
                        isEnabled = it.isEnabled,
                        modifier = Modifier.clickable(onClick = {
                            toggleClicked(it)
                        })
                    )
                    is MenuItems.Feature -> Feature(
                        label = it.label,
                        modifier = Modifier.clickable(onClick = {
                            featureClicked(it)
                        })
                    )
                }
            }
            item { SubHeader(text = stringResource(id = R.string.dashboard_all_title)) }
            items(season, key = { it.season }) {
                Row(modifier = Modifier
                    .height(IntrinsicSize.Min)
                    .clickable(onClick = {
                        seasonClicked(it.season)
                    })
                ) {
                    Box(modifier = Modifier
                        .width(AppTheme.dimens.medium)
                        .fillMaxHeight()
                    ) {
                        if (it.season == Formula1.currentSeasonYear) {
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
                        timelineColor = it.colour,
                        isEnabled = it.isSelected,
                        showTop = !it.isFirst,
                        showBottom = !it.isLast,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                end = AppTheme.dimens.medium
                            )
                    ) {
                        TextBody1(text = it.season.toString(), bold = true)
                    }
                }
            }
            item { Divider() }
            item { 
                Label(msg = stringResource(id = R.string.app_version_placeholder, appVersion))
            }
            item {
                Spacer(modifier = Modifier.height(AppTheme.dimens.xlarge))
            }
        }
    )
}

@Composable
private fun Hero(
    modifier: Modifier = Modifier
) {
    TextHeadline2(
        text = stringResource(id = R.string.app_name),
        modifier = modifier.padding(
            vertical = AppTheme.dimens.medium,
            horizontal = AppTheme.dimens.nsmall
        )
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
        Switch(
            checked = isEnabled,
            onCheckedChange = null
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

@Preview
@Composable
private fun PreviewLight() {
    AppThemePreview(isLight = true) {
        MenuScreen(
            seasonClicked = { },
            links = listOf(
                MenuItems.Button.Rss,
                MenuItems.Divider(),
                MenuItems.Toggle.DarkMode(false),
                MenuItems.Toggle.DarkMode(true)
            ),
            buttonClicked = { },
            toggleClicked = { },
            featureClicked = { },
            season = listOf(
                MenuSeasonItem(Color.Red, 2020, true),
                MenuSeasonItem(Color.Red, 2021, false)
            ),
            appVersion = "version"
        )
    }
}

@Preview
@Composable
private fun PreviewDark() {
    AppThemePreview(isLight = false) {
        MenuScreen(
            seasonClicked = { },
            links = listOf(
                MenuItems.Button.Rss,
                MenuItems.Divider(),
                MenuItems.Toggle.DarkMode(false),
                MenuItems.Toggle.DarkMode(true)
            ),
            buttonClicked = { },
            toggleClicked = { },
            featureClicked = { },
            season = listOf(
                MenuSeasonItem(Color.Red, 2020, true),
                MenuSeasonItem(Color.Red, 2021, false)
            ),
            appVersion = "version"
        )
    }
}