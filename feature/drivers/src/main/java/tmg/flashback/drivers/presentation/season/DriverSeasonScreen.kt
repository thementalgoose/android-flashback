package tmg.flashback.drivers.presentation.season

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import tmg.flashback.drivers.presentation.common.DriverBadges
import tmg.flashback.formula1.R.drawable
import tmg.flashback.formula1.enums.RaceStatus
import tmg.flashback.formula1.enums.isStatusFinished
import tmg.flashback.formula1.extensions.pointsDisplay
import tmg.flashback.formula1.model.Constructor
import tmg.flashback.formula1.model.DriverEntry
import tmg.flashback.googleanalytics.constants.AnalyticsConstants.analyticsDriverId
import tmg.flashback.googleanalytics.constants.AnalyticsConstants.analyticsSeason
import tmg.flashback.googleanalytics.presentation.ScreenView
import tmg.flashback.providers.DriverConstructorProvider
import tmg.flashback.strings.R.string
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.badge.Badge
import tmg.flashback.style.badge.BadgeView
import tmg.flashback.style.buttons.ButtonSecondary
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextBody2
import tmg.flashback.style.text.TextCaption
import tmg.flashback.style.text.TextTitle
import tmg.flashback.ui.components.constructorIndicator
import tmg.flashback.ui.components.drivers.DriverIcon
import tmg.flashback.ui.components.errors.NetworkError
import tmg.flashback.ui.components.flag.Flag
import tmg.flashback.ui.components.header.Header
import tmg.flashback.ui.components.header.HeaderAction
import tmg.flashback.ui.components.loading.SkeletonViewList
import tmg.flashback.ui.components.messages.Message
import tmg.flashback.ui.components.navigation.PipeType
import tmg.flashback.ui.components.swiperefresh.SwipeRefresh
import tmg.flashback.ui.components.timeline.Timeline
import tmg.flashback.ui.foldables.isWidthExpanded
import tmg.utilities.extensions.ordinalAbbreviation
import java.time.LocalDate

/**
 * If the width of the container is more than this value, put the stuff side by side
 */
private val resultColumnFlexBorder: Dp = 300.dp
private val resultColumnWidth: Dp = 50.dp
private val headerImageSize: Dp = 120.dp

@Composable
fun DriverSeasonScreenVM(
    paddingValues: PaddingValues,
    actionUpClicked: () -> Unit,
    windowSizeClass: WindowSizeClass,
    driverId: String,
    driverName: String,
    season: Int,
    showHeader: Boolean = true,
    viewModel: DriverSeasonViewModel = hiltViewModel()
) {
    ScreenView(
        screenName = "Driver Season", args = mapOf(
            analyticsDriverId to driverId,
            analyticsSeason to season.toString()
        )
    )

    viewModel.inputs.setup(driverId, season)

    val list = viewModel.outputs.list.collectAsState(emptyList())
    val isLoading = viewModel.outputs.isLoading.collectAsState(false)
    SwipeRefresh(
        isLoading = isLoading.value,
        onRefresh = viewModel.inputs::refresh
    ) {
        DriverSeasonScreen(
            actionUpClicked = actionUpClicked,
            windowSizeClass = windowSizeClass,
            paddingValues = paddingValues,
            list = list.value,
            driverName = driverName,
            season = season,
            showHeader = showHeader,
            resultClicked = viewModel.inputs::clickSeasonRound
        )
    }
}

