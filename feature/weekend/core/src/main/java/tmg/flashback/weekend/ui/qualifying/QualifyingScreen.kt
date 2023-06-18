package tmg.flashback.weekend.ui.qualifying

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import tmg.flashback.formula1.model.*
import tmg.flashback.providers.DriverConstructorProvider
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.badge.Badge
import tmg.flashback.style.badge.BadgeView
import tmg.flashback.style.text.TextBody2
import tmg.flashback.style.text.TextSection
import tmg.flashback.ui.components.drivers.DriverName
import tmg.flashback.ui.components.errors.NotAvailable
import tmg.flashback.ui.components.errors.NotAvailableYet
import tmg.flashback.ui.components.loading.SkeletonViewList
import tmg.flashback.ui.components.navigation.appBarHeight
import tmg.flashback.weekend.R
import tmg.flashback.weekend.contract.model.ScreenWeekendData
import tmg.flashback.weekend.ui.fakeWeekendInfo
import tmg.flashback.weekend.ui.info.RaceInfoHeader
import tmg.flashback.weekend.ui.shared.ConstructorIndicator
import tmg.flashback.weekend.ui.shared.Position
import tmg.utilities.extensions.ordinalAbbreviation

private val lapTimeWidth: Dp = 64.dp

@Composable
fun QualifyingScreenVM(
    info: ScreenWeekendData,
    actionUpClicked: () -> Unit,
    viewModel: QualifyingViewModel = hiltViewModel()
) {
    viewModel.inputs.load(
        season = info.season,
        round = info.round
    )

    val qualifying = viewModel.outputs.list.collectAsState(listOf(QualifyingModel.Loading))
    val qualifyingHeader = viewModel.outputs.headersToShow.collectAsState(
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
    info: ScreenWeekendData,
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
        Spacer(Modifier.height(appBarHeight))
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
    Column(Modifier.fillMaxWidth()) {
        Row(modifier = modifier
            .height(IntrinsicSize.Min)
        ) {
            Column(Modifier.weight(1f)) {
                DriverLabel(
                    modifier = Modifier
                        .clickable(onClick = { driverClicked(model.driver.driver) }),
                    driver = model.driver,
                    qualifyingPosition = model.qualified,
                    grid = model.grid,
                    sprintQualifyingGrid = model.sprintRaceGrid
                )
            }
            Time(
                modifier = Modifier.fillMaxHeight(),
                laptime = model.q1?.lapTime,
                column = QualifyingColumn.Q1
            )
            Time(
                modifier = Modifier.fillMaxHeight(),
                laptime = model.q2?.lapTime,
                column = QualifyingColumn.Q2
            )
            Time(
                modifier = Modifier.fillMaxHeight(),
                laptime = model.q3?.lapTime,
                column = QualifyingColumn.Q3
            )
            Spacer(Modifier.width(AppTheme.dimens.medium))
        }
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
        DriverLabel(
            modifier = Modifier
                .weight(1f)
                .clickable(onClick = { driverClicked(model.driver.driver) }),
            driver = model.driver,
            qualifyingPosition = model.qualified,
            grid = null,
            sprintQualifyingGrid = null
        )
        Time(
            modifier = Modifier.fillMaxHeight(),
            laptime = model.q1?.lapTime,
            column = QualifyingColumn.Q2
        )
        Time(
            modifier = Modifier.fillMaxHeight(),
            laptime = model.q2?.lapTime,
            column = QualifyingColumn.Q2
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
        DriverLabel(
            modifier = Modifier
                .weight(1f)
                .clickable(onClick = { driverClicked(model.driver.driver) }),
            driver = model.driver,
            qualifyingPosition = model.qualified,
            grid = null,
            sprintQualifyingGrid = null
        )
        Time(
            modifier = Modifier.fillMaxHeight(),
            laptime = model.q1?.lapTime,
            column = QualifyingColumn.Q1
        )
        Spacer(Modifier.width(AppTheme.dimens.medium))
    }
}

@Composable
private fun DriverLabel(
    driver: DriverEntry,
    qualifyingPosition: Int?,
    grid: Int?,
    sprintQualifyingGrid: Int?,
    modifier: Modifier = Modifier
) {
    val contentDescription = if (qualifyingPosition == null) {
        stringResource(
            tmg.flashback.formula1.R.string.ab_result_qualifying_overview_dnq,
            driver.driver.name,
            driver.constructor.name
        )
    } else {
        stringResource(
            tmg.flashback.formula1.R.string.ab_result_qualifying_overview,
            driver.driver.name,
            driver.driver.name,
            qualifyingPosition.ordinalAbbreviation
        )
    }
    Row(modifier = modifier
        .height(IntrinsicSize.Min)
        .semantics(mergeDescendants = true) { }
        .clearAndSetSemantics { this.contentDescription = contentDescription }
    ) {
        ConstructorIndicator(driver.constructor)
        Position(label = qualifyingPosition?.toString() ?: "-")
        Column(Modifier.fillMaxWidth()) {
            DriverName(
                firstName = driver.driver.firstName,
                lastName = driver.driver.lastName,
                modifier = Modifier.padding(top = AppTheme.dimens.xsmall)
            )
            TextBody2(
                text = driver.constructor.name,
                modifier = Modifier.padding(vertical = AppTheme.dimens.xsmall)
            )

            if (qualifyingPosition != null) {
                if (sprintQualifyingGrid != null) {
                    if (sprintQualifyingGrid > qualifyingPosition) {
                        BadgeView(
                            modifier = Modifier.padding(bottom = AppTheme.dimens.xsmall),
                            model = Badge(stringResource(id = R.string.qualifying_penalty_starts_sprint, sprintQualifyingGrid.ordinalAbbreviation))
                        )
                    } else if (sprintQualifyingGrid == 0) {
                        BadgeView(
                            modifier = Modifier.padding(bottom = AppTheme.dimens.xsmall),
                            model = Badge(stringResource(id = R.string.qualifying_penalty, sprintQualifyingGrid.ordinalAbbreviation))
                        )
                    }
                } else if (grid != null) {
                    if (grid > qualifyingPosition) {
                        BadgeView(
                            modifier = Modifier.padding(bottom = AppTheme.dimens.xsmall),
                            model = Badge(stringResource(id = R.string.qualifying_penalty_starts, grid.ordinalAbbreviation))
                        )
                    } else if (grid == 0) {
                        BadgeView(
                            modifier = Modifier.padding(bottom = AppTheme.dimens.xsmall),
                            model = Badge(stringResource(id = R.string.qualifying_penalty, grid.ordinalAbbreviation))
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Time(
    laptime: LapTime?,
    column: QualifyingColumn,
    modifier: Modifier = Modifier
) {
    val contentDescription = when (column) {
        QualifyingColumn.Q1 -> stringResource(id = R.string.qualifying_header_q1)
        QualifyingColumn.Q2 -> stringResource(id = R.string.qualifying_header_q2)
        QualifyingColumn.Q3 -> stringResource(id = R.string.qualifying_header_q3)
    }
    Box(modifier = modifier
        .width(lapTimeWidth)
        .semantics(mergeDescendants = true) {
            this.contentDescription = contentDescription
        }
    ) {
        TextBody2(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .semantics {
                    if (laptime == null) {
                        this.contentDescription = ""
                    }
                }
                .padding(vertical = AppTheme.dimens.nsmall),
            text = laptime?.time ?: ""
        )
    }
}

@PreviewTheme
@Composable
private fun Preview(
    @PreviewParameter(DriverConstructorProvider::class) driverConstructor: DriverEntry
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

private fun fakeQualifyingModel(driverConstructor: DriverEntry) = QualifyingModel.Q1Q2Q3(
    driver = driverConstructor,
    finalQualifyingPosition = 1,
    q1 = QualifyingResult(driverConstructor, lapTime = LapTime(92382), position = 1),
    q2 = QualifyingResult(driverConstructor, lapTime = LapTime(92293), position = 1),
    q3 = QualifyingResult(driverConstructor, lapTime = LapTime(91934), position = 1),
    grid = 1,
    sprintRaceGrid = null
)

private enum class QualifyingColumn { Q1, Q2, Q3 }