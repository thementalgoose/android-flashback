package tmg.flashback.weekend.presentation.sprint

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import tmg.flashback.formula1.enums.RaceStatus
import tmg.flashback.formula1.extensions.pointsDisplay
import tmg.flashback.formula1.model.Constructor
import tmg.flashback.formula1.model.DriverEntry
import tmg.flashback.formula1.model.SprintRaceResult
import tmg.flashback.providers.DriverConstructorProvider
import tmg.flashback.strings.R.string
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.buttons.ButtonSecondarySegments
import tmg.flashback.style.text.TextTitle
import tmg.flashback.ui.components.constructorIndicator
import tmg.flashback.ui.components.drivers.DriverPoints
import tmg.flashback.ui.components.drivers.driverIconSize
import tmg.flashback.ui.components.errors.NotAvailable
import tmg.flashback.ui.components.errors.NotAvailableYet
import tmg.flashback.ui.components.loading.SkeletonViewList
import tmg.flashback.weekend.presentation.shared.DriverInfoWithIcon
import tmg.flashback.weekend.presentation.shared.PointsBox
import tmg.flashback.weekend.presentation.shared.RaceHeader
import tmg.flashback.weekend.presentation.shared.Time
import tmg.flashback.weekend.presentation.shared.finishingPositionWidth
import tmg.flashback.weekend.presentation.shared.status
import tmg.flashback.weekend.presentation.shared.timeWidth

internal fun LazyListScope.sprint(
    season: Int,
    list: List<SprintModel>,
    sprintResultType: SprintResultType,
    showSprintType: (SprintResultType) -> Unit,
    driverClicked: (DriverEntry) -> Unit,
    constructorClicked: (Constructor) -> Unit,
    itemModifier: Modifier = Modifier
) {
    if (list.isNotEmpty()) {
        item {
            ButtonSecondarySegments(
                modifier = itemModifier
                    .padding(horizontal = AppTheme.dimens.medium)
                    .fillMaxWidth(),
                items = SprintResultType.values().map { it.label },
                selected = sprintResultType.label,
                onClick = { label ->
                    showSprintType(SprintResultType.values().first { it.label == label })
                },
                showTick = true
            )
        }
        item {
            RaceHeader(
                modifier = itemModifier,
                showPoints = true,
                showStatus = sprintResultType == SprintResultType.DRIVERS
            )
        }
    }
    items(list, key = { "sprint-${it.id}" }) {
        when (it) {
            is SprintModel.DriverResult -> {
                DriverResult(
                    modifier = itemModifier,
                    model = it.result,
                    season = season,
                    driverClicked = driverClicked
                )
            }
            is SprintModel.ConstructorResult -> {
                ConstructorResult(
                    modifier = itemModifier,
                    model = it,
                    constructorClicked = constructorClicked
                )
            }
            SprintModel.Loading -> {
                SkeletonViewList(
                    modifier = itemModifier
                )
            }
            SprintModel.NotAvailable -> {
                NotAvailable(
                    modifier = itemModifier
                )
            }
            SprintModel.NotAvailableYet -> {
                NotAvailableYet(
                    modifier = itemModifier
                )
            }
        }
    }
}

@Composable
private fun DriverResult(
    model: SprintRaceResult,
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
            driverClicked = driverClicked
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
    model: SprintModel.ConstructorResult,
    constructorClicked: (Constructor) -> Unit,
    modifier: Modifier = Modifier,
) {
    val contentDescription = stringResource(string.ab_scored, model.constructor.name, model.points.pointsDisplay())
    val drivers = model.drivers
        .map {
            stringResource(id = string.ab_scored, it.first.name, it.second.pointsDisplay())
        }
        .joinToString(separator = ",")

    Row(
        modifier = modifier
            .constructorIndicator(model.constructor.colour)
            .semantics(mergeDescendants = true) { }
            .clearAndSetSemantics {
                this.contentDescription = contentDescription + drivers
            }
            .clickable(onClick = {
                constructorClicked(model.constructor)
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
                        name = driver.name,
                        nationality = driver.nationality,
                        nationalityISO = driver.nationalityISO,
                        points = points,
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
    @PreviewParameter(DriverConstructorProvider::class) driverConstructor: DriverEntry
) {
    AppThemePreview {
        LazyColumn(content = {
            sprint(
                season = 2020,
                list = listOf(
                    fakeSprintModel(driverConstructor)
                ),
                sprintResultType = SprintResultType.DRIVERS,
                showSprintType = { },
                driverClicked = { },
                constructorClicked = { }
            )
        })
    }
}

private fun fakeSprintModel(driverConstructor: DriverEntry) = SprintModel.DriverResult(
    result = SprintRaceResult(
        entry = driverConstructor,
        time = null,
        points = 2.0,
        grid = 3,
        finish = 4,
        status = RaceStatus.FINISHED
    )
)