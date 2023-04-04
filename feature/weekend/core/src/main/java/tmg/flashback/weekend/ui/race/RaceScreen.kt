package tmg.flashback.weekend.ui.race

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
import tmg.flashback.weekend.ui.fakeWeekendInfo
import tmg.flashback.weekend.ui.info.RaceInfoHeader
import tmg.flashback.weekend.ui.shared.Delta
import tmg.flashback.weekend.R
import tmg.flashback.weekend.ui.shared.DriverInfo
import tmg.flashback.ui.components.errors.NotAvailable
import tmg.flashback.ui.components.errors.NotAvailableYet
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextBody2
import tmg.flashback.ui.components.loading.SkeletonViewList
import tmg.flashback.weekend.contract.model.WeekendInfo

private val timeWidth = 80.dp
private val pointsWidth = 80.dp

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
                Podium(
                    model = it,
                    driverClicked = driverClicked
                )
            }
            is RaceModel.Result -> {
                ResultOld(
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

//@Composable
//private fun Result(
//    model: RaceRaceResult,
//    driverClicked: (RaceRaceResult) -> Unit,
//    modifier: Modifier = Modifier
//) {
//    Row {
//
//    }
//}

@Composable
private fun ResultOld(
    model: RaceRaceResult,
    driverClicked: (RaceRaceResult) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .height(IntrinsicSize.Min)
            .alpha(
                when (model.status.isStatusFinished()) {
                    true -> 1.0f
                    false -> 0.7f
                }
            )
            .background(
                when (model.status.isStatusFinished()) {
                    true -> AppTheme.colors.backgroundPrimary
                    false -> AppTheme.colors.backgroundSecondary
                }
            ),
        horizontalArrangement = Arrangement.Center
    ) {
        DriverInfo(
            modifier = Modifier
                .weight(1f)
                .clickable(onClick = { driverClicked(model) }),
            driver = model.driver,
            position = model.finish,
            extraContent = {
                Delta(
                    grid = model.grid,
                    finish = model.finish
                )
                if (model.fastestLap?.rank == 1) {
                    Spacer(Modifier.width(4.dp))
                    FastestLap(Modifier.align(Alignment.CenterVertically))
                }
            }
        )
        Points(
            modifier = Modifier.align(Alignment.CenterVertically),
            points = model.points
        )
        Time(
            modifier = Modifier.align(Alignment.CenterVertically),
            lapTime = model.time,
            status = model.status
        )
    }
}

@Composable
private fun Time(
    lapTime: LapTime?,
    status: RaceStatus,
    modifier: Modifier = Modifier
) {
    TextBody2(
        modifier = modifier
            .width(timeWidth),
        textAlign = TextAlign.Center,
        text = when {
            lapTime?.noTime == false -> "+${lapTime.time}"
            status.isStatusFinished() -> status
            else -> stringResource(id = R.string.race_status_retired)
        },
    )
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