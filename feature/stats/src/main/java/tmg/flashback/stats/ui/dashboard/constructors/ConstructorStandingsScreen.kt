package tmg.flashback.stats.ui.dashboard.constructors

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import org.koin.androidx.compose.viewModel
import tmg.flashback.formula1.extensions.pointsDisplay
import tmg.flashback.formula1.model.SeasonConstructorStandingSeason
import tmg.flashback.formula1.model.SeasonDriverStandingSeason
import tmg.flashback.providers.SeasonConstructorStandingSeasonProvider
import tmg.flashback.providers.SeasonDriverStandingSeasonProvider
import tmg.flashback.stats.R
import tmg.flashback.stats.components.DriverPoints
import tmg.flashback.stats.ui.dashboard.DashboardQuickLinks
import tmg.flashback.stats.ui.dashboard.drivers.DriverStandings
import tmg.flashback.stats.ui.dashboard.drivers.DriverStandingsModel
import tmg.flashback.stats.ui.dashboard.drivers.DriverStandingsScreen
import tmg.flashback.stats.ui.dashboard.drivers.DriversStandingViewModel
import tmg.flashback.stats.ui.messaging.Banner
import tmg.flashback.stats.ui.messaging.ProvidedBy
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.text.TextTitle
import tmg.flashback.ui.components.header.Header
import tmg.flashback.ui.components.progressbar.ProgressBar
import kotlin.math.roundToInt

@Composable
fun ConstructorStandingsScreenVM(
    showMenu: Boolean,
    menuClicked: (() -> Unit)? = null,
    season: Int
) {
    val viewModel: ConstructorsStandingViewModel by viewModel()
    viewModel.inputs.load(season)

    val isRefreshing = viewModel.outputs.isRefreshing.observeAsState(false)
    val items = viewModel.outputs.items.observeAsState(emptyList())
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = isRefreshing.value),
        onRefresh = viewModel.inputs::refresh
    ) {
        ConstructorStandingsScreen(
            showMenu = showMenu,
            menuClicked = menuClicked,
            itemClicked = viewModel.inputs::clickItem,
            season = season,
            items = items.value ?: emptyList()
        )
    }
}

@Composable
fun ConstructorStandingsScreen(
    showMenu: Boolean,
    menuClicked: (() -> Unit)? = null,
    itemClicked: (ConstructorStandingsModel) -> Unit,
    season: Int,
    items: List<ConstructorStandingsModel>
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.backgroundPrimary),
        content = {
            item(key = "header") {
                Header(
                    text = stringResource(id = R.string.season_standings_constructor, season.toString()),
                    icon = when (showMenu) {
                        true -> painterResource(id = R.drawable.ic_menu)
                        false -> null
                    },
                    iconContentDescription = when (showMenu) {
                        true -> stringResource(id = R.string.ab_menu)
                        false -> null
                    },
                    actionUpClicked = {
                        menuClicked?.invoke()
                    }
                )
            }
            item(key = "info") {
                DashboardQuickLinks()
                val content = items.firstOrNull { it.standings.inProgressContent != null }?.standings?.inProgressContent
                if (content != null) {
                    val (name, round) = content
                    Banner(
                        message = stringResource(R.string.results_accurate_for, name, round),
                        showLink = false
                    )
                }
            }
            items(items, key = { it.id }) { item ->
                ConstructorStandings(
                    model = item,
                    itemClicked = itemClicked,
                    maxPoints = (items.maxOfOrNull { it.standings.points } ?: 1250.0),
                )
            }
            item(key = "footer") {
                Spacer(Modifier.height(72.dp))
            }
        }
    )
}


@Composable
private fun ConstructorStandings(
    model: ConstructorStandingsModel,
    itemClicked: (ConstructorStandingsModel) -> Unit,
    maxPoints: Double,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.clickable(onClick = {
            itemClicked(model)
        }),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TextTitle(
            text = model.standings.championshipPosition?.toString() ?: "-",
            bold = true,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(36.dp)
        )
        Row(modifier = Modifier.padding(
            top = AppTheme.dimensions.paddingSmall,
            start = AppTheme.dimensions.paddingSmall,
            end = AppTheme.dimensions.paddingMedium,
            bottom = AppTheme.dimensions.paddingSmall
        )) {
            Column(modifier = Modifier.weight(3f)) {
                TextTitle(
                    text = model.standings.constructor.name,
                    bold = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(2.dp))
                model.standings.drivers.forEach {
                    DriverPoints(
                        driver = it.driver,
                        points = it.points
                    )
                }
            }
            Spacer(Modifier.width(AppTheme.dimensions.paddingSmall))
            val progress = (model.standings.points / maxPoints).toFloat().coerceIn(0f, 1f)
            ProgressBar(
                modifier = Modifier
                    .weight(2f)
                    .height(48.dp)
                    .fillMaxHeight(),
                endProgress = progress,
                barColor = model.standings.constructor.colour,
                label = {
                    when (it) {
                        0f -> "0"
                        progress -> model.standings.points.pointsDisplay()
                        else -> (it * maxPoints).roundToInt().toString()
                    }
                }
            )
        }
    }
}

@Preview
@Composable
private fun PreviewLight(
    @PreviewParameter(SeasonConstructorStandingSeasonProvider::class) constructorStandings: SeasonConstructorStandingSeason
) {
    AppThemePreview(isLight = true) {
        ConstructorStandings(
            model = ConstructorStandingsModel(
                standings = constructorStandings,
                isSelected = false
            ),
            itemClicked = { },
            maxPoints = 25.0
        )
    }
}

@Preview
@Composable
private fun PreviewDark(
    @PreviewParameter(SeasonConstructorStandingSeasonProvider::class) constructorStandings: SeasonConstructorStandingSeason
) {
    AppThemePreview(isLight = false) {
        ConstructorStandings(
            model = ConstructorStandingsModel(
                standings = constructorStandings,
                isSelected = false
            ),
            itemClicked = { },
            maxPoints = 25.0
        )
    }
}