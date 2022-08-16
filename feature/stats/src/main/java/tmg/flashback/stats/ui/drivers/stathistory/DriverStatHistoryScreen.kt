package tmg.flashback.stats.ui.drivers.stathistory

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.lifecycle.viewmodel.compose.viewModel
import tmg.flashback.formula1.model.Race
import tmg.flashback.providers.RaceProvider
import tmg.flashback.stats.R
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextSection
import tmg.flashback.style.text.TextTitle
import tmg.flashback.ui.components.header.Header

@Composable
fun DriverStatHistoryScreenVM(
    driverId: String,
    driverName: String,
    driverStatHistoryType: DriverStatHistoryType,
    actionUpClicked: () -> Unit,
) {
    val viewModel = viewModel<DriverStatHistoryViewModel>()
    viewModel.inputs.load(driverId, driverStatHistoryType)

    val list = viewModel.outputs.results.observeAsState(emptyList())
    DriverStatHistoryScreen(
        driverName = driverName,
        list = list.value,
        driverStatHistoryType = driverStatHistoryType,
        actionUpClicked = actionUpClicked
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun DriverStatHistoryScreen(
    driverName: String,
    list: List<DriverStatHistoryModel>,
    driverStatHistoryType: DriverStatHistoryType,
    actionUpClicked: () -> Unit,
) {
    LazyColumn(
        modifier = Modifier.nestedScroll(rememberNestedScrollInteropConnection()),
        content = {
            item(key = "header") {
                Header(
                    text = "$driverName\n${stringResource(id = driverStatHistoryType.label)}",
                    icon = painterResource(id = R.drawable.ic_web_close),
                    iconContentDescription = null,
                    actionUpClicked = actionUpClicked
                )
            }
            items(list, key = { it.key }) {
                when (it) {
                    DriverStatHistoryModel.Empty -> Empty()
                    is DriverStatHistoryModel.Label -> Label(
                        model = it
                    )
                    is DriverStatHistoryModel.Race -> Race(
                        model = it
                    )
                    is DriverStatHistoryModel.Year -> Year(
                        model = it
                    )
                }
            }
        }
    )
}

@Composable
private fun Empty() {
    TextBody1(text = "Empty")
}

@Composable
private fun Label(
    model: DriverStatHistoryModel.Label
) {
    TextSection(
        modifier = Modifier.padding(horizontal = AppTheme.dimensions.paddingMedium),
        text = model.text
    )
    Divider()
}

@Composable
private fun Race(
    model: DriverStatHistoryModel.Race
) {
    TextBody1(text = "Race")
}

@Composable
private fun Year(
    model: DriverStatHistoryModel.Year
) {
    TextSection(
        modifier = Modifier.padding(horizontal = AppTheme.dimensions.paddingMedium),
        text = model.season.toString()
    )
}

@PreviewTheme
@Composable
private fun Preview(
    @PreviewParameter(RaceProvider::class) race: Race
) {
    AppThemePreview {
        DriverStatHistoryScreen(
            list = listOf(
                DriverStatHistoryModel.Empty,
                DriverStatHistoryModel.Year(
                    season = 2021
                ),
                DriverStatHistoryModel.Label(
                    text = "label"
                ),
                DriverStatHistoryModel.Race(
                    raceInfo = race.raceInfo,
                    constructor = race.constructors.first()
                )
            ),
            driverName = "firstName lastName",
            driverStatHistoryType = DriverStatHistoryType.CHAMPIONSHIPS,
            actionUpClicked = { }
        )
    }
}