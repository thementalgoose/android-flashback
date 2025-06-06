package tmg.flashback.circuits.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Icon
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import tmg.flashback.formula1.R.drawable
import tmg.flashback.formula1.enums.TrackLayout
import tmg.flashback.formula1.extensions.positionIcon
import tmg.flashback.formula1.model.CircuitHistoryRace
import tmg.flashback.formula1.model.CircuitHistoryRaceResult
import tmg.flashback.googleanalytics.constants.AnalyticsConstants.analyticsCircuitId
import tmg.flashback.googleanalytics.presentation.ScreenView
import tmg.flashback.providers.CircuitHistoryRaceProvider
import tmg.flashback.strings.R.string
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.buttons.ButtonSecondary
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextBody2
import tmg.flashback.ui.components.drivers.DriverNumber
import tmg.flashback.ui.components.errors.NetworkError
import tmg.flashback.ui.components.flag.Flag
import tmg.flashback.ui.components.header.Header
import tmg.flashback.ui.components.header.HeaderAction
import tmg.flashback.ui.components.layouts.MasterDetailsPane
import tmg.flashback.ui.components.loading.SkeletonViewList
import tmg.flashback.ui.components.swiperefresh.SwipeRefresh
import tmg.flashback.weekend.navigation.ScreenWeekendData
import tmg.flashback.weekend.presentation.WeekendScreen
import tmg.utilities.extensions.ordinalAbbreviation
import java.time.format.DateTimeFormatter

private val trackImageSize: Dp = 180.dp
private val resultIconSize = 16.dp
private val countryBadgeSize = 32.dp

private val driverPodiumHeightP1 = 18.dp
private val driverPodiumHeightP2 = 12.dp
private val driverPodiumHeightP3 = 6.dp

@Composable
fun CircuitScreenVM(
    paddingValues: PaddingValues,
    actionUpClicked: () -> Unit,
    windowSizeClass: WindowSizeClass,
    circuitId: String,
    circuitName: String,
    viewModel: CircuitViewModel = hiltViewModel()
) {
    ScreenView(
        screenName = "Circuit Overview", args = mapOf(
            analyticsCircuitId to circuitId
        )
    )

    val uiState = viewModel.outputs.uiState.collectAsState()

    MasterDetailsPane(
        windowSizeClass = windowSizeClass,
        master = {
            SwipeRefresh(
                isLoading = uiState.value.isLoading,
                onRefresh = viewModel.inputs::refresh
            ) {
                CircuitScreen(
                    actionUpClicked = actionUpClicked,
                    windowSizeClass = windowSizeClass,
                    paddingValues = paddingValues,
                    circuitName = circuitName,
                    uiState = uiState.value,
                    itemClicked = viewModel.inputs::itemClicked,
                    linkClicked = viewModel.inputs::linkClicked,
                    locationClicked = viewModel.inputs::locationClicked
                )
            }
        },
        detailsShow = uiState.value.selectedRace != null && uiState.value.circuit != null,
        detailsActionUpClicked = viewModel.inputs::back,
        details = {
            val selectedRace = uiState.value.selectedRace!!
            val circuit = uiState.value.circuit!!
            WeekendScreen(
                actionUpClicked = viewModel.inputs::back,
                windowSizeClass = windowSizeClass,
                paddingValues = paddingValues,
                weekendInfo = ScreenWeekendData(
                    season = selectedRace.season,
                    round = selectedRace.round,
                    raceName = selectedRace.name,
                    circuitId = circuit.id,
                    circuitName = circuit.name,
                    country = circuit.country,
                    countryISO = circuit.countryISO,
                    date = selectedRace.date
                )
            )
        }
    )

}

