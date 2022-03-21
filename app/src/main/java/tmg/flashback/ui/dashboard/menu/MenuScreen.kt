package tmg.flashback.ui.dashboard.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
fun MenuScreen(
    seasonSelected: Int,
    seasonClicked: (season: Int) -> Unit
) {
//    val viewModel by viewModel<MenuViewModel>()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        content = {
            item { Hero() }
            item { Divider() }
            item { SubHeader(text = stringResource(id = R.string.dashboard_season_list_extra_title))}
            item { Divider() }
            item { Divider() }
            item { SubHeader(text = stringResource(id = R.string.home_season_header_All))}
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
            vertical = AppTheme.dimensions.paddingMedium,
            horizontal = AppTheme.dimensions.paddingNSmall
        )
    )
}

@Composable
private fun Divider(
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier
        .fillMaxWidth()
        .height(2.dp)
        .background(AppTheme.colors.backgroundSecondary)
        .padding(vertical = AppTheme.dimensions.paddingSmall)
    )
}

@Composable
private fun Button(
    modifier: Modifier = Modifier
) {

}

@Composable
private fun Toggle(
    modifier: Modifier = Modifier
) {

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
        Box(Modifier
            .width(pipeCircumference)
            .defaultMinSize(
                minWidth = pipeCircumference,
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
        Spacer(modifier = Modifier.width(AppTheme.dimensions.paddingSmall))
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
private fun PreviewSeasonItems() {
    AppThemePreview(isLight = true) {
        Column {
            Season(
                season = 2020,
                isSelected = false,
                showTop = false,
                showBottom = true,
                colour = Color.Cyan,
                clickSeason = { }
            )
            Season(
                season = 2021,
                isSelected = true,
                showTop = true,
                showBottom = true,
                colour = Color.Cyan,
                clickSeason = { }
            )
            Season(
                season = 2022,
                isSelected = false,
                showTop = true,
                showBottom = false,
                colour = Color.Cyan,
                clickSeason = { }
            )
        }
    }
}

@Preview
@Composable
private fun PreviewLight() {
    AppThemePreview(isLight = true) {
        MenuScreen(
            seasonSelected = 2019,
            seasonClicked = { }
        )
    }
}

@Preview
@Composable
private fun PreviewDark() {
    AppThemePreview(isLight = false) {
        MenuScreen(
            seasonSelected = 2019,
            seasonClicked = { }
        )
    }
}