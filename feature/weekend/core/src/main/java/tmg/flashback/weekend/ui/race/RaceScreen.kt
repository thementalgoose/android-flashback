package tmg.flashback.weekend.ui.race

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import tmg.flashback.formula1.enums.RaceStatus
import tmg.flashback.formula1.enums.isStatusFinished
import tmg.flashback.formula1.extensions.pointsDisplay
import tmg.flashback.formula1.model.FastestLap
import tmg.flashback.formula1.model.LapTime
import tmg.flashback.formula1.model.RaceRaceResult
import tmg.flashback.providers.RaceRaceResultProvider
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.badge.Badge
import tmg.flashback.style.badge.BadgeView
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextBody2
import tmg.flashback.style.text.TextTitle
import tmg.flashback.ui.components.drivers.DriverIcon
import tmg.flashback.ui.components.drivers.DriverName
import tmg.flashback.ui.components.drivers.driverIconSize
import tmg.flashback.ui.components.errors.NotAvailable
import tmg.flashback.ui.components.errors.NotAvailableYet
import tmg.flashback.ui.components.loading.SkeletonViewList
import tmg.flashback.weekend.R
import tmg.flashback.weekend.contract.model.WeekendInfo
import tmg.flashback.weekend.ui.fakeWeekendInfo
import tmg.flashback.weekend.ui.info.RaceInfoHeader
import tmg.flashback.weekend.ui.shared.ConstructorIndicator
import tmg.flashback.weekend.ui.shared.RelativePosition
import tmg.flashback.weekend.ui.shared.finishingPositionWidth

private val timeWidth = 88.dp
private val pointsWidth = 48.dp

@Composable
fun RaceScreenVM(
    info: WeekendInfo,
    actionUpClicked: () -> Unit,
    viewModel: RaceViewModel = hiltViewModel()
) {
    viewModel.inputs.load(
        season = info.season,
        round = info.round
    )

    val results = viewModel.outputs.list.observeAsState(listOf(RaceModel.Loading))

    RaceScreen(
        info = info,
        list = results.value,
        driverClicked = viewModel.inputs::clickDriver,
        actionUpClicked = actionUpClicked
    )
}

@Composable
fun RaceScreen(
    info: WeekendInfo,
    list: List<RaceModel>,
    driverClicked: (RaceRaceResult) -> Unit,
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

            this.race(
                list = list,
                driverClicked = driverClicked
            )
        }
    )
}

internal fun LazyListScope.race(
    list: List<RaceModel>,
    driverClicked: (RaceRaceResult) -> Unit,
) {
    items(list, key = { it.id }) {
        when (it) {
            is RaceModel.Podium -> {
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
            is RaceModel.Result -> {
                Result(
                    model = it.result,
                    driverClicked = driverClicked
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
        Spacer(Modifier.height(72.dp))
    }
}

@Composable
private fun Result(
    model: RaceRaceResult,
    driverClicked: (RaceRaceResult) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier
        .height(IntrinsicSize.Min)
    ) {
        ConstructorIndicator(constructor = model.driver.constructor)
        Row(modifier = Modifier
            .weight(1f)
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
                driverClicked = { driverClicked(model) }
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
            status = model.status
        )
//        RelativePosition(
//            delta = model.finish - (model.grid ?: 0),
//            modifier = Modifier.width(48.dp)
//        )
    }
}

@Composable
private fun Time(
    lapTime: LapTime?,
    status: RaceStatus,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.size(timeWidth, driverIconSize)) {
        TextBody2(
            modifier = Modifier.align(Alignment.Center),
            textAlign = TextAlign.Center,
            maxLines = 1,
            text = when {
                lapTime?.noTime == false -> "+${lapTime.time}"
                status.isStatusFinished() -> status.label
//                else -> status.label
                else -> stringResource(id = R.string.race_status_retired)
            },
        )
    }
}

@Composable
private fun Points(
    points: Double,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.size(pointsWidth, driverIconSize)) {
        TextBody1(
            modifier = Modifier.align(Alignment.Center),
            bold = true,
            textAlign = TextAlign.Center,
            text = points.takeIf { it != 0.0 }?.pointsDisplay() ?: "",
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

@PreviewTheme
@Composable
private fun Preview(
    @PreviewParameter(RaceRaceResultProvider::class) result: RaceRaceResult
) {
    AppThemePreview {
        RaceScreen(
            info = fakeWeekendInfo,
            list = listOf(
                RaceModel.Podium(
                    p1 = result.copy(fastestLap = FastestLap(1, LapTime(0,1,2,3))),
                    p2 = result,
                    p3 = result,
                ),
                RaceModel.Result(
                    result = result.copy(fastestLap = FastestLap(1, LapTime(0,1,2,3)))
                )
            ),
            driverClicked = { },
            actionUpClicked = { }
        )
    }
}