@Composable
fun CircuitScreen(
    actionUpClicked: () -> Unit,
    windowSizeClass: WindowSizeClass,
    paddingValues: PaddingValues,
    circuitName: String,
    uiState: CircuitScreenState,
    itemClicked: (CircuitModel.Item) -> Unit,
    linkClicked: (String) -> Unit,
    locationClicked: (Double, Double, String) -> Unit,
) {
    LazyColumn(
        contentPadding = paddingValues,
        content = {
            item(key = "header") {
                Header(
                    text = circuitName,
                    action = if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) HeaderAction.BACK else null,
                    actionUpClicked = actionUpClicked
                )
            }
            if (uiState.list.isEmpty()) {
                if (!uiState.networkAvailable) {
                    item(key = "network") {
                        NetworkError()
                    }
                } else if (uiState.isLoading) {
                    item(key = "loading") {
                        SkeletonViewList()
                    }
                }
            }
            items(uiState.list, key = { it.id }) {
                when (it) {
                    is CircuitModel.Item -> Item(
                        model = it,
                        itemClicked = itemClicked
                    )

                    is CircuitModel.Stats -> Stats(
                        model = it,
                        linkClicked = linkClicked,
                        locationClicked = locationClicked
                    )
                }
            }
        })
}

@Composable
private fun Item(
    model: CircuitModel.Item,
    itemClicked: (CircuitModel.Item) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clickable(onClick = { itemClicked(model) })
            .padding(
                horizontal = AppTheme.dimens.medium,
                vertical = AppTheme.dimens.small
            )
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            TextBody1(
                text = "${model.data.season} ${model.data.name}",
                bold = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
            )
            TextBody2(
                text = stringResource(
                    id = string.circuit_race_round,
                    model.data.date.format(DateTimeFormatter.ofPattern("'${model.data.date.dayOfMonth.ordinalAbbreviation}' MMMM yyyy")),
                    model.data.round
                ),
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
        Standings(
            modifier = Modifier.width(130.dp),
            preview = model.data.preview
        )
    }
}

