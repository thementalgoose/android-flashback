package tmg.flashback.season.presentation.dashboard.drivers

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import tmg.flashback.drivers.contract.DriverNavigationComponent
import tmg.flashback.drivers.contract.requireDriverNavigationComponent
import tmg.flashback.formula1.extensions.pointsDisplay
import tmg.flashback.formula1.model.SeasonDriverStandingSeason
import tmg.flashback.providers.SeasonDriverStandingSeasonProvider
import tmg.flashback.season.BuildConfig
import tmg.flashback.season.R
import tmg.flashback.season.presentation.dashboard.races.RacesModel
import tmg.flashback.strings.R.string
import tmg.flashback.season.presentation.dashboard.shared.seasonpicker.SeasonTitleVM
import tmg.flashback.season.presentation.messaging.Banner
import tmg.flashback.season.presentation.messaging.ProvidedBy
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.text.TextBody2
import tmg.flashback.style.text.TextTitle
import tmg.flashback.ui.components.constructorIndicator
import tmg.flashback.ui.components.drivers.DriverIcon
import tmg.flashback.ui.components.errors.NetworkError
import tmg.flashback.ui.components.flag.Flag
import tmg.flashback.ui.components.header.Header
import tmg.flashback.ui.components.header.HeaderAction
import tmg.flashback.ui.components.layouts.MasterDetailsPane
import tmg.flashback.ui.components.loading.Fade
import tmg.flashback.ui.components.loading.SkeletonViewList
import tmg.flashback.ui.components.progressbar.ProgressBar
import tmg.flashback.ui.components.swiperefresh.SwipeRefresh
import kotlin.math.max
import kotlin.math.roundToInt

@Composable
fun DriverStandingsScreenVM(
    actionUpClicked: () -> Unit,
    windowSizeClass: WindowSizeClass,
    isRoot: (Boolean) -> Unit,
    viewModel: DriverStandingsViewModel = hiltViewModel(),
    driverNavigationComponent: DriverNavigationComponent = requireDriverNavigationComponent(),
) {
    val state = viewModel.outputs.uiState.collectAsState()
    LaunchedEffect(state.value.currentlySelected != null, block = {
        isRoot(state.value.currentlySelected != null)
    })

    MasterDetailsPane(
        windowSizeClass = windowSizeClass,
        master = {
            DriverStandingsScreen(
                actionUpClicked = actionUpClicked,
                uiState = state.value,
                windowSizeClass = windowSizeClass,
                driverClicked = viewModel.inputs::selectDriver,
                refresh = viewModel.inputs::refresh,
                comparisonClicked = viewModel.inputs::selectComparison
            )
        },
        detailsShow = state.value.currentlySelected != null,
        detailsActionUpClicked = viewModel.inputs::closeDriverDetails,
        details = {
            when (val selected = state.value.currentlySelected!!) {
                is Selected.Comparison -> {
                    driverNavigationComponent.DriverComparison(
                        windowSizeClass = windowSizeClass,
                        actionUpClicked = viewModel.inputs::closeDriverDetails,
                        season = state.value.season
                    )
                }
                is Selected.Driver -> {
                    driverNavigationComponent.DriverSeasonScreen(
                        windowSizeClass = windowSizeClass,
                        actionUpClicked = viewModel.inputs::closeDriverDetails,
                        driverId = selected.driver.driver.id,
                        driverName = selected.driver.driver.name,
                        season = selected.driver.season,
                        seasonClicked = { season: Int, round: Int, raceName: String, circuitId: String, circuitName: String, country: String, countryISO: String, dateString: String ->
                            // Do something
                        }
                    )
                }
            }
        }
    )

}

