package tmg.flashback.drivers.presentation.comparison

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import tmg.flashback.drivers.R
import tmg.flashback.drivers.presentation.common.DriverBadges
import tmg.flashback.formula1.extensions.pointsDisplay
import tmg.flashback.formula1.model.Driver
import tmg.flashback.googleanalytics.constants.AnalyticsConstants.analyticsSeason
import tmg.flashback.googleanalytics.presentation.ScreenView
import tmg.flashback.strings.R.string
import tmg.flashback.style.AppTheme
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.buttons.ButtonSecondary
import tmg.flashback.style.text.TextTitle
import tmg.flashback.ui.components.drivers.DriverIcon
import tmg.flashback.ui.components.errors.ErrorMessage
import tmg.flashback.ui.components.header.HeaderAction
import tmg.flashback.ui.components.progressbar.ProgressBar
import tmg.flashback.ui.components.swiperefresh.SwipeRefresh

private val headerImageSize: Dp = 120.dp

@Composable
fun DriverComparisonScreenVM(
    actionUpClicked: () -> Unit,
    windowSizeClass: WindowSizeClass,
    season: Int,
    viewModel: DriverComparisonViewModel = hiltViewModel()
) {
    ScreenView(
        screenName = "Driver Comparison", args = mapOf(
            analyticsSeason to season.toString()
        )
    )

    LaunchedEffect(season) {
        viewModel.inputs.setup(season)
    }

    val state = viewModel.outputs.uiState.collectAsState()

    SwipeRefresh(
        isLoading = state.value.isLoading,
        onRefresh = viewModel.inputs::refresh
    ) {
        DriverComparisonScreen(
            actionUpClicked = actionUpClicked,
            windowSizeClass = windowSizeClass,
            season = season,
            driverList = state.value.driverList,
            driverLeft = state.value.driverLeft,
            driverRight = state.value.driverRight,
            selectDriverLeft = viewModel.inputs::selectDriverLeft,
            selectDriverRight = viewModel.inputs::selectDriverRight,
            swapDriver = viewModel.inputs::swapDrivers,
            comparison = state.value.comparison
        )
    }
}

@Composable
private fun DriverComparisonScreen(
    actionUpClicked: () -> Unit,
    windowSizeClass: WindowSizeClass,
    season: Int,
    driverList: List<Driver>,
    driverLeft: Driver?,
    driverRight: Driver?,
    selectDriverLeft: (String) -> Unit,
    selectDriverRight: (String) -> Unit,
    swapDriver: () -> Unit,
    comparison: Comparison?
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.backgroundPrimary),
        content = {
            item("header") {
                tmg.flashback.ui.components.header.Header(
                    text = season.toString() + "\n" + stringResource(string.driver_comparison_title),
                    action = when (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) {
                        true -> HeaderAction.BACK
                        false -> null
                    },
                    actionUpClicked = actionUpClicked
                )
            }
            item("driver-select") {
                val driverLeftExpanded = remember { mutableStateOf(false) }
                val driverRightExpanded = remember { mutableStateOf(false) }
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppTheme.dimens.medium)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        ButtonSecondary(
                            modifier = Modifier.fillMaxWidth(),
                            text = driverLeft?.name ?: "...",
                            onClick = {
                                driverLeftExpanded.value = true
                            }
                        )
                        DriverList(
                            driverList = driverList,
                            expanded = driverLeftExpanded,
                            driverClicked = { selectDriverLeft(it.id) }
                        )
                        if (driverLeft != null && driverRight != null) {
                            DriverIcon(
                                modifier = Modifier.align(Alignment.Start),
                                photoUrl = driverLeft.photoUrl,
                                size = headerImageSize
                            )
                            DriverBadges(
                                driver = driverLeft,
                                horizontalAlignment = Alignment.Start,
                                constructors = comparison?.leftConstructors ?: emptyList()
                            )
                        }
                    }
                    IconButton(
                        onClick = swapDriver,
                        modifier = Modifier.padding(horizontal = AppTheme.dimens.small)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_driver_comparison_swap),
                            contentDescription = stringResource(string.driver_comparison_swap_drivers),
                            tint = AppTheme.colors.contentSecondary
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        ButtonSecondary(
                            modifier = Modifier.fillMaxWidth(),
                            text = driverRight?.name ?: "...",
                            onClick = {
                                driverRightExpanded.value = true
                            }
                        )
                        DriverList(
                            driverList = driverList,
                            expanded = driverRightExpanded,
                            driverClicked = { selectDriverRight(it.id) }
                        )
                        if (driverLeft != null && driverRight != null) {
                            DriverIcon(
                                modifier = Modifier.align(Alignment.End),
                                photoUrl = driverRight.photoUrl,
                                size = headerImageSize
                            )
                            DriverBadges(
                                driver = driverRight,
                                horizontalAlignment = Alignment.End,
                                constructors = comparison?.rightConstructors ?: emptyList()
                            )
                        }
                    }
                }
            }
            if (comparison != null) {
                item("comparison-races") {
                    ItemComparison(
                        label = stringResource(string.driver_comparison_stat_races),
                        comparison = comparison,
                        percentageResolver = { it.racesHeadToHead.toFloat() },
                        valueResolver = { it.racesHeadToHead.toString() },
                    )
                }
                item("comparison-qualifying") {
                    ItemComparison(
                        label = stringResource(string.driver_comparison_stat_qualifying),
                        comparison = comparison,
                        percentageResolver = { it.qualifyingHeadToHead.toFloat() },
                        valueResolver = { it.qualifyingHeadToHead.toString() },
                    )
                }
                item("comparison-points") {
                    ItemComparison(
                        label = stringResource(string.driver_comparison_stat_points),
                        comparison = comparison,
                        percentageResolver = { it.points?.toFloat() ?: 0f },
                        valueResolver = { it.points?.pointsDisplay() ?: "" },
                    )
                }
                item("comparison-points-finishes") {
                    ItemComparison(
                        label = stringResource(string.driver_comparison_stat_points_finishes),
                        comparison = comparison,
                        percentageResolver = { it.pointsFinishes.toFloat() },
                        valueResolver = { it.pointsFinishes.toString() },
                    )
                }
                item("comparison-podiums") {
                    ItemComparison(
                        label = stringResource(string.driver_comparison_stat_podiums),
                        comparison = comparison,
                        percentageResolver = { it.podiums.toFloat() },
                        valueResolver = { it.podiums.toString() },
                    )
                }
                item("comparison-wins") {
                    ItemComparison(
                        label = stringResource(string.driver_comparison_stat_wins),
                        comparison = comparison,
                        percentageResolver = { it.wins.toFloat() },
                        valueResolver = { it.wins.toString() },
                    )
                }
                item("comparison-dnfs") {
                    ItemComparison(
                        label = stringResource(string.driver_comparison_stat_dnfs),
                        comparison = comparison,
                        percentageResolver = { it.dnfs.toFloat() },
                        valueResolver = { it.dnfs.toString() },
                    )
                }
            } else {
                if (driverLeft != null && driverRight != null) {
                    item("no-common-races") {
                        ErrorMessage(
                            label = string.driver_comparison_no_races_in_common
                        )
                    }
                } else {
                    item("get-started") {
                        // TODO: Put something here
                    }
                }
            }
            item("footer") {
                Spacer(Modifier.height(AppTheme.dimens.large))
            }
        }
    )
}

