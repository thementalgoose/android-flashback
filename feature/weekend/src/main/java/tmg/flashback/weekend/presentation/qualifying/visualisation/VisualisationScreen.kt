package tmg.flashback.weekend.presentation.qualifying.visualisation

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import tmg.flashback.formula1.model.QualifyingResult
import tmg.flashback.formula1.model.QualifyingType
import tmg.flashback.formula1.model.QualifyingType.Q1
import tmg.flashback.formula1.model.QualifyingType.Q2
import tmg.flashback.formula1.model.QualifyingType.Q3
import tmg.flashback.formula1.model.Race
import tmg.flashback.formula1.model.headerLabel
import tmg.flashback.providers.RaceProvider
import tmg.flashback.strings.R.string
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.buttons.ButtonSecondarySegments
import tmg.flashback.style.text.TextBody2
import tmg.flashback.style.text.TextHeadline2

private val widthOfASecond = 140.dp
private val sizeOfVisualisedCar = DpSize(75.dp, 25.dp)

@Composable
fun VisualisationScreenVM(
    viewModel: VisualisationViewModel = viewModel()
) {
    val uiState = viewModel.uiState.collectAsState()
}

@Composable
private fun VisualisationUnavailableScreen(

) {
    Column(Modifier.fillMaxWidth()) {

    }
}

@Composable
private fun VisualisationScreen(
    availableQualifyingTypes: List<QualifyingType>,
    selectedQualifyingType: QualifyingType,
    clickQualifyingType: (QualifyingType) -> Unit,
    qualifyingResults: List<QualifyingResult>,
    timestampSecondsIndicators: List<Int>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        ButtonSecondarySegments(
            items = availableQualifyingTypes.map { it.headerLabel },
            selected = selectedQualifyingType.headerLabel,
            onClick = {
                when (it) {
                    string.qualifying_header_q3 -> clickQualifyingType(Q3)
                    string.qualifying_header_q2 -> clickQualifyingType(Q2)
                    string.qualifying_header_q1 -> clickQualifyingType(Q1)
                }
            },
            showTick = true
        )

        TextHeadline2(
            modifier = Modifier
                .fillMaxWidth(),
            text = stringResource(selectedQualifyingType.headerLabel)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                timestampSecondsIndicators.forEachIndexed { index, seconds ->
                    Indicator(
                        modifier = Modifier.offset(widthOfASecond * index),
                        second = seconds
                    )
                }

                qualifyingResults.forEach { item ->
                    VisualisationCar(
                        modifier = Modifier.offset(23.dp, 50.dp),
                        entry = item.entry,
                    )
                }
            }
        }
    }
}

@Composable
private fun Indicator(
    second: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        TextBody2("${second}s")
        Box(modifier = Modifier
            .background(AppTheme.colors.contentTertiary)
            .width(1.dp)
            .fillMaxHeight()
            .height(IntrinsicSize.Max)
        )
    }
}

@PreviewTheme
@Composable
private fun PreviewUnavailable() {
    AppThemePreview {
        VisualisationUnavailableScreen()
    }
}

@PreviewTheme
@Composable
private fun Preview(
    @PreviewParameter(RaceProvider::class) race: Race
) {
    AppThemePreview {
        VisualisationScreen(
            availableQualifyingTypes = listOf(Q3, Q2, Q1),
            selectedQualifyingType = Q3,
            clickQualifyingType = { },
            qualifyingResults = race.qualifying.first { it.label == Q3 }.results,
            timestampSecondsIndicators = listOf(70, 71, 72, 73)
        )
    }
}