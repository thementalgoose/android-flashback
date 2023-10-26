package tmg.flashback.weekend.presentation.qualifying

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import tmg.flashback.formula1.model.*
import tmg.flashback.style.AppTheme
import tmg.flashback.style.badge.Badge
import tmg.flashback.style.badge.BadgeView
import tmg.flashback.style.text.TextBody2
import tmg.flashback.style.text.TextSection
import tmg.flashback.ui.components.constructorIndicator
import tmg.flashback.ui.components.drivers.DriverName
import tmg.flashback.ui.components.errors.NotAvailable
import tmg.flashback.ui.components.errors.NotAvailableYet
import tmg.flashback.ui.components.loading.SkeletonViewList
import tmg.flashback.weekend.R
import tmg.flashback.weekend.presentation.shared.Position
import tmg.utilities.extensions.ordinalAbbreviation

private val lapTimeWidth: Dp = 64.dp

internal fun LazyListScope.qualifying(
    driverClicked: (Driver) -> Unit,
    list: List<QualifyingModel>,
    header: QualifyingHeader,
    itemModifier: Modifier = Modifier,
) {
    if (list.any { it.isResult }) {
        item("qheader") {
            Header(
                modifier = itemModifier,
                showQ1 = header.first,
                showQ2 = header.second,
                showQ3 = header.third,
            )
        }
    }
    items(list, key = { "qualifying-${it.id}" }) {
        Render(
            modifier = itemModifier,
            model = it,
            driverClicked = driverClicked
        )
    }
}

@Composable
private fun Render(
    modifier: Modifier = Modifier,
    model: QualifyingModel,
    driverClicked: (Driver) -> Unit
) {
    when (model) {
        is QualifyingModel.Q1 -> Qualifying(
            modifier = modifier,
            model = model,
            driverClicked = driverClicked
        )
        is QualifyingModel.Q1Q2 -> Qualifying(
            modifier = modifier,
            model = model,
            driverClicked = driverClicked
        )
        is QualifyingModel.Q1Q2Q3 -> Qualifying(
            modifier = modifier,
            model = model,
            driverClicked = driverClicked
        )
        QualifyingModel.Loading -> {
            SkeletonViewList(
                modifier = modifier
            )
        }
        QualifyingModel.NotAvailable -> {
            NotAvailable(
                modifier = modifier
            )
        }
        QualifyingModel.NotAvailableYet -> {
            NotAvailableYet(
                modifier = modifier
            )
        }
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
        .constructorIndicator(driver.constructor.colour)
        .semantics(mergeDescendants = true) { }
        .clearAndSetSemantics { this.contentDescription = contentDescription }
    ) {
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

private enum class QualifyingColumn { Q1, Q2, Q3 }