@Composable
private fun ItemComparison(
    label: String,
    comparison: Comparison,
    percentageResolver: (ComparisonValue) -> Float,
    valueResolver: (ComparisonValue) -> String,
    modifier: Modifier = Modifier,
) {
    val (leftPercentage, rightPercentage) = comparison.getPercentages(value = percentageResolver)
    val left = valueResolver(comparison.left)
    val leftColour = comparison.leftConstructors.lastOrNull()?.colour ?: Color.Gray
    val right = valueResolver(comparison.right)
    val rightColour = comparison.rightConstructors.lastOrNull()?.colour ?: Color.Gray
    Column(modifier = modifier.padding(top = AppTheme.dimens.small)) {
        TextTitle(
            text = label,
            bold = true,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = AppTheme.dimens.medium,
                    vertical = AppTheme.dimens.small
                )
        )
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = AppTheme.dimens.medium)
        ) {
            Row(Modifier.weight(1f)) {
                ProgressBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    backgroundColor = AppTheme.colors.backgroundSecondary,
                    barColor = leftColour,
                    endProgress = leftPercentage,
                    label = { left },
                    reverse = true,
                )
            }
            Box(Modifier.width(2.dp))
            Row(Modifier.weight(1f)) {
                ProgressBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    backgroundColor = AppTheme.colors.backgroundSecondary,
                    barColor = rightColour,
                    endProgress = rightPercentage,
                    label = { right },
                    reverse = false,
                )
            }
        }
    }
}

@Composable
private fun DriverList(
    driverList: List<Driver>,
    expanded: MutableState<Boolean>,
    driverClicked: (Driver) -> Unit,
    modifier: Modifier = Modifier,
) {
    DropdownMenu(
        offset = DpOffset(AppTheme.dimens.medium, 0.dp),
        modifier = modifier.background(AppTheme.colors.backgroundContainer),
        expanded = expanded.value,
        onDismissRequest = { expanded.value = false },
        content = {
            driverList.forEach { driver ->
                DropdownMenuItem(
                    text = {
                        TextTitle(
                            text = driver.name,
                            bold = true
                        )
                    },
                    onClick = {
                        driverClicked(driver)
                        expanded.value = false
                    }
                )
            }
        }
    )
}



@PreviewTheme
@Composable
private fun Preview() {
    DriverComparisonScreen(
        actionUpClicked = {},
        windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(400.dp, 700.dp)),
        season = 2024,
        driverList = emptyList(),
        driverLeft = null,
        driverRight = null,
        selectDriverLeft = { },
        selectDriverRight = { },
        swapDriver = { },
        comparison = fakeComparison
    )
}

private val fakeComparison = Comparison(
    left = ComparisonValue(
        racesHeadToHead = 3,
        qualifyingHeadToHead = 2,
        points = 100.0,
        pointsFinishes = 4,
        podiums = 0,
        wins = 1,
        dnfs = 1,
    ),
    leftConstructors = listOf(),
    right = ComparisonValue(
        racesHeadToHead = 2,
        qualifyingHeadToHead = 10,
        points = 144.0,
        pointsFinishes = 5,
        podiums = 0,
        wins = 1,
        dnfs = 3
    ),
    rightConstructors = listOf()
)