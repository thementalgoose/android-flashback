package tmg.flashback.ui.dashboard.menu

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.viewModel
import tmg.flashback.R
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextHeadline2
import tmg.flashback.style.text.TextSection

@Composable
fun MenuScreen() {
    val viewModel by viewModel<MenuViewModel>()

    val buttons = viewModel.outputs.buttons.observeAsState(emptyList())
    val seasons = viewModel.outputs.season.observeAsState(emptyList())
    MenuScreenImpl(
        seasonClicked = viewModel.inputs::clickSeason,
        buttons = buttons.value,
        season = seasons.value
    )
}

@Composable
private fun MenuScreenImpl(
    seasonClicked: (season: Int) -> Unit,
    buttons: List<MenuButtonItem>,
    season: List<MenuSeasonItem>,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        content = {
            item { Hero() }
            item { Divider() }
            item { SubHeader(text = stringResource(id = R.string.dashboard_season_list_extra_title))}
            items(buttons) {
                when (it) {
                    is MenuButtonItem.Button -> Button(
                        label = it.label,
                        icon = it.icon
                    )
                    is MenuButtonItem.Toggle -> Toggle(
                        label = it.label,
                        icon = it.icon
                    )
                }
            }
            item { Divider() }
            item { Toggle(
                label = R.string.dashboard_season_list_extra_dark_mode_title,
                icon = R.drawable.ic_nightmode_dark
            ) }
            item { Divider() }
            item { SubHeader(text = stringResource(id = R.string.home_season_header_All))}
            items(season) {
                Season(
                    season = it.season,
                    isSelected = it.isSelected,
                    colour = it.colour,
                    clickSeason = seasonClicked
                )
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
        modifier = modifier.padding(
            start = AppTheme.dimensions.paddingMedium,
            end = AppTheme.dimensions.paddingMedium,
            top = AppTheme.dimensions.paddingNSmall,
            bottom = AppTheme.dimensions.paddingSmall
        )
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
            vertical = AppTheme.dimensions.paddingSmall,
            horizontal = AppTheme.dimensions.paddingMedium
        )
    ) {
        Icon(
            painter = painterResource(id = icon),
            tint = AppTheme.colors.contentPrimary,
            contentDescription = null
        )
        Spacer(Modifier.width(AppTheme.dimensions.paddingSmall))
        TextBody1(
            text = stringResource(id = label),
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
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier
        .fillMaxWidth()
        .clickable(onClick = {})
        .padding(
            vertical = AppTheme.dimensions.paddingSmall,
            horizontal = AppTheme.dimensions.paddingMedium
        )
    ) {
        Icon(
            painter = painterResource(id = icon),
            tint = AppTheme.colors.contentPrimary,
            contentDescription = null
        )
        Spacer(Modifier.width(AppTheme.dimensions.paddingSmall))
        TextBody1(
            text = stringResource(id = label),
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
        )
        Switch(
            checked = true,
            onCheckedChange = null
        )
    }
}

private val pipeCircumference = 20.dp
private val pipeCircumferenceInner = 12.dp
private val pipeWidth = 12.dp

@Composable
private fun Season(
    season: Int,
    isSelected: Boolean,
    colour: Color,
    clickSeason: (season: Int) -> Unit,
    modifier: Modifier = Modifier,
    showBottom: Boolean = false,
    showTop: Boolean = false,
) {
    Row(modifier = modifier
        .clickable(onClick = { clickSeason(season) })
        .padding(
            horizontal = AppTheme.dimensions.paddingMedium
        )
    ) {
        Box(
            Modifier
                .width(pipeCircumference)
                .padding(start = 2.dp)
                .defaultMinSize(
                    minWidth = 24.dp,
                    minHeight = pipeCircumference + pipeWidth
                )
        ) {
            Box(modifier = Modifier
                .align(Alignment.Center)
                .size(pipeCircumference)
                .clip(CircleShape)
                .background(colour))
            Box(modifier = Modifier
                .align(Alignment.Center)
                .size(pipeCircumferenceInner)
                .clip(CircleShape)
                .background(if (!isSelected) AppTheme.colors.backgroundSecondary else colour))
        }
        Spacer(modifier = Modifier.width(AppTheme.dimensions.paddingSmall + 2.dp))
        TextBody1(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(1f),
            bold = true,
            text = season.toString()
        )
    }
}

@Preview
@Composable
private fun PreviewLight() {
    AppThemePreview(isLight = true) {
        MenuScreenImpl(
            seasonClicked = { },
            buttons = listOf(
                MenuButtonItem.Button(R.string.ab_menu, R.drawable.ic_menu)
            ),
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
        MenuScreenImpl(
            seasonClicked = { },
            buttons = listOf(
                MenuButtonItem.Button(R.string.ab_menu, R.drawable.ic_menu)
            ),
            season = listOf(
                MenuSeasonItem(Color.Red, 2020, true),
                MenuSeasonItem(Color.Red, 2021, false)
            )
        )
    }
}