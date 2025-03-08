@file:OptIn(ExperimentalMaterial3Api::class)

package tmg.flashback.drivers.presentation.overview

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.Icon
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import tmg.flashback.googleanalytics.constants.AnalyticsConstants.analyticsDriverId
import tmg.flashback.drivers.R
import tmg.flashback.formula1.R.drawable
import tmg.flashback.strings.R.string
import tmg.flashback.drivers.contract.model.DriverStatHistoryType
import tmg.flashback.drivers.presentation.common.DriverBadges
import tmg.flashback.drivers.presentation.season.DriverSeasonScreenVM
import tmg.flashback.drivers.presentation.stathistory.DriverStatHistoryScreenVM
import tmg.flashback.formula1.model.Driver
import tmg.flashback.formula1.model.DriverEntry
import tmg.flashback.providers.DriverConstructorProvider
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.badge.Badge
import tmg.flashback.style.badge.BadgeView
import tmg.flashback.style.buttons.ButtonSecondary
import tmg.flashback.style.text.TextBody1
import tmg.flashback.googleanalytics.presentation.ScreenView
import tmg.flashback.ui.components.drivers.DriverIcon
import tmg.flashback.ui.components.errors.NetworkError
import tmg.flashback.ui.components.header.HeaderAction
import tmg.flashback.ui.components.layouts.MasterDetailsPane
import tmg.flashback.ui.components.loading.SkeletonViewList
import tmg.flashback.ui.components.messages.Message
import tmg.flashback.ui.components.navigation.PipeType
import tmg.flashback.ui.components.swiperefresh.SwipeRefresh
import tmg.flashback.ui.components.timeline.Timeline
import tmg.flashback.ui.utils.DrawableUtils.getFlagResourceAlpha3
import tmg.flashback.ui.utils.isInPreview
import tmg.utilities.extensions.format

private val headerImageSize: Dp = 120.dp

@Composable
fun DriverOverviewScreenVM(
    actionUpClicked: () -> Unit,
    windowSizeClass: WindowSizeClass,
    driverId: String,
    driverName: String,
    viewModel: DriverOverviewViewModel = hiltViewModel()
) {
    ScreenView(
        screenName = "Driver Overview", args = mapOf(
            analyticsDriverId to driverId
        )
    )

    val uiState = viewModel.outputs.uiState.collectAsState()
    MasterDetailsPane(
        windowSizeClass = windowSizeClass,
        master = {
            SwipeRefresh(
                isLoading = uiState.value.isLoading,
                onRefresh = viewModel.inputs::refresh
            ) {
                DriverOverviewScreen(
                    actionUpClicked = actionUpClicked,
                    windowSizeClass = windowSizeClass,
                    uiState = uiState.value,
                    linkClicked = viewModel.inputs::openUrl,
                    racedForClicked = {
                        viewModel.inputs.openSeason(it.season)
                    }
                )
            }
        },
        detailsShow = uiState.value.selectedSeason != null,
        details = {
            DriverSeasonScreenVM(
                actionUpClicked = viewModel.inputs::back,
                windowSizeClass = windowSizeClass,
                driverId = driverId,
                driverName = driverName,
                showHeader = false,
                season = uiState.value.selectedSeason!!
            )
        },
        detailsActionUpClicked = viewModel.inputs::back
    )
}

@Composable
fun DriverOverviewScreen(
    actionUpClicked: () -> Unit,
    windowSizeClass: WindowSizeClass,
    linkClicked: (String) -> Unit,
    uiState: DriverOverviewScreenState,
    racedForClicked: (DriverOverviewModel.RacedFor) -> Unit,
) {
    var showDriverStatHistoryType = rememberSaveable { mutableStateOf<DriverStatHistoryType?>(null) }
    val bottomSheetState = rememberModalBottomSheetState()
    if (showDriverStatHistoryType.value != null) {
        ModalBottomSheet(
            containerColor = AppTheme.colors.backgroundContainer,
            onDismissRequest = {
                showDriverStatHistoryType.value = null
            },
            sheetState = bottomSheetState,
            content = {
                DriverStatHistoryScreenVM(
                    driverId = uiState.driverId,
                    driverName = uiState.driverName,
                    driverStatHistoryType = showDriverStatHistoryType.value!!
                )
            }
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.backgroundPrimary),
        content = {
            item("header") {
                tmg.flashback.ui.components.header.Header(
                    text = uiState.driverName,
                    action = when (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) {
                        true -> HeaderAction.BACK
                        false -> null
                    },
                    actionUpClicked = actionUpClicked
                )
                if (uiState.driver != null) {
                    Header(
                        model = uiState.driver,
                        linkClicked = linkClicked
                    )
                }
            }
            if (uiState.list.isEmpty()) {
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
            items(uiState.list, key = { it.key }) {
                when (it) {
                    is DriverOverviewModel.Message -> {
                        Message(title = stringResource(id = it.label, *it.args.toTypedArray()))
                    }

                    is DriverOverviewModel.RacedFor -> {
                        History(
                            model = it,
                            clicked = racedForClicked
                        )
                    }

                    is DriverOverviewModel.Stat -> {
                        Stat(
                            model = it,
                            statHistoryClicked = {
                                showDriverStatHistoryType.value = it
                            }
                        )
                    }
                }
            }
        }
    )
}

@Composable
private fun Header(
    model: Driver,
    linkClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(
            horizontal = AppTheme.dimens.medium
        )
    ) {
        DriverIcon(
            photoUrl = model.photoUrl,
            code = model.code,
            number = model.number,
            size = headerImageSize
        )
        DriverBadges(
            driver = model
        )
        if (model.wikiUrl?.isNotEmpty() == true) {
            ButtonSecondary(
                text = stringResource(id = string.details_link_wikipedia),
                onClick = { linkClicked(model.wikiUrl!!) },
            )
            Spacer(Modifier.height(AppTheme.dimens.xsmall))
        }
    }
}

