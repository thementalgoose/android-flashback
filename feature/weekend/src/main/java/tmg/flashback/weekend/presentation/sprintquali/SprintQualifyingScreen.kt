package tmg.flashback.weekend.presentation.sprintquali


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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import tmg.flashback.formula1.model.*
import tmg.flashback.providers.DriverConstructorProvider
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
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
import tmg.flashback.strings.R.string
import tmg.flashback.weekend.presentation.shared.Position
import tmg.utilities.extensions.ordinalAbbreviation

private val lapTimeWidth: Dp = 64.dp

internal fun LazyListScope.sprintQualifying(
    itemModifier: Modifier = Modifier,
    driverClicked: (Driver) -> Unit,
    list: List<SprintQualifyingModel>
) {
    if (list.any { it.isResult }) {
        item("qheader") {
            Header(
                modifier = itemModifier
            )
        }
    }
    items(list, key = { "sprintquali-${it.id}" }) {
        when (it) {
            is SprintQualifyingModel.Result-> SprintQualifying(
                modifier = itemModifier,
                model = it,
                driverClicked = driverClicked
            )
            SprintQualifyingModel.Loading -> {
                SkeletonViewList(
                    modifier = itemModifier
                )
            }
            SprintQualifyingModel.NotAvailable -> {
                NotAvailable(
                    modifier = itemModifier
                )
            }
            SprintQualifyingModel.NotAvailableYet -> {
                NotAvailableYet(
                    modifier = itemModifier
                )
            }
        }
    }
}

@Composable
private fun Header(
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier
        .padding(vertical = AppTheme.dimens.small)
    ) {
        Box(Modifier.weight(1f))
        TextSection(
            modifier = Modifier.width(lapTimeWidth),
            text = stringResource(id = string.sprint_qualifying_header_q1),
            textAlign = TextAlign.Center
        )
        TextSection(
            modifier = Modifier.width(lapTimeWidth),
            text = stringResource(id = string.sprint_qualifying_header_q2),
            textAlign = TextAlign.Center
        )
        TextSection(
            modifier = Modifier.width(lapTimeWidth),
            text = stringResource(id = string.sprint_qualifying_header_q3),
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.width(AppTheme.dimens.medium))
    }
}

@Composable
private fun SprintQualifying(
    model: SprintQualifyingModel.Result,
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
                    grid = model.grid
                )
            }
            Time(
                modifier = Modifier.fillMaxHeight(),
                laptime = model.sq1?.lapTime,
                column = SprintQualifyingColumn.Q1
            )
            Time(
                modifier = Modifier.fillMaxHeight(),
                laptime = model.sq2?.lapTime,
                column = SprintQualifyingColumn.Q2
            )
            Time(
                modifier = Modifier.fillMaxHeight(),
                laptime = model.sq3?.lapTime,
                column = SprintQualifyingColumn.Q3
            )
            Spacer(Modifier.width(AppTheme.dimens.medium))
        }
    }
}

@Composable
private fun DriverLabel(
    driver: DriverEntry,
    qualifyingPosition: Int?,
    grid: Int?,
    modifier: Modifier = Modifier
) {
    val contentDescription = if (qualifyingPosition == null) {
        stringResource(
            string.ab_result_qualifying_overview_dnq,
            driver.driver.name,
            driver.constructor.name
        )
    } else {
        stringResource(
            string.ab_result_qualifying_overview,
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

            if (qualifyingPosition != null && grid != null) {
                if (grid > qualifyingPosition) {
                    BadgeView(
                        modifier = Modifier.padding(bottom = AppTheme.dimens.xsmall),
                        model = Badge(stringResource(id = string.qualifying_penalty_starts_sprint, grid.ordinalAbbreviation))
                    )
                } else if (grid == 0) {
                    BadgeView(
                        modifier = Modifier.padding(bottom = AppTheme.dimens.xsmall),
                        model = Badge(stringResource(id = string.qualifying_penalty, grid.ordinalAbbreviation))
                    )
                }
            }
        }
    }
}

@Composable
private fun Time(
    laptime: LapTime?,
    column: SprintQualifyingColumn,
    modifier: Modifier = Modifier
) {
    val contentDescription = when (column) {
        SprintQualifyingColumn.Q1 -> stringResource(id = string.sprint_qualifying_header_q1)
        SprintQualifyingColumn.Q2 -> stringResource(id = string.sprint_qualifying_header_q2)
        SprintQualifyingColumn.Q3 -> stringResource(id = string.sprint_qualifying_header_q3)
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
        LazyColumn(content = {
            sprintQualifying(
                driverClicked = { },
                list = listOf(
                    fakeQualifyingModel(driverConstructor)
                )
            )
        })
    }
}

private fun fakeQualifyingModel(driverConstructor: DriverEntry) = SprintQualifyingModel.Result(
    driver = driverConstructor,
    finalQualifyingPosition = 1,
    sq1 = SprintQualifyingResult(driverConstructor, lapTime = LapTime(92382), position = 1),
    sq2 = SprintQualifyingResult(driverConstructor, lapTime = LapTime(92293), position = 1),
    sq3 = SprintQualifyingResult(driverConstructor, lapTime = LapTime(91934), position = 1),
    grid = 1
)

private enum class SprintQualifyingColumn { Q1, Q2, Q3 }