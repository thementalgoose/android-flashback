package tmg.flashback.ui.dashboard.menu

import android.content.Intent
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.inject
import org.koin.androidx.compose.viewModel
import tmg.flashback.R
import tmg.flashback.rss.ui.RSSActivity
import tmg.flashback.stats.components.Timeline
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextHeadline2
import tmg.flashback.style.text.TextSection
import tmg.flashback.ui.extensions.Observe
import tmg.flashback.ui.extensions.ObserveContext
import tmg.flashback.ui.navigation.NavigationProvider
import tmg.flashback.ui2.settings.SettingsAllActivity

@Composable
fun MenuScreenVM(
    seasonClicked: (season: Int) -> Unit
) {
    val viewModel by viewModel<MenuViewModel>()

    val links = viewModel.outputs.links.observeAsState(emptyList())
    val seasons = viewModel.outputs.season.observeAsState(emptyList())

    val navigationProvider: NavigationProvider by inject()
    ObserveContext(liveData = viewModel.outputs.openRss, callback = { context ->
        val intent = Intent(context, RSSActivity::class.java)
        context.startActivity(intent)
    })
    ObserveContext(liveData = viewModel.outputs.openSettings, callback = { context ->
        val intent = Intent(context, SettingsAllActivity::class.java)
        context.startActivity(intent)
    })
    ObserveContext(liveData = viewModel.outputs.openContact, callback = { context ->
        navigationProvider.aboutAppIntent(context = context)
    })

    MenuScreen(
        seasonClicked = { season ->
            viewModel.inputs.clickSeason(season)
            seasonClicked(season)
        },
        buttonClicked = viewModel.inputs::clickButton,
        toggleClicked = viewModel.inputs::clickToggle,
        links = links.value,
        season = seasons.value
    )
}

@Composable
fun MenuScreen(
    seasonClicked: (season: Int) -> Unit,
    links: List<MenuItems>,
    buttonClicked: (MenuItems.Button) -> Unit,
    toggleClicked: (MenuItems.Toggle) -> Unit,
    season: List<MenuSeasonItem>,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        content = {
            item { Hero() }
            item { Divider() }
            item { SubHeader(text = stringResource(id = R.string.dashboard_links_title))}
            items(links, key = { it.id }) {
                when (it) {
                    is MenuItems.Button -> Button(
                        label = it.label,
                        icon = it.icon,
                        modifier = Modifier.clickable(onClick = {
                            buttonClicked(it)
                        })
                    )
                    MenuItems.Divider -> Divider()
                    is MenuItems.Toggle.DarkMode -> Toggle(
                        label = it.label,
                        icon = it.icon,
                        isEnabled = it.isEnabled,
                        modifier = Modifier.clickable(onClick = {
                            toggleClicked(it)
                        })
                    )
                }
            }
            item { Divider() }
            item { SubHeader(text = stringResource(id = R.string.dashboard_all_title))}
            items(season, key = { it.season }) {
                Timeline(
                    timelineColor = it.colour,
                    isEnabled = it.isSelected,
                    showTop = !it.isFirst,
                    showBottom = !it.isLast,
                    modifier = Modifier
                        .clickable(onClick = {
                            seasonClicked(it.season)
                        })
                        .fillMaxWidth()
                        .padding(
                            horizontal = AppTheme.dimensions.paddingMedium
                        )
                ) {
                    TextBody1(text = it.season.toString(), bold = true)
                }
            }
            item {
                Spacer(modifier = Modifier.height(AppTheme.dimensions.paddingXXLarge))
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
            vertical = AppTheme.dimensions.paddingMedium,
            horizontal = AppTheme.dimensions.paddingNSmall
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
                start = AppTheme.dimensions.paddingMedium,
                end = AppTheme.dimensions.paddingMedium,
                top = AppTheme.dimensions.paddingNSmall,
                bottom = AppTheme.dimensions.paddingSmall
            )
            .alpha(0.8f)
    )
}

@Composable
private fun Divider(
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier
        .fillMaxWidth()
        .padding(vertical = AppTheme.dimensions.paddingXSmall)
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
        .clickable(onClick = {})
        .padding(
            vertical = AppTheme.dimensions.paddingNSmall,
            horizontal = AppTheme.dimensions.paddingMedium
        )
    ) {
        Icon(
            painter = painterResource(id = icon),
            modifier = Modifier.size(20.dp),
            tint = AppTheme.colors.contentPrimary,
            contentDescription = null
        )
        Spacer(Modifier.width(AppTheme.dimensions.paddingMedium))
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
    isEnabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier
        .fillMaxWidth()
        .padding(
            vertical = AppTheme.dimensions.paddingNSmall,
            horizontal = AppTheme.dimensions.paddingMedium
        )
    ) {
        Icon(
            modifier = Modifier.size(20.dp),
            painter = painterResource(id = icon),
            tint = AppTheme.colors.contentPrimary,
            contentDescription = null
        )
        Spacer(Modifier.width(AppTheme.dimensions.paddingMedium))
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

@Preview
@Composable
private fun PreviewLight() {
    AppThemePreview(isLight = true) {
        MenuScreen(
            seasonClicked = { },
            links = listOf(
                MenuItems.Button.Rss,
                MenuItems.Divider,
                MenuItems.Toggle.DarkMode(false),
                MenuItems.Toggle.DarkMode(true)
            ),
            buttonClicked = { },
            toggleClicked = { },
            season = listOf(
                MenuSeasonItem(Color.Red, 2020, true),
                MenuSeasonItem(Color.Red, 2021, false)
            )
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
                MenuItems.Divider,
                MenuItems.Toggle.DarkMode(false),
                MenuItems.Toggle.DarkMode(true)
            ),
            buttonClicked = { },
            toggleClicked = { },
            season = listOf(
                MenuSeasonItem(Color.Red, 2020, true),
                MenuSeasonItem(Color.Red, 2021, false)
            )
        )
    }
}