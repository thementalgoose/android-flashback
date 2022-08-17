package tmg.flashback.stats.ui.drivers.stathistory

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import tmg.flashback.formula1.model.Race
import tmg.flashback.formula1.utils.getFlagResourceAlpha3
import tmg.flashback.providers.RaceProvider
import tmg.flashback.stats.R
import tmg.flashback.stats.analytics.AnalyticsConstants.analyticsDriverId
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.text.*
import tmg.flashback.ui.components.analytics.ScreenView
import tmg.flashback.ui.components.header.Header
import tmg.flashback.ui.utils.isInPreview
import tmg.utilities.extensions.format

@Composable
fun DriverStatHistoryScreenVM(
    driverId: String,
    driverName: String,
    driverStatHistoryType: DriverStatHistoryType,
    actionUpClicked: () -> Unit,
) {
    val viewModel = viewModel<DriverStatHistoryViewModel>()
    viewModel.inputs.load(driverId, driverStatHistoryType)

    ScreenView(screenName = "Driver Stat History", mapOf(
        analyticsDriverId to driverId,
        "stat" to driverStatHistoryType.analyticsKey
    ))

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
            item(key = "footer") {
                Spacer(Modifier.height(AppTheme.dimensions.paddingXLarge))
            }
        }
    )
}

@Composable
private fun Empty() {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(
            start = AppTheme.dimensions.paddingMedium,
            end = AppTheme.dimensions.paddingMedium,
            bottom = AppTheme.dimensions.paddingXLarge
        )
    ) {
        TextBody1(text = stringResource(id = R.string.stat_history_empty))
    }
}

@Composable
private fun Label(
    model: DriverStatHistoryModel.Label
) {
    TextSection(
        modifier = Modifier.padding(horizontal = AppTheme.dimensions.paddingMedium),
        text = model.text
    )
    Divider(
        modifier = Modifier.padding(horizontal = AppTheme.dimensions.paddingMedium),
        color = AppTheme.colors.backgroundTertiary
    )
}

@Composable
private fun Race(
    model: DriverStatHistoryModel.Race
) {
    Row(modifier = Modifier
        .padding(
            vertical = AppTheme.dimensions.paddingSmall,
            horizontal = AppTheme.dimensions.paddingMedium
        )
    ) {
        val resourceId = when (isInPreview()) {
            true -> R.drawable.gb
            false -> LocalContext.current.getFlagResourceAlpha3(model.raceInfo.circuit.countryISO)
        }
        Image(
            modifier = Modifier.size(32.dp),
            painter = painterResource(id = resourceId),
            contentDescription = null,
            contentScale = ContentScale.Fit,
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = AppTheme.dimensions.paddingSmall)
        ) {
            TextBody1(
                text = model.raceInfo.name,
                bold = true
            )
            TextBody2(
                modifier = Modifier.padding(vertical = 2.dp),
                text = model.raceInfo.circuit.name
            )
        }
        Column(
            horizontalAlignment = Alignment.End
        ) {
            TextBody2(
                bold = true,
                text = "#${model.raceInfo.round}",
                textAlign = TextAlign.End,
            )
            if (model.constructor != null) {
                Spacer(Modifier.height(2.dp))
                Row(Modifier.height(IntrinsicSize.Min)) {
                    TextBody2(
                        modifier = Modifier.padding(horizontal = AppTheme.dimensions.paddingXSmall),
                        text = model.constructor.name
                    )
                    Box(
                        Modifier
                            .fillMaxHeight()
                            .width(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(model.constructor.colour)
                    )
                }
            }
        }
    }
}

@Composable
private fun Year(
    model: DriverStatHistoryModel.Year
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(
            horizontal = AppTheme.dimensions.paddingMedium,
            vertical = AppTheme.dimensions.paddingSmall
        )
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_trophy),
            contentDescription = stringResource(id = R.string.world_champion),
            tint = AppTheme.colors.f1Championship,
            modifier = Modifier.size(32.dp)
        )
        TextHeadline2(
            modifier = Modifier
                .padding(horizontal = AppTheme.dimensions.paddingMedium)
                .weight(1f),
            text = model.season.toString()
        )
    }
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