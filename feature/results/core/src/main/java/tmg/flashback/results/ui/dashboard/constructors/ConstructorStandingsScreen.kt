package tmg.flashback.results.ui.dashboard.constructors

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
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import tmg.flashback.formula1.extensions.pointsDisplay
import tmg.flashback.formula1.model.SeasonConstructorStandingSeason
import tmg.flashback.providers.SeasonConstructorStandingSeasonProvider
import tmg.flashback.results.R
import tmg.flashback.results.components.DriverPoints
import tmg.flashback.results.ui.dashboard.DashboardQuickLinks
import tmg.flashback.results.ui.messaging.Banner
import tmg.flashback.results.ui.messaging.ProvidedBy
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.text.TextTitle
import tmg.flashback.ui.components.errors.NetworkError
import tmg.flashback.ui.components.header.Header
import tmg.flashback.ui.components.loading.SkeletonViewList
import tmg.flashback.ui.components.progressbar.ProgressBar
import tmg.flashback.ui.components.swiperefresh.SwipeRefresh
import kotlin.math.roundToInt

@Composable
fun ConstructorStandingsScreenVM(
    showMenu: Boolean,
    menuClicked: (() -> Unit)? = null,
    season: Int,
    viewModel: ConstructorsStandingViewModel = hiltViewModel()
) {
    viewModel.inputs.load(season)

    val isRefreshing = viewModel.outputs.isRefreshing.observeAsState(false)
    val items = viewModel.outputs.items.observeAsState(listOf(ConstructorStandingsModel.Loading))
    SwipeRefresh(
        isLoading = isRefreshing.value,
        onRefresh = viewModel.inputs::refresh
    ) {
        ConstructorStandingsScreen(
            showMenu = showMenu,
            menuClicked = menuClicked,
            itemClicked = viewModel.inputs::clickItem,
            season = season,
            items = items.value
        )
    }
}

@Composable
fun ConstructorStandingsScreen(
    showMenu: Boolean,
    menuClicked: (() -> Unit)? = null,
    itemClicked: (ConstructorStandingsModel.Standings) -> Unit,
    season: Int,
    items: List<ConstructorStandingsModel>?
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
                DashboardQuickLinks(season = season)
                val content = (items ?: emptyList())
                    .filterIsInstance<ConstructorStandingsModel.Standings>()
                    .firstOrNull { it.standings.inProgressContent != null }?.standings?.inProgressContent
                if (content != null) {
                    val (name, round) = content
                    Banner(
                        message = stringResource(R.string.results_accurate_for, name, round),
                        showLink = false
                    )
                }
            }

            if (items == null) {
                item(key = "network") {
                    NetworkError()
                }
            }

            val maxPoints = (items ?: emptyList())
                .filterIsInstance<ConstructorStandingsModel.Standings>()
                .maxOfOrNull { it.standings.points } ?: 1125.0
            items(items ?: emptyList(), key = { it.id }) { item ->
                when (item) {
                    ConstructorStandingsModel.Loading -> {
                        SkeletonViewList()
                    }
                    is ConstructorStandingsModel.Standings -> {
                        ConstructorStandings(
                            model = item,
                            itemClicked = itemClicked,
                            maxPoints = maxPoints,
                        )
                    }
                }
            }
            item(key = "footer") {
                ProvidedBy()
                Spacer(Modifier.height(72.dp))
            }
        }
    )
}


@Composable
private fun ConstructorStandings(
    model: ConstructorStandingsModel.Standings,
    itemClicked: (ConstructorStandingsModel.Standings) -> Unit,
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
            top = AppTheme.dimens.small,
            start = AppTheme.dimens.small,
            end = AppTheme.dimens.medium,
            bottom = AppTheme.dimens.small
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
            Spacer(Modifier.width(AppTheme.dimens.small))
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
                        else -> (it * maxPoints).takeIf { !it.isNaN() }?.roundToInt()?.toString() ?: model.standings.points.pointsDisplay()
                    }
                }
            )
        }
    }
}

@PreviewTheme
@Composable
private fun Preview(
    @PreviewParameter(SeasonConstructorStandingSeasonProvider::class) constructorStandings: SeasonConstructorStandingSeason
) {
    AppThemePreview {
        ConstructorStandings(
            model = ConstructorStandingsModel.Standings(
                standings = constructorStandings,
                isSelected = false
            ),
            itemClicked = { },
            maxPoints = 25.0
        )
    }
}