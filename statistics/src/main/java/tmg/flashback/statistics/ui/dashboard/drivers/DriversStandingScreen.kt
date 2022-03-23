package tmg.flashback.statistics.ui.dashboard.drivers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import tmg.flashback.statistics.R
import tmg.flashback.statistics.models.WeekendOverview
import tmg.flashback.statistics.models.model
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.ui.components.header.Header

@Composable
fun DriversStandingScreen(
    season: Int,
    menuClicked: (() -> Unit)?
) {
    DriversStandingScreenImpl(
        season = season,
        list = emptyList(),
        menuClicked = menuClicked
    )
}

@Composable
private fun DriversStandingScreenImpl(
    season: Int,
    list: List<WeekendOverview>,
    menuClicked: (() -> Unit)?
) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.backgroundPrimary),
        content = {
            item {
                Header(
                    text = stringResource(id = R.string.dashboard_standings_driver, season.toString()),
                    icon = menuClicked?.let { painterResource(id = R.drawable.ic_menu) },
                    iconContentDescription = stringResource(id = R.string.ab_menu),
                    actionUpClicked = {
                        menuClicked?.invoke()
                    }
                )
            }
            items(list, key = { it.key }) {
                DriverView()
            }
            item {
                Spacer(Modifier.height(AppTheme.dimensions.paddingXLarge))
            }
        }
    )
}

@Composable
private fun DriverView(

) {

}



@Preview
@Composable
private fun PreviewLight() {
    AppThemePreview(isLight = true) {
        DriversStandingScreenImpl(
            season = 2022,
            list = listOf(
                WeekendOverview.model()
            ),
            menuClicked = {}
        )
    }
}

@Preview
@Composable
private fun PreviewDark() {
    AppThemePreview(isLight = false) {
        DriversStandingScreenImpl(
            season = 2022,
            list = listOf(
                WeekendOverview.model()
            ),
            menuClicked = {}
        )
    }
}