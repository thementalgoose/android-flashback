package tmg.flashback.stats.ui.weekend.sprint

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
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
import tmg.flashback.ui.components.loading.SkeletonViewList

private val timeWidth = 80.dp
private val pointsWidth = 80.dp

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
    driverClicked: (RaceSprintResult) -> Unit,
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
    driverClicked: (RaceSprintResult) -> Unit,
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
    model: RaceSprintResult,
    driverClicked: (RaceSprintResult) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.height(IntrinsicSize.Min),
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