@Composable
internal fun DriverStandingsScreen(
    actionUpClicked: () -> Unit,
    windowSizeClass: WindowSizeClass,
    uiState: DriverStandingsScreenState,
    driverClicked: (SeasonDriverStandingSeason) -> Unit,
    comparisonClicked: () -> Unit,
    refresh: () -> Unit
) {
    SwipeRefresh(
        isLoading = uiState.isLoading,
        onRefresh = refresh
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(AppTheme.colors.backgroundPrimary),
            content = {
                item(key = "header") {
                    Header(
                        content = {
                            SeasonTitleVM(subtitle = stringResource(id = string.season_standings_driver))
                        },
                        action = when (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) {
                            true -> HeaderAction.MENU
                            false -> null
                        },
                        overrideIcons = {
                            if (uiState.maxPoints != 0.0) {
                                IconButton(onClick = comparisonClicked) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_menu_comparison),
                                        contentDescription = stringResource(id = string.driver_comparison_title),
                                        tint = AppTheme.colors.contentSecondary
                                    )
                                }
                            }
                        },
                        actionUpClicked = actionUpClicked
                    )
                }
                uiState.inProgress?.let { (raceName, round) ->
                    item(key = "banner") {
                        Banner(
                            message = stringResource(
                                id = string.results_accurate_for,
                                raceName,
                                round
                            ),
                            showLink = false
                        )
                    }
                }
                if (uiState.standings.isEmpty()) {
                    if (!uiState.networkAvailable) {
                        item(key = "network") {
                            NetworkError()
                        }
                    } else if (uiState.isLoading) {
                        item(key = "loading") {
                            SkeletonViewList()
                        }
                    }
                }
                items(uiState.standings, key = { "driver=${it.driver.id}" }) {
                    DriverStandings(
                        model = it,
                        itemClicked = driverClicked,
                        maxPoints = uiState.maxPoints
                    )
                }
                item(key = "footer") {
                    ProvidedBy()
                }
            }
        )
    }
}


@Composable
private fun DriverStandings(
    model: SeasonDriverStandingSeason,
    itemClicked: (SeasonDriverStandingSeason) -> Unit,
    maxPoints: Double,
    modifier: Modifier = Modifier,
) {
    val constructorColor =
        model.constructors.lastOrNull()?.constructor?.colour ?: AppTheme.colors.backgroundTertiary
    Row(
        modifier = modifier
            .constructorIndicator(constructorColor)
            .clickable(onClick = {
                itemClicked(model)
            }),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TextTitle(
            text = model.championshipPosition?.toString() ?: "-",
            bold = true,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(36.dp)
        )
        DriverIcon(
            photoUrl = model.driver.photoUrl,
            constructorColor = constructorColor,
            size = 40.dp
        )
        Row(
            modifier = Modifier
                .padding(
                    top = AppTheme.dimens.small,
                    start = AppTheme.dimens.small,
                    end = AppTheme.dimens.medium,
                    bottom = AppTheme.dimens.small
                )
                .wrapContentHeight()
        ) {
            Column(modifier = Modifier.weight(2f)) {
                TextTitle(
                    text = model.driver.name,
                    bold = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(
                            vertical = AppTheme.dimens.xxsmall,
                        )
                ) {
                    Flag(
                        iso = model.driver.nationalityISO,
                        nationality = model.driver.nationality,
                        modifier = Modifier.size(16.dp),
                    )
                    Spacer(Modifier.width(4.dp))
                    TextBody2(
                        text = model.constructors.joinToString { it.constructor.name },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            Spacer(Modifier.width(AppTheme.dimens.small))
            ProgressBar(
                modifier = Modifier
                    .weight(2f)
                    .height(48.dp)
                    .fillMaxHeight(),
                points = model.points,
                maxPoints = maxPoints,
                barColor = model.constructors.lastOrNull()?.constructor?.colour
                    ?: AppTheme.colors.primary
            )
        }
    }
}


@PreviewTheme
@Composable
private fun Preview(
    @PreviewParameter(SeasonDriverStandingSeasonProvider::class) driverStandings: SeasonDriverStandingSeason
) {
    AppThemePreview {
        LazyColumn(content = {
            item {
                DriverStandings(
                    model = driverStandings,
                    itemClicked = { },
                    maxPoints = 25.0
                )
            }
        })
    }
}