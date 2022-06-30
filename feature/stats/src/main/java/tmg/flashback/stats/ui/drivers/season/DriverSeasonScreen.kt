package tmg.flashback.stats.ui.drivers.season

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.viewModel
import org.threeten.bp.LocalDate
import tmg.flashback.formula1.enums.isStatusFinished
import tmg.flashback.formula1.extensions.pointsDisplay
import tmg.flashback.formula1.model.DriverConstructor
import tmg.flashback.providers.DriverConstructorProvider
import tmg.flashback.stats.R
import tmg.flashback.stats.components.Timeline
import tmg.flashback.stats.ui.drivers.overview.PipeType
import tmg.flashback.stats.ui.weekend.shared.DriverInfo
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextCaption
import tmg.flashback.ui.components.errors.NetworkError
import tmg.flashback.ui.components.header.Header
import tmg.flashback.ui.components.loading.SkeletonViewList
import tmg.flashback.ui.components.messages.Message
import tmg.flashback.ui.components.progressbar.ProgressBar
import tmg.flashback.ui.model.AnimationSpeed
import tmg.utilities.extensions.ordinalAbbreviation
import kotlin.math.roundToInt

/**
 * If the width of the container is more than this value, put the stuff side by side
 */
private val resultColumnFlexBorder: Dp = 300.dp
private val resultColumnWidth: Dp = 60.dp

@Composable
fun DriverSeasonScreenVM(
    driverId: String,
    driverName: String,
    season: Int,
    actionUpClicked: () -> Unit
) {
    val viewModel by viewModel<DriverSeasonViewModel>()
    viewModel.inputs.setup(driverId, season)

    val list = viewModel.outputs.list.observeAsState(listOf(DriverSeasonModel.Loading))
    DriverSeasonScreen(
        list = list.value,
        driverName = driverName,
        season = season,
        actionUpClicked = actionUpClicked,
        resultClicked = viewModel.inputs::clickSeasonRound
    )
}

@Composable
fun DriverSeasonScreen(
    list: List<DriverSeasonModel>,
    driverName: String,
    season: Int,
    actionUpClicked: () -> Unit,
    resultClicked: (DriverSeasonModel.Result) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.backgroundPrimary),
        content = {
            item(key = "header") {
                Header(
                    text = "${driverName}\n${season}",
                    icon = painterResource(id = R.drawable.ic_back),
                    iconContentDescription = stringResource(id = R.string.ab_back),
                    actionUpClicked = actionUpClicked
                )
            }
            items(list, key = { it.key }) {
                when (it) {
                    is DriverSeasonModel.Message -> {
                        Message(title = stringResource(id = it.label, *it.args.toTypedArray()))
                    }
                    is DriverSeasonModel.RacedFor -> {
                        History(model = it)
                    }
                    is DriverSeasonModel.Stat -> {
                        Stat(model = it)
                    }
                    is DriverSeasonModel.Result -> {
                        Result(
                            model = it,
                            clickResult = resultClicked
                        )
                    }
                    DriverSeasonModel.InternalError -> {
                        NetworkError(error = NetworkError.INTERNAL_ERROR)
                    }
                    DriverSeasonModel.Loading -> {
                        SkeletonViewList()
                    }
                    DriverSeasonModel.NetworkError -> {
                        NetworkError(error = NetworkError.NETWORK_ERROR)
                    }
                    DriverSeasonModel.ResultHeader -> {
                        ResultHeader()
                    }
                }
            }
        }
    )
}

@Composable
private fun Stat(
    model: DriverSeasonModel.Stat,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(
                vertical = AppTheme.dimensions.paddingXSmall,
                horizontal = AppTheme.dimensions.paddingMedium
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
    model: DriverSeasonModel.RacedFor,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier
        .padding(horizontal = AppTheme.dimensions.paddingMedium)
    ) {
        Timeline(
            timelineColor = AppTheme.colors.contentSecondary,
            isEnabled = true,
            showTop = model.type.showTop,
            showBottom = model.type.showBottom
        ) {
            TextBody1(
                text = "",
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            )
            Column(
                modifier = Modifier.padding(vertical = 2.dp),
                horizontalAlignment = Alignment.End
            ) {
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .height(IntrinsicSize.Min)
                ) {
                    TextBody1(
                        text = model.constructors.name,
                        bold = true,
                        modifier = Modifier
                            .padding(
                                end = AppTheme.dimensions.paddingSmall,
                                top = 2.dp,
                                bottom = 2.dp
                            )
                    )
                    Box(modifier = Modifier
                        .width(8.dp)
                        .fillMaxHeight()
                        .background(model.constructors.colour)
                    )
                }
            }
        }
    }
}