@Composable
fun DriverSeasonScreen(
    actionUpClicked: () -> Unit,
    windowSizeClass: WindowSizeClass,
    paddingValues: PaddingValues,
    list: List<DriverSeasonModel>,
    driverName: String,
    season: Int,
    showHeader: Boolean,
    resultClicked: (DriverSeasonModel.Result) -> Unit,
) {
    LazyColumn(
        contentPadding = paddingValues,
        content = {
            item(key = "header") {
                Header(
                    text = "${season}\n${driverName}",
                    action = when (windowSizeClass.isWidthExpanded) {
                        false -> HeaderAction.BACK
                        true -> null
                    },
                    actionUpClicked = actionUpClicked
                )
            }
            items(list, key = { it.key }) {
                when (it) {
                    is DriverSeasonModel.Header -> {
                        if (showHeader) {
                            Header(
                                model = it,
                                linkClicked = { }
                            )
                        }
                    }

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
            .semantics(mergeDescendants = true) { }
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
    model: DriverSeasonModel.RacedFor,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .padding(horizontal = AppTheme.dimens.medium)
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
                                end = AppTheme.dimens.small,
                                top = 2.dp,
                                bottom = 2.dp
                            )
                    )
                    Box(
                        modifier = Modifier
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
    Row(modifier = modifier) {
        Box(Modifier.weight(1f))
        Box(
            Modifier
                .width(resultColumnWidth)
                .padding(vertical = AppTheme.dimens.xsmall)
        ) {
            Icon(
                modifier = Modifier.align(Alignment.Center),
                painter = painterResource(id = drawable.ic_status_results_qualifying),
                contentDescription = null,
                tint = AppTheme.colors.contentSecondary
            )
        }
        Box(
            Modifier
                .width(resultColumnWidth)
                .padding(vertical = AppTheme.dimens.xsmall)
        ) {
            Icon(
                modifier = Modifier.align(Alignment.Center),
                painter = painterResource(id = drawable.ic_status_finished),
                contentDescription = null,
                tint = AppTheme.colors.contentSecondary
            )
        }
        Box(
            Modifier
                .width(resultColumnWidth)
                .padding(vertical = AppTheme.dimens.xsmall)
        ) {
            Icon(
                modifier = Modifier.align(Alignment.Center),
                painter = painterResource(id = drawable.ic_race_points),
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
    Row(
        modifier = modifier
            .clickable(onClick = { clickResult(model) })
            .alpha(
                when (model.raceStatus.isStatusFinished()) {
                    true -> 1.0f
                    false -> 0.7f
                }
            )
            .background(
                when (model.raceStatus.isStatusFinished()) {
                    true -> AppTheme.colors.backgroundPrimary
                    false -> AppTheme.colors.backgroundSecondary
                }
            )
    ) {
        Column(
            Modifier.weight(1f)
        ) {
            if (model.isSprint) {
                SprintInfo(
                    raceName = model.raceName,
                    raceCountryISO = model.raceCountryISO,
                    constructorColor = model.constructor.colour,
                    circuitName = model.circuitName,
                    country = model.raceCountry,
                    constructor = model.constructor,
                    round = model.round,
                    showConstructorLabel = model.showConstructorLabel
                )
            } else {
                RaceInfo(
                    raceName = model.raceName,
                    raceCountryISO = model.raceCountryISO,
                    constructorColor = model.constructor.colour,
                    circuitName = model.circuitName,
                    constructor = model.constructor,
                    round = model.round,
                    showConstructorLabel = model.showConstructorLabel
                )
            }
        }
        Box(
            Modifier
                .width(resultColumnWidth)
                .padding(vertical = AppTheme.dimens.xsmall)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = AppTheme.dimens.xsmall)
            ) {
                TextBody2(
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    bold = false,
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
                .padding(vertical = AppTheme.dimens.xsmall)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = AppTheme.dimens.xsmall)
            ) {
                if (model.isSprint) {
                    TextBody2(
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        bold = true,
                        text = model.finished?.ordinalAbbreviation ?: "-"
                    )
                } else {
                    TextBody1(
                        textColor = when (model.finished == 1) {
                            true -> AppTheme.colors.f1Championship
                            false -> null
                        },
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        bold = true,
                        text = model.finished?.ordinalAbbreviation ?: "-"
                    )
                }
                if (!model.raceStatus.isStatusFinished()) {
                    TextCaption(
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        text = model.raceStatus.label
                    )
                }
            }
        }
        Box(
            Modifier
                .width(resultColumnWidth)
                .padding(vertical = AppTheme.dimens.small)
        ) {
            TextBody1(
                text = model.points.pointsDisplay(),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
private fun Header(
    model: DriverSeasonModel.Header,
    linkClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(
            horizontal = AppTheme.dimens.medium
        )
    ) {
        DriverIcon(
            photoUrl = model.driver.photoUrl,
            number = model.driver.number,
            code = model.driver.code,
            size = headerImageSize
        )
        DriverBadges(
            driver = model.driver
        )
        if (!model.driver.wikiUrl.isNullOrEmpty()) {
            ButtonSecondary(
                text = stringResource(id = string.details_link_wikipedia),
                onClick = { linkClicked(model.driver.wikiUrl!!) },
            )
            Spacer(Modifier.height(AppTheme.dimens.xsmall))
        }
    }
}

private val colorIndicator: Dp = 6.dp

@Composable
private fun RaceInfo(
    raceName: String,
    raceCountryISO: String,
    circuitName: String,
    constructorColor: Color,
    constructor: Constructor,
    showConstructorLabel: Boolean,
    round: Int?,
    modifier: Modifier = Modifier,
) {

    Row(
        modifier = modifier
            .height(IntrinsicSize.Min)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(colorIndicator)
                .background(constructorColor)
        )
        if (round != null) {
            TextTitle(
                modifier = Modifier
                    .width(36.dp)
                    .padding(horizontal = AppTheme.dimens.xsmall)
                    .align(Alignment.CenterVertically),
                bold = true,
                textAlign = TextAlign.Center,
                text = round.toString()
            )
        } else {
            Spacer(Modifier.width(AppTheme.dimens.medium - colorIndicator))
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(
                    top = 3.dp,
                    bottom = AppTheme.dimens.small
                )
        ) {
            Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                TextTitle(
                    text = raceName,
                    bold = true
                )
            }
            Spacer(Modifier.height(4.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Flag(
                    iso = raceCountryISO,
                    modifier = Modifier
                        .size(16.dp)
                        .align(Alignment.CenterVertically),
                )
                Spacer(Modifier.width(AppTheme.dimens.small))
                TextBody2(text = circuitName)
            }

            if (showConstructorLabel) {
                TextCaption(
                    modifier = Modifier
                        .padding(
                            top = AppTheme.dimens.xsmall,
                            bottom = AppTheme.dimens.xsmall
                        )
                        .fillMaxWidth(),
                    text = constructor.name
                )
            }
        }
    }
}

@Composable
private fun SprintInfo(
    raceName: String,
    raceCountryISO: String,
    circuitName: String,
    country: String,
    constructorColor: Color,
    constructor: Constructor,
    showConstructorLabel: Boolean,
    round: Int?,
    modifier: Modifier = Modifier,
) {

    Row(
        modifier = modifier
            .constructorIndicator(constructorColor)
    ) {
        if (round != null) {
            TextTitle(
                modifier = Modifier
                    .width(36.dp)
                    .padding(horizontal = AppTheme.dimens.xsmall)
                    .align(Alignment.CenterVertically),
                bold = true,
                textAlign = TextAlign.Center,
                text = ""
            )
        } else {
            Spacer(Modifier.width(AppTheme.dimens.medium - colorIndicator))
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(
                    top = 6.dp,
                    bottom = AppTheme.dimens.medium
                )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                BadgeView(model = Badge(label = stringResource(id = string.nav_sprint)))
            }
            if (showConstructorLabel) {
                Spacer(Modifier.height(AppTheme.dimens.xsmall))
                TextCaption(
                    modifier = Modifier
                        .padding(
                            start = AppTheme.dimens.small,
                            top = AppTheme.dimens.xsmall,
                            bottom = AppTheme.dimens.xsmall
                        )
                        .fillMaxWidth(),
                    text = constructor.name
                )
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
        DriverSeasonScreen(
            list = listOf(
                fakeStatWinning,
                fakeStat,
                driverConstructor.racedFor(),
                DriverSeasonModel.ResultHeader,
                driverConstructor.result(round = 1),
                driverConstructor.result(round = 1, isSprint = true, showConstructorLabel = false),
                driverConstructor.result(round = 2)
            ),
            driverName = "firstName lastName",
            season = 2020,
            actionUpClicked = { },
            windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(400.dp, 400.dp)),
            paddingValues = PaddingValues.Absolute(),
            resultClicked = { },
            showHeader = false
        )
    }
}

private val fakeStatWinning = DriverSeasonModel.Stat(
    isWinning = true,
    icon = drawable.ic_status_front_wing,
    label = string.driver_overview_stat_career_points_finishes,
    value = "12"
)
private val fakeStat = DriverSeasonModel.Stat(
    isWinning = false,
    icon = drawable.ic_status_battery,
    label = string.driver_overview_stat_career_points,
    value = "4"
)

private fun DriverEntry.racedFor() = DriverSeasonModel.RacedFor(
    season = 2022,
    type = PipeType.START,
    constructors = constructor,
    isChampionship = false
)

private fun DriverEntry.result(
    isSprint: Boolean = false,
    round: Int = 1,
    showConstructorLabel: Boolean = true
) = DriverSeasonModel.Result(
    season = 2020,
    round = round,
    raceName = "raceName",
    circuitName = "circuitName",
    circuitId = "circuitId",
    raceCountry = "country",
    raceCountryISO = "countryISO",
    date = LocalDate.now(),
    showConstructorLabel = showConstructorLabel,
    constructor = this.constructor,
    qualified = 2,
    finished = 1,
    raceStatus = RaceStatus.from("Finished"),
    points = 10.0,
    maxPoints = 25,
    isSprint = isSprint
)