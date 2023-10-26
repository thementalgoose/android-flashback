@file:OptIn(ExperimentalFoundationApi::class)

package tmg.flashback.weekend.presentation.race

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import tmg.flashback.formula1.constants.Formula1
import tmg.flashback.formula1.extensions.pointsDisplay
import tmg.flashback.formula1.model.Constructor
import tmg.flashback.formula1.model.DriverEntry
import tmg.flashback.formula1.model.FastestLap
import tmg.flashback.formula1.model.LapTime
import tmg.flashback.formula1.model.RaceResult
import tmg.flashback.providers.RaceRaceResultProvider
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.buttons.ButtonSecondarySegments
import tmg.flashback.style.text.TextTitle
import tmg.flashback.ui.components.constructorIndicator
import tmg.flashback.ui.components.drivers.driverIconSize
import tmg.flashback.ui.components.errors.NotAvailable
import tmg.flashback.ui.components.errors.NotAvailableYet
import tmg.flashback.ui.components.loading.SkeletonViewList
import tmg.flashback.weekend.R
import tmg.flashback.weekend.presentation.shared.DriverInfoWithIcon
import tmg.flashback.weekend.presentation.shared.DriverPoints
import tmg.flashback.weekend.presentation.shared.PointsBox
import tmg.flashback.weekend.presentation.shared.RaceHeader
import tmg.flashback.weekend.presentation.shared.Time
import tmg.flashback.weekend.presentation.shared.finishingPositionWidth
import tmg.flashback.weekend.presentation.shared.status
import tmg.flashback.weekend.presentation.shared.timeWidth
import tmg.utilities.extensions.ordinalAbbreviation

internal fun LazyListScope.race(
    list: List<RaceModel>,
    season: Int,
    raceResultType: RaceResultType,
    showRaceType: (RaceResultType) -> Unit,
    driverClicked: (DriverEntry) -> Unit,
    constructorClicked: (Constructor) -> Unit,
    itemModifier: Modifier = Modifier,
) {
    if (list.isNotEmpty()) {
        item {
            ButtonSecondarySegments(
                modifier = itemModifier
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
        item {
            RaceHeader(
                modifier = itemModifier,
                showPoints = true,
                showStatus = raceResultType == RaceResultType.DRIVERS
            )
        }
    }
    items(list, key = { "race-${it.id}" }) {
        when (it) {
            is RaceModel.DriverPodium -> {
                Column(itemModifier.fillMaxWidth()) {
                    Result(
                        model = it.p1,
                        season = season,
                        driverClicked = driverClicked
                    )
                    Result(
                        model = it.p2,
                        season = season,
                        driverClicked = driverClicked
                    )
                    Result(
                        model = it.p3,
                        season = season,
                        driverClicked = driverClicked
                    )
                }
            }
            is RaceModel.DriverResult -> {
                Result(
                    modifier = itemModifier,
                    model = it.result,
                    season = season,
                    driverClicked = driverClicked
                )
            }
            is RaceModel.ConstructorResult -> {
                ConstructorResult(
                    modifier = itemModifier,
                    model = it,
                    itemClicked = constructorClicked
                )
            }
            RaceModel.Loading -> {
                SkeletonViewList(
                    modifier = itemModifier
                )
            }
            RaceModel.NotAvailable -> {
                NotAvailable(
                    modifier = itemModifier
                )
            }
            RaceModel.NotAvailableYet -> {
                NotAvailableYet(
                    modifier = itemModifier
                )
            }
        }
    }
}

@Composable
private fun Result(
    model: RaceResult,
    season: Int,
    driverClicked: (DriverEntry) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.status(model.status, AppTheme.colors.backgroundSecondary)) {
        DriverInfoWithIcon(
            modifier = Modifier
                .weight(1f),
            entry = model.entry,
            position = model.finish,
            driverClicked = driverClicked,
            fastestLap = model.fastestLap?.rank == 1
        )

        Box(
            modifier = Modifier
                .width(timeWidth)
                .padding(top = AppTheme.dimens.medium + 2.dp)
        ) {
            Time(
                modifier = Modifier
                    .align(Alignment.Center),
                lapTime = model.time,
                status = model.status
            )
        }

        PointsBox(
            points = model.points,
            maxPoints = Formula1.maxDriverPointsBySeason(season).toDouble(),
            colour = model.entry.constructor.colour,
        )
    }
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
        verticalAlignment = Alignment.Top,
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
            PointsBox(
                points = model.points,
                maxPoints = model.maxTeamPoints,
                colour = model.constructor.colour
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
                season = 2023,
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
                season = 2023,
                list = listOf(
                    RaceModel.ConstructorResult(
                        constructor = result.entry.constructor,
                        position = 1,
                        points = 40.0,
                        drivers = listOf(
                            result.entry.driver to 30.0
                        ),
                        maxTeamPoints = 45.0,
                        highestDriverPosition = 3
                    ),
                    RaceModel.ConstructorResult(
                        constructor = result.entry.constructor.copy(id = "3"),
                        position = 3,
                        points = 20.0,
                        drivers = listOf(
                            result.entry.driver to 30.0
                        ),
                        maxTeamPoints = 45.0,
                        highestDriverPosition = 3
                    )
                ),
                showRaceType = { },
                driverClicked = { },
                constructorClicked = { }
            )
        })
    }
}