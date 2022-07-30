package tmg.flashback.stats.ui.weekend.sprint

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import tmg.flashback.formula1.enums.RaceStatus
import tmg.flashback.formula1.enums.isStatusFinished
import tmg.flashback.formula1.extensions.pointsDisplay
import tmg.flashback.formula1.model.LapTime
import tmg.flashback.formula1.model.RaceSprintResult
import tmg.flashback.stats.R
import tmg.flashback.stats.ui.weekend.WeekendInfo
import tmg.flashback.stats.ui.weekend.info.RaceInfoHeader
import tmg.flashback.stats.ui.weekend.shared.Delta
import tmg.flashback.stats.ui.weekend.shared.DriverInfo
import tmg.flashback.stats.ui.weekend.shared.NotAvailable
import tmg.flashback.stats.ui.weekend.shared.NotAvailableYet
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextBody2
import tmg.flashback.ui.components.loading.SkeletonView
import tmg.flashback.ui.components.loading.SkeletonViewList

private val timeWidth = 80.dp
private val pointsWidth = 80.dp

@Composable
fun SprintScreenVM(
    info: WeekendInfo,
    actionUpClicked: () -> Unit
) {
    val viewModel = hiltViewModel<SprintViewModel>()
    viewModel.inputs.load(
        season = info.season,
        round = info.round
    )

    val list = viewModel.outputs.list.observeAsState(listOf(SprintModel.Loading))
    SprintScreen(
        info = info,
        actionUpClicked = actionUpClicked,
        list = list.value
    )
}

@Composable
fun SprintScreen(
    info: WeekendInfo,
    list: List<SprintModel>,
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
            items(list, key = { it.id }) {
                when (it) {
                    is SprintModel.Result -> {
                        Result(model = it.result)
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
    )
}

@Composable
private fun Result(
    model: RaceSprintResult,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.Center
    ) {
        DriverInfo(
            modifier = Modifier.weight(1f),
            driver = model.driver,
            position = model.finish,
            extraContent = {
                Delta(
                    grid = model.grid,
                    finish = model.finish
                )
            }
        )
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
    TextBody2(
        modifier = modifier
            .width(timeWidth),
        textAlign = TextAlign.Center,
        text = when {
            lapTime?.noTime == false && position == 1 -> lapTime.time
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