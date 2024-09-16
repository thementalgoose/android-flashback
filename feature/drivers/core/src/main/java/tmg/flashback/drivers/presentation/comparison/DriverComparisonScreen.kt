package tmg.flashback.drivers.presentation.comparison

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import tmg.flashback.drivers.R
import tmg.flashback.formula1.extensions.pointsDisplay
import tmg.flashback.formula1.model.Driver
import tmg.flashback.googleanalytics.constants.AnalyticsConstants.analyticsSeason
import tmg.flashback.googleanalytics.presentation.ScreenView
import tmg.flashback.style.AppTheme
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextTitle
import tmg.flashback.ui.components.header.HeaderAction
import tmg.flashback.ui.components.progressbar.ProgressBar
import tmg.flashback.ui.components.swiperefresh.SwipeRefresh
import tmg.flashback.strings.R.string
import tmg.flashback.style.buttons.ButtonPrimary
import tmg.flashback.style.buttons.ButtonSecondary
import tmg.flashback.style.buttons.ButtonSecondarySegments
import tmg.flashback.ui.components.drivers.DriverIcon

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
                    text = season.toString(),
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
                    }
                }
            }
            if (driverLeft != null && driverRight != null) {
                item("driver-details") {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = AppTheme.dimens.medium)
                    ) {
                        Row(Modifier.weight(1f)) {
                            DriverIcon(driverLeft.photoUrl)
                        }
                        Box(Modifier.width(2.dp))
                        Row(Modifier.weight(1f)) {
                            DriverIcon(driverRight.photoUrl)
                        }
                    }
                }
            }
            if (comparison != null) {
                item("comparison-races") {
                    val (leftPercentage, rightPercentage) = comparison.getPercentages(value = { it.racesHeadToHead.toFloat() })
                    ItemComparison(
                        label = stringResource(string.driver_comparison_stat_races),
                        left = comparison.left.racesHeadToHead.toString(),
                        leftProgress = leftPercentage,
                        right = comparison.right.racesHeadToHead.toString(),
                        rightProgress = rightPercentage
                    )
                }
                item("comparison-qualifying") {
                    val (leftPercentage, rightPercentage) = comparison.getPercentages(value = { it.qualifyingHeadToHead.toFloat() })
                    ItemComparison(
                        label = stringResource(string.driver_comparison_stat_qualifying),
                        left = comparison.left.qualifyingHeadToHead.toString(),
                        leftProgress = leftPercentage,
                        right = comparison.right.qualifyingHeadToHead.toString(),
                        rightProgress = rightPercentage
                    )
                }
                item("comparison-points") {
                    val (leftPercentage, rightPercentage) = comparison.getPercentages(value = { it.points?.toFloat() ?: 0f })
                    ItemComparison(
                        label = stringResource(string.driver_comparison_stat_points),
                        left = comparison.left.points?.pointsDisplay() ?: "",
                        leftProgress = leftPercentage,
                        right = comparison.right.points?.pointsDisplay() ?: "",
                        rightProgress = rightPercentage
                    )
                }
                item("comparison-points-finishes") {
                    val (leftPercentage, rightPercentage) = comparison.getPercentages(value = { it.pointsFinishes.toFloat() })
                    ItemComparison(
                        label = stringResource(string.driver_comparison_stat_points_finishes),
                        left = comparison.left.pointsFinishes.toString(),
                        leftProgress = leftPercentage,
                        right = comparison.right.pointsFinishes.toString(),
                        rightProgress = rightPercentage
                    )
                }
                item("comparison-podiums") {
                    val (leftPercentage, rightPercentage) = comparison.getPercentages(value = { it.podiums.toFloat() })
                    ItemComparison(
                        label = stringResource(string.driver_comparison_stat_podiums),
                        left = comparison.left.podiums.toString(),
                        leftProgress = leftPercentage,
                        right = comparison.right.podiums.toString(),
                        rightProgress = rightPercentage
                    )
                }
                item("comparison-wins") {
                    val (leftPercentage, rightPercentage) = comparison.getPercentages(value = { it.wins.toFloat() })
                    ItemComparison(
                        label = stringResource(string.driver_comparison_stat_wins),
                        left = comparison.left.wins.toString(),
                        leftProgress = leftPercentage,
                        right = comparison.right.wins.toString(),
                        rightProgress = rightPercentage
                    )
                }
                item("comparison-dnfs") {
                    val (leftPercentage, rightPercentage) = comparison.getPercentages(value = { it.dnfs.toFloat() })
                    ItemComparison(
                        label = stringResource(string.driver_comparison_stat_dnfs),
                        left = comparison.left.dnfs.toString(),
                        leftProgress = leftPercentage,
                        right = comparison.right.dnfs.toString(),
                        rightProgress = rightPercentage
                    )
                }
            }
        }
    )
}

@Composable
private fun ItemComparison(
    label: String,
    left: String,
    leftProgress: Float,
    right: String,
    rightProgress: Float,
    modifier: Modifier = Modifier,
) {
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
                    radius = 0.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .border(2.dp, AppTheme.colors.contentTertiary),
                    backgroundColor = AppTheme.colors.backgroundTertiary,
                    barColor = AppTheme.colors.primary,
                    endProgress = leftProgress,
                    label = { left },
                    reverse = true,
                )
            }
            Box(Modifier.width(2.dp))
            Row(Modifier.weight(1f)) {
                ProgressBar(
                    radius = 0.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .border(2.dp, AppTheme.colors.contentTertiary),
                    backgroundColor = AppTheme.colors.backgroundTertiary,
                    barColor = AppTheme.colors.primary,
                    endProgress = rightProgress,
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
    right = ComparisonValue(
        racesHeadToHead = 2,
        qualifyingHeadToHead = 10,
        points = 144.0,
        pointsFinishes = 5,
        podiums = 0,
        wins = 1,
        dnfs = 3
    )
)