@Composable
private fun ResultHeader(
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier
        .padding(end = AppTheme.dimensions.paddingMedium)
    ) {
        Box(Modifier.weight(1f))
        Box(
            Modifier
                .width(resultColumnWidth)
                .padding(vertical = AppTheme.dimensions.paddingXSmall)
        ) {
            Icon(
                modifier = Modifier.align(Alignment.Center),
                painter = painterResource(id = R.drawable.ic_status_results_qualifying),
                contentDescription = null,
                tint = AppTheme.colors.contentSecondary
            )
        }
        Box(
            Modifier
                .width(resultColumnWidth)
                .padding(vertical = AppTheme.dimensions.paddingXSmall)
        ) {
            Icon(
                modifier = Modifier.align(Alignment.Center),
                painter = painterResource(id = R.drawable.ic_status_finished),
                contentDescription = null,
                tint = AppTheme.colors.contentSecondary
            )
        }
        Box(
            Modifier
                .width(resultColumnWidth)
                .padding(vertical = AppTheme.dimensions.paddingXSmall)
        ) {
            Icon(
                modifier = Modifier.align(Alignment.Center),
                painter = painterResource(id = R.drawable.ic_race_points),
                contentDescription = null,
                tint = AppTheme.colors.contentSecondary
            )
        }
    }
}

@Composable
private fun Result(
    model: DriverSeasonModel.Result,
    clickResult: (DriverSeasonModel.Result) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier
        .clickable(onClick = { clickResult(model) })
        .padding(
            top = AppTheme.dimensions.paddingXSmall,
            bottom = AppTheme.dimensions.paddingXSmall,
            end = AppTheme.dimensions.paddingMedium
        )
    ) {
        Column(
            Modifier.weight(1f)
        ) {
            DriverInfo(
                driverName = model.raceName,
                driverNationalityISO = model.raceCountryISO,
                constructorColor = model.constructor.colour.copy(alpha = 0.2f),
                constructorName = model.circuitName,
                position = model.round
            )
            if (model.showConstructorLabel) {
                TextCaption(text = model.constructor.name)
            }
        }
        Box(
            Modifier
                .width(resultColumnWidth)
                .padding(vertical = AppTheme.dimensions.paddingXSmall)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = AppTheme.dimensions.paddingXSmall)
            ) {
                TextBody1(
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    bold = true,
                    text = model.qualified?.ordinalAbbreviation ?: "-"
                )
                if (!model.raceStatus.isStatusFinished()) {
                    TextCaption(
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        text = ""
                    )
                }
            }
        }
        Box(
            Modifier
                .width(resultColumnWidth)
                .padding(vertical = AppTheme.dimensions.paddingXSmall)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = AppTheme.dimensions.paddingXSmall)
            ) {
                TextBody1(
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    bold = true,
                    text = model.finished?.ordinalAbbreviation ?: "-"
                )
                if (!model.raceStatus.isStatusFinished()) {
                    TextCaption(
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        text = model.raceStatus
                    )
                }
            }
        }
        Box(
            Modifier
                .width(resultColumnWidth)
                .padding(vertical = AppTheme.dimensions.paddingXSmall)
        ) {
            val progress = (model.points / model.maxPoints).toFloat().coerceIn(0f, 1f)
            ProgressBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(36.dp),
                endProgress = progress,
                barColor = model.constructor.colour,
                label = {
                    when (it) {
                        0f -> "0"
                        progress -> model.points.pointsDisplay()
                        else -> (it * model.maxPoints).takeIf { !it.isNaN() }?.roundToInt()?.toString() ?: model.points.pointsDisplay()
                    }
                }
            )
        }
    }
}

@PreviewTheme
@Composable
private fun Preview(
    @PreviewParameter(DriverConstructorProvider::class) driverConstructor: DriverConstructor
) {
    AppThemePreview {
        DriverSeasonScreen(
            list = listOf(
                fakeStatWinning,
                fakeStat,
                driverConstructor.racedFor(),
                DriverSeasonModel.ResultHeader,
                driverConstructor.result()
            ),
            driverName = "firstName lastName",
            season = 2020,
            actionUpClicked = { },
            resultClicked = { }
        )
    }
}
private val fakeStatWinning = DriverSeasonModel.Stat(
    isWinning = true,
    icon = R.drawable.ic_status_front_wing,
    label = R.string.driver_overview_stat_career_points_finishes,
    value = "12"
)
private val fakeStat = DriverSeasonModel.Stat(
    isWinning = false,
    icon = R.drawable.ic_status_battery,
    label = R.string.driver_overview_stat_career_points,
    value = "4"
)
private fun DriverConstructor.racedFor() = DriverSeasonModel.RacedFor(
    season = 2022,
    type = PipeType.START,
    constructors = constructor,
    isChampionship = false
)
private fun DriverConstructor.result() = DriverSeasonModel.Result(
    season = 2020,
    round = 1,
    raceName = "raceName",
    circuitName = "circuitName",
    circuitId = "circuitId",
    raceCountry = "country",
    raceCountryISO = "countryISO",
    date = LocalDate.now(),
    showConstructorLabel = true,
    constructor = this.constructor,
    qualified = 2,
    finished = 1,
    raceStatus = "Retired",
    points = 10.0,
    maxPoints = 25,
    animationSpeed = AnimationSpeed.MEDIUM,
)