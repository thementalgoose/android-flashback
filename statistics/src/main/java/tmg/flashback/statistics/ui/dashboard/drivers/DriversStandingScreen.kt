package tmg.flashback.statistics.ui.dashboard.drivers

import android.view.RoundedCorner
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import org.threeten.bp.LocalDate
import tmg.flashback.formula1.model.Driver
import tmg.flashback.formula1.model.OverviewRace
import tmg.flashback.formula1.model.SeasonDriverStandingSeason
import tmg.flashback.formula1.model.SeasonDriverStandingSeasonConstructor
import tmg.flashback.providers.DriverProvider
import tmg.flashback.providers.SeasonDriverStandingSeasonProvider
import tmg.flashback.statistics.R
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextBody2
import tmg.flashback.ui.components.header.Header
import tmg.flashback.ui.components.progressbar.ProgressBar
import kotlin.math.roundToInt

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
    list: List<SeasonDriverStandingSeason>,
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
            items(list, key = { it.driver.id }) {
                DriverView(
                    model = it,
                    itemClicked = { }
                )
            }
            item {
                Spacer(Modifier.height(AppTheme.dimensions.paddingXLarge))
            }
        }
    )
}

@Composable
private fun DriverView(
    model: SeasonDriverStandingSeason,
    itemClicked: (SeasonDriverStandingSeason) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier
        .clickable(onClick = {
            itemClicked(model)
        })
    ) {
        TextBody1(
            text = model.championshipPosition?.toString() ?: "-",
            modifier = Modifier.width(32.dp)
        )
        Box(modifier = Modifier
            .size(64.dp)
            .clip(RoundedCornerShape(AppTheme.dimensions.radiusSmall))
            .background(AppTheme.colors.primary)
        ) {

        }
        Row {
            Column(modifier = Modifier.weight(2f)) {
                TextBody1(text = model.driver.name)
                TextBody2(text = model.constructors.joinToString { it.constructor.name })
            }
            ProgressBar(
                modifier = Modifier.weight(3f),
                endProgress = 0.6f,
                label = { it.roundToInt().toString() }
            )
        }
    }
}



@Preview
@Composable
private fun PreviewLight(
    @PreviewParameter(SeasonDriverStandingSeasonProvider::class) standing: SeasonDriverStandingSeason
) {
    AppThemePreview(isLight = true) {
        DriversStandingScreenImpl(
            season = 2022,
            list = listOf(
                standing
            ),
            menuClicked = {}
        )
    }
}

@Preview
@Composable
private fun PreviewDark(
    @PreviewParameter(SeasonDriverStandingSeasonProvider::class) standing: SeasonDriverStandingSeason
) {
    AppThemePreview(isLight = false) {
        DriversStandingScreenImpl(
            season = 2022,
            list = listOf(
                standing
            ),
            menuClicked = {}
        )
    }
}