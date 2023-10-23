package tmg.flashback.season.presentation.dashboard.constructors

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import tmg.flashback.constructors.contract.ConstructorsNavigationComponent
import tmg.flashback.constructors.contract.requireConstructorsNavigationComponent
import tmg.flashback.formula1.extensions.pointsDisplay
import tmg.flashback.formula1.model.SeasonConstructorStandingSeason
import tmg.flashback.providers.SeasonConstructorStandingSeasonProvider
import tmg.flashback.season.R
import tmg.flashback.season.presentation.dashboard.shared.seasonpicker.SeasonTitleVM
import tmg.flashback.season.presentation.messaging.Banner
import tmg.flashback.season.presentation.messaging.ProvidedBy
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.text.TextTitle
import tmg.flashback.ui.components.constructorIndicator
import tmg.flashback.ui.components.drivers.DriverPoints
import tmg.flashback.ui.components.errors.NetworkError
import tmg.flashback.ui.components.header.Header
import tmg.flashback.ui.components.header.HeaderAction
import tmg.flashback.ui.components.layouts.MasterDetailsPane
import tmg.flashback.ui.components.loading.SkeletonViewList
import tmg.flashback.ui.components.navigation.appBarHeight
import tmg.flashback.ui.components.progressbar.ProgressBar
import tmg.flashback.ui.components.swiperefresh.SwipeRefresh
import tmg.flashback.ui.foldables.isWidthExpanded
import kotlin.math.roundToInt

@Composable
fun ConstructorStandingsScreenVM(
    actionUpClicked: () -> Unit,
    windowSizeClass: WindowSizeClass,
    isRoot: (Boolean) -> Unit,
    viewModel: ConstructorStandingsViewModel = hiltViewModel(),
    constructorsNavigationComponent: ConstructorsNavigationComponent = requireConstructorsNavigationComponent()
) {
    val state = viewModel.outputs.uiState.collectAsState()
    LaunchedEffect(state.value.currentlySelected != null, block = {
        isRoot(state.value.currentlySelected != null)
    })

    MasterDetailsPane(
        windowSizeClass = windowSizeClass,
        master = {
            ConstructorStandingsScreen(
                actionUpClicked = actionUpClicked,
                windowSizeClass = windowSizeClass,
                uiState = state.value,
                constructorClicked = viewModel.inputs::selectConstructor,
                refresh = viewModel.inputs::refresh
            )
        },
        detailsShow = state.value.currentlySelected != null,
        detailsActionUpClicked = viewModel.inputs::closeConstructor,
        details = {
            val selected = state.value.currentlySelected!!
            constructorsNavigationComponent.ConstructorSeasonScreen(
                actionUpClicked = viewModel.inputs::closeConstructor,
                windowSizeClass = windowSizeClass,
                constructorId = selected.constructor.id,
                constructorName = selected.constructor.name,
                season = selected.season,
                driverClicked = { id, name, season -> }
            )
        }
    )
}

@Composable
fun ConstructorStandingsScreen(
    actionUpClicked: () -> Unit,
    windowSizeClass: WindowSizeClass,
    uiState: ConstructorStandingsScreenState,
    constructorClicked: (SeasonConstructorStandingSeason) -> Unit,
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
                            SeasonTitleVM(subtitle = stringResource(id = R.string.season_standings_constructor))
                        },
                        action = when (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) {
                            true -> HeaderAction.MENU
                            false -> null
                        },
                        actionUpClicked = actionUpClicked
                    )
                }
                uiState.inProgress?.let { (raceName, round) ->
                    item(key = "banner") {
                        Banner(
                            message = stringResource(id = R.string.results_accurate_for, raceName, round),
                            showLink = false
                        )
                    }
                }
                if (uiState.standings.isNullOrEmpty()) {
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
                items(uiState.standings, key = { "constructor=${it.constructor.id}" }) {
                    ConstructorStandings(
                        model = it,
                        itemClicked = constructorClicked,
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
private fun ConstructorStandings(
    model: SeasonConstructorStandingSeason,
    itemClicked: (SeasonConstructorStandingSeason) -> Unit,
    maxPoints: Double,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .constructorIndicator(model.constructor.colour)
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
        Row(modifier = Modifier.padding(
            top = AppTheme.dimens.small,
            start = 0.dp,
            end = AppTheme.dimens.medium,
            bottom = AppTheme.dimens.small
        )) {
            Column(modifier = Modifier.weight(3f)) {
                TextTitle(
                    text = model.constructor.name,
                    bold = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(2.dp))
                model.drivers.forEach {
                    DriverPoints(
                        name = it.driver.name,
                        nationality = it.driver.nationality,
                        nationalityISO = it.driver.nationalityISO,
                        points = it.points
                    )
                }
            }
            Spacer(Modifier.width(AppTheme.dimens.small))
            val progress = (model.points / maxPoints).toFloat().coerceIn(0f, 1f)
            ProgressBar(
                modifier = Modifier
                    .weight(2f)
                    .height(48.dp)
                    .fillMaxHeight(),
                endProgress = progress,
                barColor = model.constructor.colour,
                label = {
                    when (it) {
                        0f -> "0"
                        progress -> model.points.pointsDisplay()
                        else -> (it * maxPoints).takeIf { !it.isNaN() }?.roundToInt()?.toString() ?: model.points.pointsDisplay()
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
            model = constructorStandings,
            itemClicked = { },
            maxPoints = 25.0
        )
    }
}