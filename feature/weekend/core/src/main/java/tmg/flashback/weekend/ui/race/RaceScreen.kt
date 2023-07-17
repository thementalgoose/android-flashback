package tmg.flashback.weekend.ui.race

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import tmg.flashback.formula1.enums.RaceStatus
import tmg.flashback.formula1.enums.isStatusFinished
import tmg.flashback.formula1.extensions.pointsDisplay
import tmg.flashback.formula1.model.Constructor
import tmg.flashback.formula1.model.FastestLap
import tmg.flashback.formula1.model.LapTime
import tmg.flashback.formula1.model.RaceResult
import tmg.flashback.providers.RaceRaceResultProvider
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.badge.Badge
import tmg.flashback.style.badge.BadgeView
import tmg.flashback.style.buttons.ButtonSecondarySegments
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextBody2
import tmg.flashback.style.text.TextTitle
import tmg.flashback.ui.components.constructorIndicator
import tmg.flashback.ui.components.drivers.DriverIcon
import tmg.flashback.ui.components.drivers.DriverName
import tmg.flashback.ui.components.drivers.driverIconSize
import tmg.flashback.ui.components.errors.NotAvailable
import tmg.flashback.ui.components.errors.NotAvailableYet
import tmg.flashback.ui.components.loading.SkeletonViewList
import tmg.flashback.ui.components.navigation.appBarHeight
import tmg.flashback.ui.components.progressbar.ProgressBar
import tmg.flashback.weekend.R
import tmg.flashback.weekend.ui.shared.DriverPoints
import tmg.flashback.weekend.ui.shared.finishingPositionWidth
import tmg.utilities.extensions.ordinalAbbreviation
import kotlin.math.roundToInt

private val timeWidth = 88.dp
private val pointsWidth = 56.dp

internal fun LazyListScope.race(
    list: List<RaceModel>,
    raceResultType: RaceResultType,
    showRaceType: (RaceResultType) -> Unit,
    driverClicked: (RaceResult) -> Unit,
    constructorClicked: (Constructor) -> Unit
) {
    item {
        ButtonSecondarySegments(
            modifier = Modifier
                .padding(horizontal = AppTheme.dimens.medium)
                .fillMaxWidth(),
            items = RaceResultType.values().map { it.label },
            selected = raceResultType.label,
            onClick = { label ->
                showRaceType(RaceResultType.values().first { it.label == label })
            },
            showTick = true
        )
    }
    items(list, key = { it.id }) {
        when (it) {
            is RaceModel.DriverPodium -> {
                Column(Modifier.fillMaxWidth()) {
                    Result(
                        model = it.p1,
                        driverClicked = driverClicked
                    )
                    Result(
                        model = it.p2,
                        driverClicked = driverClicked
                    )
                    Result(
                        model = it.p3,
                        driverClicked = driverClicked
                    )
                }
//                Podium(
//                    model = it,
//                    driverClicked = driverClicked
//                )
            }
            is RaceModel.DriverResult -> {
                Result(
                    model = it.result,
                    driverClicked = driverClicked
                )
            }
            is RaceModel.ConstructorResult -> {
                ConstructorResult(
                    model = it,
                    itemClicked = constructorClicked
                )
            }
            RaceModel.Loading -> {
                SkeletonViewList()
            }
            RaceModel.NotAvailable -> {
                NotAvailable()
            }
            RaceModel.NotAvailableYet -> {
                NotAvailableYet()
            }
        }
    }
    item(key = "footer") {
        Spacer(Modifier.height(appBarHeight))
    }
}