@Composable
private fun Stat(
    model: DriverOverviewModel.Stat,
    statHistoryClicked: (DriverStatHistoryType) -> Unit,
    modifier: Modifier = Modifier
) {
    val initialModifier = when (model.driverStatHistoryType != null) {
        true -> modifier.clickable(onClick = {
            statHistoryClicked(model.driverStatHistoryType)
        })

        false -> modifier
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = initialModifier
            .semantics(mergeDescendants = true) { }
            .fillMaxWidth()
            .padding(
                vertical = AppTheme.dimens.xsmall,
                horizontal = AppTheme.dimens.medium
            )
    ) {
        Icon(
            painter = painterResource(id = model.icon),
            contentDescription = null,
            tint = when (model.isWinning) {
                true -> AppTheme.colors.f1Championship
                false -> AppTheme.colors.contentSecondary
            }
        )
        Spacer(Modifier.width(8.dp))
        TextBody1(
            text = stringResource(id = model.label),
            modifier = Modifier.weight(1f)
        )
        Spacer(Modifier.width(8.dp))
        TextBody1(
            text = model.value,
            bold = true
        )
    }
}

@Composable
private fun History(
    model: DriverOverviewModel.RacedFor,
    clicked: (DriverOverviewModel.RacedFor) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clickable(onClick = { clicked(model) })
            .padding(horizontal = AppTheme.dimens.medium)
    ) {
        Timeline(
            timelineColor = AppTheme.colors.contentSecondary,
            overrideDotColor = when {
                model.isChampionship -> AppTheme.colors.f1Championship
                else -> null
            },
            isEnabled = true,
            showTop = model.type.showTop,
            showBottom = model.type.showBottom
        ) {
            TextBody1(
                text = model.season.toString(),
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            )
            Column(
                modifier = Modifier.padding(vertical = 2.dp),
                horizontalAlignment = Alignment.End
            ) {
                model.constructors.forEach { constructor ->
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier
                            .height(IntrinsicSize.Min)
                    ) {
                        TextBody1(
                            text = constructor.name,
                            bold = true,
                            modifier = Modifier
                                .padding(
                                    end = AppTheme.dimens.small,
                                    top = 2.dp,
                                    bottom = 2.dp
                                )
                        )
                        Box(
                            modifier = Modifier
                                .width(8.dp)
                                .fillMaxHeight()
                                .background(constructor.colour)
                        )
                    }
                }
            }
        }
    }
}


@PreviewTheme
@Composable
private fun Preview(
    @PreviewParameter(DriverConstructorProvider::class) driverConstructor: DriverEntry
) {
    AppThemePreview {
        DriverOverviewScreen(
            actionUpClicked = { },
            windowSizeClass = WindowSizeClass.calculateFromSize(DpSize.Unspecified),
            uiState = DriverOverviewScreenState(
                driverId = "driverId",
                driverName = "driverName",
                list = listOf(
                    fakeStat,
                    fakeStatWinning,
                    driverConstructor.racedFor(),
                    driverConstructor.racedFor2()
                )
            ),
            racedForClicked = { },
            linkClicked = { }
        )
    }
}

private val fakeStatWinning = DriverOverviewModel.Stat(
    isWinning = true,
    icon = drawable.ic_status_front_wing,
    label = string.driver_overview_stat_career_points_finishes,
    value = "12"
)
private val fakeStat = DriverOverviewModel.Stat(
    isWinning = false,
    icon = drawable.ic_status_battery,
    label = string.driver_overview_stat_career_points,
    value = "4"
)

private fun DriverEntry.racedFor() = DriverOverviewModel.RacedFor(
    season = 2022,
    type = PipeType.START,
    constructors = listOf(
        this.constructor.copy(id = "1", name = "Toro Rosso")
    ),
    isChampionship = false
)

private fun DriverEntry.racedFor2() = DriverOverviewModel.RacedFor(
    season = 2021,
    type = PipeType.END,
    constructors = listOf(
        this.constructor.copy(id = "1", name = "McLaren"),
        this.constructor.copy(id = "2", name = "Ferrari")
    ),
    isChampionship = false
)