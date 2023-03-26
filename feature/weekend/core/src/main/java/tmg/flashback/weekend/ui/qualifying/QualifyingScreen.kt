package tmg.flashback.weekend.ui.qualifying

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import tmg.flashback.weekend.R
import tmg.flashback.formula1.model.*
import tmg.flashback.providers.DriverConstructorProvider
import tmg.flashback.weekend.ui.fakeWeekendInfo
import tmg.flashback.weekend.ui.info.RaceInfoHeader
import tmg.flashback.weekend.ui.shared.DriverInfo
import tmg.flashback.ui.components.errors.NotAvailable
import tmg.flashback.ui.components.errors.NotAvailableYet
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.text.TextBody2
import tmg.flashback.style.text.TextSection
import tmg.flashback.ui.components.loading.SkeletonViewList
import tmg.flashback.weekend.contract.model.WeekendInfo

private val lapTimeWidth: Dp = 64.dp

@Composable
fun QualifyingScreenVM(
    info: WeekendInfo,
    actionUpClicked: () -> Unit,
    viewModel: QualifyingViewModel = hiltViewModel()
) {
    viewModel.inputs.load(
        season = info.season,
        round = info.round
    )

    val qualifying = viewModel.outputs.list.observeAsState(listOf(QualifyingModel.Loading))
    val qualifyingHeader = viewModel.outputs.headersToShow.observeAsState(
        QualifyingHeader(
            first = false,
            second = false,
            third = false
        )
    )
    QualifyingScreen(
        info = info,
        actionUpClicked = actionUpClicked,
        driverClicked = viewModel.inputs::clickDriver,
        list = qualifying.value,
        header = qualifyingHeader.value,
    )
}

@Composable
fun QualifyingScreen(
    info: WeekendInfo,
    actionUpClicked: () -> Unit,
    driverClicked: (Driver) -> Unit,
    list: List<QualifyingModel>,
    header: QualifyingHeader
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
            this.qualifying(
                driverClicked = driverClicked,
                list = list,
                header = header,
            )
        }
    )
}

internal fun LazyListScope.qualifying(
    driverClicked: (Driver) -> Unit,
    list: List<QualifyingModel>,
    header: QualifyingHeader
) {
    if (list.any { it.isResult }) {
        item("qheader") {
            Spacer(Modifier.height(AppTheme.dimens.medium))
            Header(
                showQ1 = header.first,
                showQ2 = header.second,
                showQ3 = header.third,
            )
        }
    }
    items(list, key = { it.id }) {
        when (it) {
            is QualifyingModel.Q1 -> Qualifying(
                model = it,
                driverClicked = driverClicked
            )
            is QualifyingModel.Q1Q2 -> Qualifying(
                model = it,
                driverClicked = driverClicked
            )
            is QualifyingModel.Q1Q2Q3 -> Qualifying(
                model = it,
                driverClicked = driverClicked
            )
            QualifyingModel.Loading -> {
                SkeletonViewList()
            }
            QualifyingModel.NotAvailable -> {
                NotAvailable()
            }
            QualifyingModel.NotAvailableYet -> {
                NotAvailableYet()
            }
        }
    }
    item(key = "footer") {
        Spacer(Modifier.height(72.dp))
    }
}

@Composable
private fun Header(
    modifier: Modifier = Modifier,
    showQ1: Boolean = true,
    showQ2: Boolean = true,
    showQ3: Boolean = true,
) {
    Row(modifier = modifier
        .padding(vertical = AppTheme.dimens.small)
    ) {
        Box(Modifier.weight(1f))

        if (showQ1) {
            TextSection(
                modifier = Modifier.width(lapTimeWidth),
                text = stringResource(id = R.string.qualifying_header_q1),
                textAlign = TextAlign.Center
            )
        }
        if (showQ2) {
            TextSection(
                modifier = Modifier.width(lapTimeWidth),
                text = stringResource(id = R.string.qualifying_header_q2),
                textAlign = TextAlign.Center
            )
        }
        if (showQ3) {
            TextSection(
                modifier = Modifier.width(lapTimeWidth),
                text = stringResource(id = R.string.qualifying_header_q3),
                textAlign = TextAlign.Center
            )
        }
        Spacer(Modifier.width(AppTheme.dimens.medium))
    }
}

@Composable
private fun Qualifying(
    model: QualifyingModel.Q1Q2Q3,
    driverClicked: (Driver) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier
        .height(IntrinsicSize.Min)
    ) {
        DriverInfo(
            modifier = Modifier
                .weight(1f)
                .clickable(onClick = { driverClicked(model.driver.driver) }),
            driver = model.driver,
            position = model.qualified
        )
        Time(
            modifier = Modifier.fillMaxHeight(),
            laptime = model.q1?.lapTime
        )
        Time(
            modifier = Modifier.fillMaxHeight(),
            laptime = model.q2?.lapTime
        )
        Time(
            modifier = Modifier.fillMaxHeight(),
            laptime = model.q3?.lapTime
        )
        Spacer(Modifier.width(AppTheme.dimens.medium))
    }
}

@Composable
private fun Qualifying(
    model: QualifyingModel.Q1Q2,
    driverClicked: (Driver) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier
        .height(IntrinsicSize.Min)
    ) {
        DriverInfo(
            modifier = Modifier
                .weight(1f)
                .clickable(onClick = { driverClicked(model.driver.driver) }),
            driver = model.driver,
            position = model.qualified
        )
        Time(
            modifier = Modifier.fillMaxHeight(),
            laptime = model.q1?.lapTime
        )
        Time(
            modifier = Modifier.fillMaxHeight(),
            laptime = model.q2?.lapTime
        )
        Spacer(Modifier.width(AppTheme.dimens.medium))
    }
}

@Composable
private fun Qualifying(
    model: QualifyingModel.Q1,
    driverClicked: (Driver) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier
        .height(IntrinsicSize.Min)
    ) {
        DriverInfo(
            modifier = Modifier
                .weight(1f)
                .clickable(onClick = { driverClicked(model.driver.driver) }),
            driver = model.driver,
            position = model.qualified
        )
        Time(
            modifier = Modifier.fillMaxHeight(),
            laptime = model.q1?.lapTime
        )
        Spacer(Modifier.width(AppTheme.dimens.medium))
    }
}

@Composable
private fun Time(
    laptime: LapTime?,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier
        .width(lapTimeWidth)
    ) {
        TextBody2(
            modifier = Modifier.align(Alignment.Center),
            text = laptime?.time ?: ""
        )
    }
}

@PreviewTheme
@Composable
private fun Preview(
    @PreviewParameter(DriverConstructorProvider::class) driverConstructor: DriverConstructor
) {
    AppThemePreview {
        QualifyingScreen(
            info = fakeWeekendInfo,
            actionUpClicked = { },
            driverClicked = { },
            list = listOf(
                fakeQualifyingModel(driverConstructor)
            ),
            header = QualifyingHeader(true, true, true)
        )
    }
}

private fun fakeQualifyingModel(driverConstructor: DriverConstructor) = QualifyingModel.Q1Q2Q3(
    driver = driverConstructor,
    finalQualifyingPosition = 1,
    q1 = RaceQualifyingResult(driverConstructor, lapTime = LapTime(92382), position = 1),
    q2 = RaceQualifyingResult(driverConstructor, lapTime = LapTime(92293), position = 1),
    q3 = RaceQualifyingResult(driverConstructor, lapTime = LapTime(91934), position = 1)
)