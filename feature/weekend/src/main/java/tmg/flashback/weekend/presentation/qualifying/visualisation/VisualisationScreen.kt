package tmg.flashback.weekend.presentation.qualifying.visualisation

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.hilt.navigation.compose.hiltViewModel
import tmg.flashback.formula1.model.DriverEntry
import tmg.flashback.formula1.model.QualifyingType
import tmg.flashback.formula1.model.QualifyingType.Q1
import tmg.flashback.formula1.model.QualifyingType.Q2
import tmg.flashback.formula1.model.QualifyingType.Q3
import tmg.flashback.formula1.model.headerLabel
import tmg.flashback.googleanalytics.presentation.ScreenView
import tmg.flashback.providers.DriverConstructorProvider
import tmg.flashback.strings.R.string
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.buttons.ButtonSecondarySegments
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextHeadline2

@Composable
fun VisualisationScreenVM(
    season: Int,
    round: Int,
    viewModel: VisualisationViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState()

    LaunchedEffect(season, round) {
        viewModel.load(season, round)
    }

    when (val state = uiState.value) {
        VisualisationUiState.Loading -> {

        }
        VisualisationUiState.QualifyingDataNotAvailable -> {
            VisualisationUnavailableScreen()
        }
        is VisualisationUiState.Visualisation -> {
            VisualisationScreen(
                availableQualifyingTypes = state.availableQualifyingTypes,
                selectedQualifyingType = state.qualifyingType,
                clickQualifyingType = viewModel::selectType,
                resultEntries = state.resultEntries,
                indicatorEntries = state.indicatorEntries
            )
        }
    }
}

@Composable
private fun VisualisationUnavailableScreen() {
    Column(Modifier.fillMaxWidth()) {
        TextBody1(
            text = stringResource(string.qualifying_visualisation_not_found)
        )
    }
}

@Composable
private fun VisualisationScreen(
    availableQualifyingTypes: List<QualifyingType>,
    selectedQualifyingType: QualifyingType,
    clickQualifyingType: (QualifyingType) -> Unit,
    resultEntries: List<ResultEntry>,
    indicatorEntries: List<IndicatorEntry>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = AppTheme.dimens.medium,
                    vertical = AppTheme.dimens.small
                ),
            verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.small)
        ) {
            TextHeadline2(
                modifier = Modifier
                    .fillMaxWidth(),
                text = stringResource(string.qualifying_visualisation_title)
            )
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
            TextBody1(
                text = stringResource(string.qualifying_visualisation_subtitle)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .horizontalScroll(rememberScrollState()),
        ) {
            Graph(
                indicators = indicatorEntries,
                results = resultEntries
            )
        }
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
    @PreviewParameter(DriverConstructorProvider ::class) driverEntry: DriverEntry
) {
    AppThemePreview {
        VisualisationScreen(
            availableQualifyingTypes = listOf(Q3, Q2, Q1),
            selectedQualifyingType = Q3,
            clickQualifyingType = { },
            resultEntries = fakeResults(driverEntry.driver, driverEntry.constructor),
            indicatorEntries = fakeIndicators(),
        )
    }
}