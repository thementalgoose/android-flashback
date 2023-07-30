package tmg.flashback.drivers.ui.stathistory

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import tmg.flashback.googleanalytics.constants.AnalyticsConstants.analyticsDriverId
import tmg.flashback.drivers.R
import tmg.flashback.drivers.contract.model.DriverStatHistoryType
import tmg.flashback.formula1.model.Constructor
import tmg.flashback.formula1.model.Race
import tmg.flashback.formula1.model.RaceInfo
import tmg.flashback.providers.RaceProvider
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.text.*
import tmg.flashback.googleanalytics.presentation.ScreenView
import tmg.flashback.ui.components.flag.Flag
import tmg.flashback.ui.components.header.Header
import tmg.flashback.ui.components.header.HeaderAction

@Composable
fun DriverStatHistoryScreenVM(
    driverId: String,
    driverName: String,
    driverStatHistoryType: DriverStatHistoryType,
    actionUpClicked: () -> Unit,
    viewModel: DriverStatHistoryViewModel = hiltViewModel()
) {
    viewModel.inputs.load(driverId, driverStatHistoryType)

    ScreenView(screenName = "Driver Stat History", mapOf(
        analyticsDriverId to driverId,
        "stat" to driverStatHistoryType.analyticsKey
    ))

    val list = viewModel.outputs.results.collectAsState(emptyList())
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
                    action = HeaderAction.CLOSE,
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
                    is DriverStatHistoryModel.RacePosition -> Race(
                        model = it
                    )
                }
            }
            item(key = "footer") {
                Spacer(Modifier.height(AppTheme.dimens.xlarge))
            }
        }
    )
}

@Composable
private fun Empty() {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(
            start = AppTheme.dimens.medium,
            end = AppTheme.dimens.medium,
            bottom = AppTheme.dimens.xlarge
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
        modifier = Modifier.padding(horizontal = AppTheme.dimens.medium),
        text = model.text
    )
    Divider(
        modifier = Modifier.padding(horizontal = AppTheme.dimens.medium),
        color = AppTheme.colors.backgroundTertiary
    )
}

@Composable
private fun Race(
    model: DriverStatHistoryModel.Race,
) {
    Race(
        raceInfo = model.raceInfo,
        constructor = model.constructor
    )
}

@Composable
private fun Race(
    model: DriverStatHistoryModel.RacePosition
) {
    Race(
        raceInfo = model.raceInfo,
        constructor = model.constructor,
        position = model.position
    )
}

@Composable
private fun Race(
    raceInfo: RaceInfo,
    constructor: Constructor?,
    position: Int? = null
) {
    Row(modifier = Modifier
        .padding(
            vertical = AppTheme.dimens.small,
            horizontal = AppTheme.dimens.medium
        )
    ) {
        if (position != null) {
            Column(modifier = Modifier
                .padding(vertical = 3.dp)
                .width(IntrinsicSize.Min)
            ) {
                TextTitle(
                    bold = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = AppTheme.dimens.xsmall),
                    textAlign = TextAlign.Center,
                    text = "P$position"
                )
//                Icon(
//                    painter = painterResource(
//                        id = when (position) {
//                            1 -> R.drawable.ic_p1_podium
//                            2 -> R.drawable.ic_p2_podium
//                            3 -> R.drawable.ic_p3_podium
//                            else -> position.positionIcon
//                        }
//                    ),
//                    modifier = Modifier
//                        .size(16.dp)
//                        .align(Alignment.CenterHorizontally),
//                    contentDescription = null,
//                    tint = when (position) {
//                        1 -> AppTheme.colors.contentPrimary
//                        else -> AppTheme.colors.contentTertiary
//                    }
//                )
            }
            Spacer(Modifier.width(AppTheme.dimens.small))
        }
        Flag(
            iso = raceInfo.circuit.countryISO,
            nationality = raceInfo.circuit.country,
            modifier = Modifier.size(32.dp),
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = AppTheme.dimens.small)
        ) {
            TextBody1(
                text = raceInfo.name,
                bold = true
            )
            TextBody2(
                modifier = Modifier.padding(vertical = 2.dp),
                text = raceInfo.circuit.name
            )
        }
        Column(
            horizontalAlignment = Alignment.End
        ) {
            TextBody2(
                bold = true,
                text = "#${raceInfo.round}",
                textAlign = TextAlign.End,
            )
            if (constructor != null) {
                Spacer(Modifier.height(2.dp))
                Row(Modifier.height(IntrinsicSize.Min)) {
                    TextBody2(
                        modifier = Modifier.padding(horizontal = AppTheme.dimens.xsmall),
                        text = constructor.name
                    )
                    Box(
                        Modifier
                            .fillMaxHeight()
                            .width(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(constructor.colour)
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
            horizontal = AppTheme.dimens.medium,
            vertical = AppTheme.dimens.small
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
                .padding(horizontal = AppTheme.dimens.medium)
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
                ),
                DriverStatHistoryModel.RacePosition(
                    position = 1,
                    raceInfo = race.raceInfo,
                    constructor = race.constructors.first()
                ),
                DriverStatHistoryModel.RacePosition(
                    position = 2,
                    raceInfo = race.raceInfo,
                    constructor = race.constructors.first()
                ),
                DriverStatHistoryModel.RacePosition(
                    position = 3,
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