@Composable
private fun Stats(
    model: CircuitModel.Stats,
    linkClicked: (String) -> Unit,
    locationClicked: (Double, Double, String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        val trackIcon = TrackLayout.getTrack(circuitId = model.circuitId)?.getDefaultIcon()
            ?: drawable.circuit_unknown
        Row(modifier = Modifier.padding(horizontal = AppTheme.dimens.medium)) {
            Icon(
                painter = painterResource(id = trackIcon),
                contentDescription = null,
                tint = AppTheme.colors.contentSecondary,
                modifier = modifier
                    .size(trackImageSize)
            )
        }
        Spacer(Modifier.height(AppTheme.dimens.medium))
        Row {
            Box(
                modifier = Modifier
                    .padding(
                        top = AppTheme.dimens.medium,
                        end = AppTheme.dimens.nsmall,
                        start = AppTheme.dimens.nsmall
                    )
            ) {
                Flag(
                    iso = model.countryISO,
                    nationality = model.country,
                    modifier = Modifier.size(countryBadgeSize),
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(
                        top = AppTheme.dimens.small,
                        bottom = AppTheme.dimens.small,
                        end = AppTheme.dimens.medium
                    )
            ) {
                TextBody1(
                    text = model.country,
                    bold = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 2.dp)
                )
                if (model.startYear != null && model.endYear != null) {
                    TextBody2(
                        text = stringResource(
                            id = string.circuit_hosted_grand_prix_from,
                            model.numberOfGrandPrix,
                            model.startYear,
                            model.endYear
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    TextBody2(
                        text = stringResource(
                            id = string.circuit_hosted_grand_prix,
                            model.numberOfGrandPrix
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = AppTheme.dimens.medium)
        ) {
            model.wikipedia?.let { wiki ->
                ButtonSecondary(
                    text = stringResource(id = string.details_link_wikipedia),
                    onClick = { linkClicked(wiki) },
//                    icon = R.drawable.ic_details_wikipedia
                )
                Spacer(Modifier.width(AppTheme.dimens.medium))
            }
            model.location?.let { location ->
                ButtonSecondary(
                    text = stringResource(id = string.details_link_map),
                    onClick = {
                        locationClicked(location.lat, location.lng, model.name)
                    },
//                    icon = R.drawable.ic_details_maps
                )
                Spacer(Modifier.width(AppTheme.dimens.medium))
            }
        }

    }
}

@Composable
private fun Standings(
    preview: List<CircuitHistoryRaceResult>,
    modifier: Modifier = Modifier
) {
    val first = preview.firstOrNull { it.position == 1 } ?: return
    val second = preview.firstOrNull { it.position == 2 } ?: return
    val third = preview.firstOrNull { it.position == 3 } ?: return

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Bottom
    ) {
        Column(modifier = Modifier.weight(1f)) {
            DriverCode(
                code = second.driver.code ?: second.driver.lastName.substring(0..2),
                colour = second.constructor.colour
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(driverPodiumHeightP2)
                    .background(AppTheme.colors.f1Podium2)
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            DriverCode(
                code = first.driver.code ?: first.driver.lastName.substring(0..2),
                colour = first.constructor.colour
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(driverPodiumHeightP1)
                    .background(AppTheme.colors.f1Podium1)
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            DriverCode(
                code = third.driver.code ?: third.driver.lastName.substring(0..2),
                colour = third.constructor.colour
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(driverPodiumHeightP3)
                    .background(AppTheme.colors.f1Podium3)
            )
        }
    }
}

@Composable
private fun DriverCode(
    code: String,
    colour: Color
) {
    Row(
        Modifier
            .height(IntrinsicSize.Min)
            .padding(2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(4.dp)
                .background(colour)
        )
        DriverNumber(
            textAlign = TextAlign.Center,
            small = true,
            modifier = Modifier.weight(1f),
            number = code
        )
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(4.dp)
                .background(Color.Transparent)
        )
    }
}

@Composable
private fun StandingResult(
    model: CircuitHistoryRaceResult,
    modifier: Modifier = Modifier
) {
    val icon = model.position.positionIcon
    Row(
        modifier = modifier
            .height(IntrinsicSize.Min)
    ) {
        Icon(
            modifier = Modifier.size(resultIconSize),
            painter = painterResource(id = icon),
            contentDescription = null,
            tint = if (model.position == 1) AppTheme.colors.f1Championship else AppTheme.colors.contentSecondary
        )
        Spacer(Modifier.width(AppTheme.dimens.xsmall))
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(6.dp)
                .background(model.constructor.colour)
        )
        Spacer(Modifier.width(AppTheme.dimens.xsmall))
        DriverNumber(
            modifier = Modifier.width(resultIconSize * 3),
            small = true,
            number = model.driver.code ?: model.driver.lastName.substring(0..3)
        )
    }
}


@PreviewTheme
@Composable
private fun Preview(
    @PreviewParameter(CircuitHistoryRaceProvider::class) race: CircuitHistoryRace
) {
    AppThemePreview {
        CircuitScreen(
            circuitName = "Circuit Name",
            uiState = CircuitScreenState(
                circuitId = "circuitId",
                circuitName = "circuitName",
                list = listOf(
                    CircuitModel.Stats(
                        circuitId = "circuitId",
                        name = "name",
                        country = "England",
                        countryISO = "GBR",
                        numberOfGrandPrix = 2,
                        startYear = 2019,
                        endYear = 2020,
                        wikipedia = "wikipediaUrl",
                        location = null,
                    ),
                    CircuitModel.Item(
                        circuitId = "circuitId",
                        circuitName = "name",
                        country = "country",
                        countryISO = "countryISO",
                        race
                    )
                )
            ),
            itemClicked = { },
            linkClicked = { },
            locationClicked = { lat, lng, name -> },
            actionUpClicked = { },
            windowSizeClass = WindowSizeClass.calculateFromSize(DpSize.Unspecified),
            paddingValues = PaddingValues.Absolute()
        )
    }
}