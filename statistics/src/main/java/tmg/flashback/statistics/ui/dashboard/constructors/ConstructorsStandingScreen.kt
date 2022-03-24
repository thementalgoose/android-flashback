package tmg.flashback.statistics.ui.dashboard.constructors

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import tmg.flashback.formula1.model.OverviewRace
import tmg.flashback.statistics.R
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.ui.components.header.Header

@Composable
fun ConstructorsStandingScreen(
    season: Int,
    menuClicked: (() -> Unit)?
) {
    ConstructorsStandingScreenImpl(
        season = season,
        list = emptyList(),
        menuClicked = menuClicked
    )
}

@Composable
private fun ConstructorsStandingScreenImpl(
    season: Int,
    list: List<OverviewRace>,
    menuClicked: (() -> Unit)?
) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.backgroundPrimary),
        content = {
            item {
                Header(
                    text = stringResource(id = R.string.dashboard_standings_constructor, season.toString()),
                    icon = menuClicked?.let { painterResource(id = R.drawable.ic_menu) },
                    iconContentDescription = stringResource(id = R.string.ab_menu),
                    actionUpClicked = {
                        menuClicked?.invoke()
                    }
                )
            }
            items(list, key = { "${it.season}r${it.round}" }) {
                ConstructorView()
            }
            item {
                Spacer(Modifier.height(AppTheme.dimensions.paddingXLarge))
            }
        }
    )
}

@Composable
private fun ConstructorView(

) {

}



@Preview
@Composable
private fun PreviewLight() {
    AppThemePreview(isLight = true) {
        ConstructorsStandingScreenImpl(
            season = 2022,
            list = listOf(
//                WeekendOverview.model()
            ),
            menuClicked = {}
        )
    }
}

@Preview
@Composable
private fun PreviewDark() {
    AppThemePreview(isLight = false) {
        ConstructorsStandingScreenImpl(
            season = 2022,
            list = listOf(
//                WeekendOverview.model()
            ),
            menuClicked = {}
        )
    }
}