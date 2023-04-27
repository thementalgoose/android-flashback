package tmg.flashback.weekend.ui.sprint

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import tmg.flashback.formula1.enums.RaceStatus
import tmg.flashback.formula1.enums.isStatusFinished
import tmg.flashback.formula1.extensions.pointsDisplay
import tmg.flashback.formula1.model.DriverConstructor
import tmg.flashback.formula1.model.LapTime
import tmg.flashback.formula1.model.SprintRaceResult
import tmg.flashback.providers.DriverConstructorProvider
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.weekend.ui.info.RaceInfoHeader
import tmg.flashback.ui.components.errors.NotAvailable
import tmg.flashback.ui.components.errors.NotAvailableYet
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextBody2
import tmg.flashback.style.text.TextTitle
import tmg.flashback.ui.components.drivers.DriverIcon
import tmg.flashback.ui.components.drivers.DriverName
import tmg.flashback.ui.components.drivers.driverIconSize
import tmg.flashback.ui.components.loading.SkeletonViewList
import tmg.flashback.weekend.R
import tmg.flashback.weekend.contract.model.WeekendInfo
import tmg.flashback.weekend.ui.fakeWeekendInfo
import tmg.flashback.weekend.ui.shared.ConstructorIndicator
import tmg.flashback.weekend.ui.shared.finishingPositionWidth
import tmg.utilities.extensions.ordinalAbbreviation

private val timeWidth = 88.dp
private val pointsWidth = 56.dp

@Composable
fun SprintScreenVM(
    info: WeekendInfo,
    actionUpClicked: () -> Unit,
    viewModel: SprintViewModel = hiltViewModel()
) {
    viewModel.inputs.load(
        season = info.season,
        round = info.round
    )

    val list = viewModel.outputs.list.observeAsState(listOf(SprintModel.Loading))
    SprintScreen(
        info = info,
        actionUpClicked = actionUpClicked,
        driverClicked = viewModel.inputs::clickDriver,
        list = list.value
    )
}

@Composable
fun SprintScreen(
    info: WeekendInfo,
    list: List<SprintModel>,
    driverClicked: (SprintRaceResult) -> Unit,
    actionUpClicked: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        content = {
            item("header") {
                RaceInfoHeader(
                    model = info,
                    actionUpClicked = actionUpClicked
                )
            }

            this.sprint(
                list = list,
                driverClicked = driverClicked
            )
        }
    )
}

internal fun LazyListScope.sprint(
    list: List<SprintModel>,
    driverClicked: (SprintRaceResult) -> Unit,
) {
    items(list, key = { it.id }) {
        when (it) {
            is SprintModel.Result -> {
                Result(
                    model = it.result,
                    driverClicked = driverClicked
                )
            }
            SprintModel.Loading -> {
                SkeletonViewList()
            }
            SprintModel.NotAvailable -> {
                NotAvailable()
            }
            SprintModel.NotAvailableYet -> {
                NotAvailableYet()
            }
        }
    }
    item(key = "footer") {
        Spacer(Modifier.height(72.dp))
    }
}

@Composable
private fun Result(
    model: SprintRaceResult,
    driverClicked: (SprintRaceResult) -> Unit,
    modifier: Modifier = Modifier
) {
    val contentDescription = stringResource(
        id = R.string.ab_result_overview,
        model.finish.ordinalAbbreviation,
        model.driver.driver.name,
        model.driver.constructor.name
    )
    Row(
        modifier = modifier.height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.Center
    ) {
        ConstructorIndicator(constructor = model.driver.constructor)
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
                photoUrl = model.driver.driver.photoUrl,
                number = model.driver.driver.number,
                code = model.driver.driver.code,
                constructorColor = model.driver.constructor.colour,
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
                    firstName = model.driver.driver.firstName,
                    lastName = model.driver.driver.lastName
                )
                TextBody2(text = model.driver.constructor.name)
//                if (model.fastestLap?.rank == 1) {
//                    BadgeView(model = Badge(stringResource(id = R.string.ab_fastest_lap)))
//                }
            }
        }
        Points(
            modifier = Modifier.align(Alignment.CenterVertically),
            points = model.points
        )
        Time(
            modifier = Modifier.align(Alignment.CenterVertically),
            position = model.finish,
            lapTime = model.time,
            status = model.status
        )
    }
}

@Composable
private fun Time(
    position: Int,
    lapTime: LapTime?,
    status: RaceStatus,
    modifier: Modifier = Modifier
) {
    val contentDescription = when {
        lapTime?.noTime == false && position == 1 -> stringResource(id = R.string.ab_result_finish_p1, lapTime.time)
        lapTime?.noTime == false-> stringResource(id = R.string.ab_result_finish_time, "+${lapTime.time}")
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
    TextBody1(
        modifier = modifier.width(pointsWidth),
        bold = true,
        textAlign = TextAlign.Center,
        text = points.takeIf { it != 0.0 }?.pointsDisplay() ?: "",
    )
}


@PreviewTheme
@Composable
private fun Preview(
    @PreviewParameter(DriverConstructorProvider::class) driverConstructor: DriverConstructor
) {
    AppThemePreview {
        SprintScreen(
            info = fakeWeekendInfo,
            actionUpClicked = { },
            driverClicked = { },
            list = listOf(
                fakeSprintModel(driverConstructor)
            )
        )
    }
}

private fun fakeSprintModel(driverConstructor: DriverConstructor) = SprintModel.Result(
    result = SprintRaceResult(
        driver = driverConstructor,
        time = null,
        points = 2.0,
        grid = 3,
        finish = 4,
        status = RaceStatus.FINISHED
    )
)