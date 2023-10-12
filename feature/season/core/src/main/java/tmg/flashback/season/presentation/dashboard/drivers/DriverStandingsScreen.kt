package tmg.flashback.season.presentation.dashboard.drivers

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import tmg.flashback.formula1.extensions.pointsDisplay
import tmg.flashback.formula1.model.SeasonDriverStandingSeason
import tmg.flashback.providers.SeasonDriverStandingSeasonProvider
import tmg.flashback.season.R
import tmg.flashback.season.presentation.messaging.Banner
import tmg.flashback.season.presentation.messaging.ProvidedBy
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.text.TextBody2
import tmg.flashback.style.text.TextTitle
import tmg.flashback.ui.components.constructorIndicator
import tmg.flashback.ui.components.drivers.DriverIcon
import tmg.flashback.ui.components.flag.Flag
import tmg.flashback.ui.components.header.Header
import tmg.flashback.ui.components.header.HeaderAction
import tmg.flashback.ui.components.layouts.MasterDetailsPane
import tmg.flashback.ui.components.navigation.appBarHeight
import tmg.flashback.ui.components.progressbar.ProgressBar
import tmg.flashback.ui.components.swiperefresh.SwipeRefresh
import tmg.flashback.ui.foldables.isWidthExpanded
import kotlin.math.roundToInt

@Composable
fun DriverStandingsScreenVM(
    actionUpClicked: () -> Unit,
    windowSizeClass: WindowSizeClass,
    viewModel: DriverStandingsViewModel = hiltViewModel(),
) {
    val state = viewModel.outputs.uiState.collectAsState()

    DriverStandingsScreen(
        actionUpClicked = actionUpClicked,
        uiState = state.value,
        windowSizeClass = windowSizeClass,
        driverClicked = viewModel.inputs::selectDriver,
        closeDriverDetails = viewModel.inputs::closeDriverDetails,
        refresh = viewModel.inputs::refresh
    )
}

@Composable
internal fun DriverStandingsScreen(
    actionUpClicked: () -> Unit,
    windowSizeClass: WindowSizeClass,
    uiState: DriverStandingsScreenState,
    driverClicked: (SeasonDriverStandingSeason) -> Unit,
    closeDriverDetails: () -> Unit,
    refresh: () -> Unit
) {
    MasterDetailsPane(
        windowSizeClass = windowSizeClass,
        master = {
            SwipeRefresh(
                isLoading = uiState.isLoading,
                onRefresh = refresh
            ) {
                LazyColumn(
                    content = {
                        item(key = "header") {
                            Header(
                                text = stringResource(id = R.string.season_standings_driver, uiState.season.toString()),
                                action = when (windowSizeClass.isWidthExpanded) {
                                    false -> HeaderAction.MENU
                                    true -> null
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
                        items(uiState.standings, key = { "driver=${it.driver.id}" }) {
                            DriverStandings(
                                model = it,
                                itemClicked = driverClicked,
                                maxPoints = uiState.maxPoints
                            )
                        }
                        item(key = "footer") {
                            ProvidedBy()
                            Spacer(Modifier.height(appBarHeight))
                        }
                    }
                )
            }
        },
        detailsActionUpClicked = closeDriverDetails,
        detailsShow = false, // uiState.currentlySelected != null,
        details = {
            Box(
                Modifier
                    .size(100.dp)
                    .background(Color.Magenta))
//            DriverSeasonScreenVM(
//                driverId = uiState.currentlySelected!!.driver.id,
//                driverName = uiState.currentlySelected.driver.name,
//                season = uiState.currentlySelected.season,
//                actionUpClicked = closeDriverDetails
//            )
        }
    )
}


@Composable
private fun DriverStandings(
    model: SeasonDriverStandingSeason,
    itemClicked: (SeasonDriverStandingSeason) -> Unit,
    maxPoints: Double,
    modifier: Modifier = Modifier,
) {
    val constructorColor = model.constructors.lastOrNull()?.constructor?.colour ?: AppTheme.colors.backgroundTertiary
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
        Row(modifier = Modifier
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
                Row(modifier = Modifier
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
            val progress = (model.points / maxPoints).toFloat().coerceIn(0f, 1f)
            ProgressBar(
                modifier = Modifier
                    .weight(2f)
                    .height(48.dp)
                    .fillMaxHeight(),
                endProgress = progress,
                barColor = model.constructors.lastOrNull()?.constructor?.colour ?: AppTheme.colors.primary,
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