@Composable
private fun Result(
    model: RaceResult,
    driverClicked: (RaceResult) -> Unit,
    modifier: Modifier = Modifier
) {
    val fastestLap = if (model.fastestLap?.rank == 1) {
        ". ${stringResource(tmg.flashback.formula1.R.string.ab_result_fastest_lap)}"
    } else {
        "."
    }

    val contentDescription = stringResource(
        R.string.ab_result_race_overview,
        model.finish.ordinalAbbreviation,
        model.entry.driver.name,
        model.entry.constructor.name
    ) + fastestLap
    Row(modifier = Modifier
        .constructorIndicator(model.entry.constructor.colour)
    ) {
        Row(modifier = Modifier
            .weight(1f)
            .semantics(mergeDescendants = true) { }
            .clickable(
                enabled = true,
                onClick = { driverClicked(model) }
            )
            .clearAndSetSemantics { this.contentDescription = contentDescription }
            .padding(vertical = AppTheme.dimens.xsmall)
        ) {
            Box(Modifier.size(finishingPositionWidth, driverIconSize)) {
                TextTitle(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(
                            horizontal = AppTheme.dimens.xsmall,
                            vertical = AppTheme.dimens.medium
                        ),
                    bold = true,
                    textAlign = TextAlign.Center,
                    text = model.finish.toString()
                )
            }
            DriverIcon(
                photoUrl = model.entry.driver.photoUrl,
                number = model.entry.driver.number,
                code = model.entry.driver.code,
                constructorColor = model.entry.constructor.colour,
                driverClicked = null
            )
            Column(
                modifier = Modifier
                    .padding(
                        top = AppTheme.dimens.small,
                        start = AppTheme.dimens.small,
                        end = AppTheme.dimens.small,
                    )
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                DriverName(
                    firstName = model.entry.driver.firstName,
                    lastName = model.entry.driver.lastName
                )
                TextBody2(text = model.entry.constructor.name)
                if (model.fastestLap?.rank == 1) {
                    BadgeView(model = Badge(stringResource(id = R.string.ab_fastest_lap)))
                }
            }
        }
        Points(
            points = model.points
        )
        Time(
            lapTime = model.time,
            status = model.status,
            position = model.finish
        )
    }
}

@Composable
private fun Time(
    lapTime: LapTime?,
    status: RaceStatus,
    position: Int,
    modifier: Modifier = Modifier
) {
    val contentDescription = when {
        lapTime?.noTime == false && position == 1 -> stringResource(id = R.string.ab_result_finish_p1, lapTime.time)
        lapTime?.noTime == false-> stringResource(id = R.string.ab_result_finish_time, "+${lapTime.contentDescription}")
        status.isStatusFinished() -> status.label
        else -> stringResource(id = R.string.ab_result_finish_dnf, status.label)
    }
    Box(modifier = modifier
        .size(timeWidth, driverIconSize)
        .semantics(mergeDescendants = true) { }
        .clearAndSetSemantics {
            this.contentDescription = contentDescription
        }
    ) {
        TextBody2(
            modifier = Modifier.align(Alignment.Center),
            textAlign = TextAlign.Center,
            maxLines = 2,
            text = when {
                lapTime?.noTime == false && position == 1 -> lapTime.time
                lapTime?.noTime == false -> "+${lapTime.time}"
                status.isStatusFinished() -> status.label
                else -> "${stringResource(id = R.string.race_status_retired)}\n${status.label}"
            },
        )
    }
}

@Composable
private fun Points(
    points: Double,
    modifier: Modifier = Modifier
) {
    val pointsLabel = points.takeIf { it != 0.0 }?.pointsDisplay() ?: ""
    val contentDescription = pluralStringResource(id = R.plurals.race_points, count = points.roundToInt(), pointsLabel)
    Box(modifier = modifier
        .semantics(mergeDescendants = true) { }
        .clearAndSetSemantics { this.contentDescription = contentDescription }
        .size(pointsWidth, driverIconSize)
    ) {
        TextBody1(
            modifier = Modifier.align(Alignment.Center),
            bold = true,
            textAlign = TextAlign.Center,
            text = pointsLabel,
        )
    }
}

@Composable
internal fun FastestLap(
    modifier: Modifier = Modifier
) {
    Icon(
        painter = painterResource(id = R.drawable.ic_fastest_lap),
        contentDescription = stringResource(id = R.string.ab_fastest_lap),
        modifier = modifier
            .padding(top = 4.dp)
            .size(14.dp),
        tint = AppTheme.colors.f1FastestSector
    )
}

@Composable
private fun ConstructorResult(
    model: RaceModel.ConstructorResult,
    itemClicked: (Constructor) -> Unit,
    modifier: Modifier = Modifier,
) {
    val contentDescription = "${model.position?.ordinalAbbreviation}. ${stringResource(id = R.string.ab_scored, model.constructor.name, model.points.pointsDisplay())}."
    val drivers = model.drivers
        .map {
            stringResource(id = R.string.ab_scored, it.first.name, it.second.pointsDisplay())
        }
        .joinToString(separator = ",")

    Row(
        modifier = modifier
            .constructorIndicator(colour = model.constructor.colour)
            .semantics(mergeDescendants = true) { }
            .clearAndSetSemantics {
                this.contentDescription = contentDescription + drivers
            }
            .clickable(onClick = {
                itemClicked(model.constructor)
            }),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(Modifier.size(finishingPositionWidth, driverIconSize)) {
            TextTitle(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(
                        horizontal = AppTheme.dimens.xsmall,
                        vertical = AppTheme.dimens.medium
                    ),
                bold = true,
                textAlign = TextAlign.Center,
                text = model.position.toString()
            )
        }
        Row(modifier = Modifier
            .padding(
                top = AppTheme.dimens.small,
                end = AppTheme.dimens.medium,
                bottom = AppTheme.dimens.small
            )
        ) {
            Column(modifier = Modifier.weight(1f)) {
                TextTitle(
                    text = model.constructor.name,
                    bold = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(2.dp))
                model.drivers.forEach { (driver, points) ->
                    DriverPoints(
                        driver = driver,
                        points = points
                    )
                }
            }
            Spacer(Modifier.width(AppTheme.dimens.small))
            val progress = (model.points / model.maxTeamPoints).toFloat().coerceIn(0f, 1f)
            ProgressBar(
                modifier = Modifier
                    .width(110.dp)
                    .height(48.dp),
                endProgress = progress,
                barColor = model.constructor.colour,
                label = {
                    when (it) {
                        0f -> "0"
                        progress -> model.points.pointsDisplay()
                        else -> (it * model.maxTeamPoints).takeIf { !it.isNaN() }?.roundToInt()?.toString() ?: model.points.pointsDisplay()
                    }
                }
            )
        }
    }
}

@PreviewTheme
@Composable
private fun Preview(
    @PreviewParameter(RaceRaceResultProvider::class) result: RaceResult
) {
    AppThemePreview {
        LazyColumn(content = {
            race(
                raceResultType = RaceResultType.DRIVERS,
                list = listOf(
                    RaceModel.DriverPodium(
                        p1 = result.copy(fastestLap = FastestLap(1, LapTime(0,1,2,3))),
                        p2 = result,
                        p3 = result,
                    ),
                    RaceModel.DriverResult(
                        result = result.copy(fastestLap = FastestLap(1, LapTime(0,1,2,3)))
                    )
                ),
                showRaceType = { },
                driverClicked = { },
                constructorClicked = { }
            )
        })
    }
}

@PreviewTheme
@Composable
private fun PreviewConstructors(
    @PreviewParameter(RaceRaceResultProvider::class) result: RaceResult
) {
    AppThemePreview {
        LazyColumn(content = {
            race(
                raceResultType = RaceResultType.CONSTRUCTORS,
                list = listOf(
                    RaceModel.DriverPodium(
                        p1 = result.copy(fastestLap = FastestLap(1, LapTime(0,1,2,3))),
                        p2 = result,
                        p3 = result,
                    ),
                    RaceModel.DriverResult(
                        result = result.copy(fastestLap = FastestLap(1, LapTime(0,1,2,3)))
                    )
                ),
                showRaceType = { },
                driverClicked = { },
                constructorClicked = { }
            )
        })
    }
}