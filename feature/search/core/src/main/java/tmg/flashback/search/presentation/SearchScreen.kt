@file:OptIn(ExperimentalLayoutApi::class)

package tmg.flashback.search.presentation

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import tmg.flashback.formula1.enums.TrackLayout
import tmg.flashback.formula1.model.Circuit
import tmg.flashback.formula1.model.Constructor
import tmg.flashback.formula1.model.Driver
import tmg.flashback.formula1.model.OverviewRace
import tmg.flashback.googleanalytics.presentation.ScreenView
import tmg.flashback.search.R
import tmg.flashback.style.AppTheme
import tmg.flashback.style.input.InputPrimary
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextTitle
import tmg.flashback.ui.components.drivers.DriverImage
import tmg.flashback.ui.components.flag.Flag
import tmg.flashback.ui.components.header.Header
import tmg.flashback.ui.components.header.HeaderAction
import tmg.flashback.ui.components.layouts.MasterDetailsPane
import tmg.flashback.weekend.contract.WeekendNavigationComponent
import tmg.flashback.weekend.contract.model.ScreenWeekendData
import tmg.flashback.weekend.contract.requireWeekendNavigationComponent

@Composable
fun SearchScreenVM(
    actionUpClicked: () -> Unit,
    windowSizeClass: WindowSizeClass,
    viewModel: SearchViewModel = hiltViewModel(),
    weekendNavigationComponent: WeekendNavigationComponent = requireWeekendNavigationComponent()
) {
    val uiState = viewModel.outputs.uiState.collectAsState()

    MasterDetailsPane(
        windowSizeClass = windowSizeClass,
        master = {
            SearchScreen(
                actionUpClicked = actionUpClicked,
                windowSizeClass = windowSizeClass,
                uiState = uiState.value,
                driverClicked = viewModel.inputs::clickDriver,
                constructorClicked = viewModel.inputs::clickConstructor,
                circuitClicked = viewModel.inputs::clickCircuit,
                raceClicked = viewModel.inputs::clickRace,
                searchTermUpdated = viewModel.inputs::search,
                clear = viewModel.inputs::searchClear,
                refresh = viewModel.inputs::refresh
            )
        },
        detailsShow = uiState.value.selected != null,
        detailsActionUpClicked = viewModel.inputs::back,
        details = {
            val race = uiState.value.selected!!
            weekendNavigationComponent.Weekend(
                actionUpClicked = viewModel.inputs::back,
                windowSizeClass = windowSizeClass,
                weekendData = ScreenWeekendData(race)
            )
        }
    )
}

@Composable
internal fun SearchScreen(
    actionUpClicked: () -> Unit,
    windowSizeClass: WindowSizeClass,
    uiState: SearchScreenState,
    driverClicked: (Driver) -> Unit,
    constructorClicked: (Constructor) -> Unit,
    circuitClicked: (Circuit) -> Unit,
    raceClicked: (OverviewRace) -> Unit,
    searchTermUpdated: (String) -> Unit,
    clear: () -> Unit,
    refresh: () -> Unit
) {
    ScreenView(screenName = "Search")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.backgroundPrimary),
        content = {
            item("header") {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Header(
                        text = stringResource(id = R.string.search_title),
                        actionUpClicked = actionUpClicked,
                        action = when (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) {
                            true -> HeaderAction.MENU
                            false -> null
                        }
                    )
                    Spacer(Modifier.height(AppTheme.dimens.small))
                    val text = remember { mutableStateOf(TextFieldValue(uiState.searchTerm)) }
                    InputPrimary(
                        modifier = Modifier
                            .imeNestedScroll()
                            .padding(
                                start = AppTheme.dimens.medium,
                                end = AppTheme.dimens.medium,
                                bottom = AppTheme.dimens.medium
                            ),
                        text = text,
                        onValueChange = {
                            text.value = it
                            searchTermUpdated(it.text)
                        },
                        imeAction = ImeAction.Done,
                        placeholder = stringResource(id = R.string.search_title),
                        clear = {
                            clear()
                        }
                    )
                }
            }

            if (uiState.drivers.isNotEmpty()) {
                item("drivers") {
                    Title(R.string.search_category_driver)
                    LazyRow(
                        content = {
                            item(key = "prefix") { Spacer(Modifier.width(AppTheme.dimens.small)) }
                            items(uiState.drivers, key = { it.id }) {
                                SearchDriver(
                                    driver = it,
                                    itemClicked = driverClicked
                                )
                            }
                            item(key = "suffix") { Spacer(Modifier.width(AppTheme.dimens.small)) }
                        },
                        horizontalArrangement = Arrangement.spacedBy(AppTheme.dimens.small)
                    )
                }
            }

            if (uiState.constructors.isNotEmpty()) {
                item("constructors") {
                    Title(R.string.search_category_constructors)
                    LazyRow(
                        content = {
                            item(key = "prefix") { Spacer(Modifier.width(AppTheme.dimens.small)) }
                            items(uiState.constructors, key = { it.id }) {
                                SearchConstructor(
                                    constructor = it,
                                    itemClicked = constructorClicked
                                )
                            }
                            item(key = "suffix") { Spacer(Modifier.width(AppTheme.dimens.small)) }
                        },
                        horizontalArrangement = Arrangement.spacedBy(AppTheme.dimens.small)
                    )
                }
            }

            if (uiState.circuits.isNotEmpty()) {
                item("circuits") {
                    Title(R.string.search_category_circuits)
                    LazyRow(
                        content = {
                            item(key = "prefix") { Spacer(Modifier.width(AppTheme.dimens.small)) }
                            items(uiState.circuits, key = { it.id }) {
                                SearchCircuit(
                                    circuit = it,
                                    itemClicked = circuitClicked
                                )
                            }
                            item(key = "suffix") { Spacer(Modifier.width(AppTheme.dimens.small)) }
                        },
                        horizontalArrangement = Arrangement.spacedBy(AppTheme.dimens.small)
                    )
                }
            }

            if (uiState.races.isNotEmpty()) {
                item("races") {
                    Title(R.string.search_category_races)
                    LazyRow(
                        content = {
                            item(key = "prefix") { Spacer(Modifier.width(AppTheme.dimens.small)) }
                            items(uiState.races, key = { "${it.season}-${it.round}" }) {
                                SearchRaces(
                                    races = it,
                                    itemClicked = raceClicked
                                )
                            }
                            item(key = "suffix") { Spacer(Modifier.width(AppTheme.dimens.small)) }
                        },
                        horizontalArrangement = Arrangement.spacedBy(AppTheme.dimens.small)
                    )
                }
            }

            item {
                Spacer(Modifier.height(90.dp))
            }
        }
    )
}

@Composable
private fun Title(
    @StringRes title: Int
) {
    TextTitle(
        modifier = Modifier.padding(
            horizontal = AppTheme.dimens.medium,
            vertical = AppTheme.dimens.medium
        ),
        text = stringResource(id = title)
    )
}

@Composable
private fun SearchDriver(
    driver: Driver,
    itemClicked: (Driver) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(AppTheme.dimens.radiusSmall))
            .clickable { itemClicked(driver) }
            .background(AppTheme.colors.backgroundSecondary)
            .padding(
                horizontal = AppTheme.dimens.medium,
                vertical = AppTheme.dimens.medium
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DriverImage(photoUrl = driver.photoUrl, size = 64.dp)
        Column(
            modifier = Modifier.padding(top = AppTheme.dimens.small),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextBody1(text = driver.firstName, maxLines = 1)
            TextBody1(text = driver.lastName, bold = true, maxLines = 1)
        }
    }
}

@Composable
private fun SearchConstructor(
    constructor: Constructor,
    itemClicked: (Constructor) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier
        .clip(RoundedCornerShape(AppTheme.dimens.radiusSmall))
        .clickable { itemClicked(constructor) }
        .background(AppTheme.colors.backgroundSecondary)
        .padding(
            horizontal = AppTheme.dimens.medium,
            vertical = AppTheme.dimens.medium
        )
    ) {
        TextBody1(text = constructor.name, bold = true)
    }
}

private val circuitIconSize: Dp = 80.dp
@Composable
private fun SearchCircuit(
    circuit: Circuit,
    itemClicked: (Circuit) -> Unit,
    modifier: Modifier = Modifier
) {
    val trackIcon = TrackLayout.getTrack(circuit.id)?.getDefaultIcon()
    Column(
        modifier = modifier
            .width(circuitIconSize + (AppTheme.dimens.medium * 2))
            .clip(RoundedCornerShape(AppTheme.dimens.radiusSmall))
            .clickable { itemClicked(circuit) }
            .background(AppTheme.colors.backgroundSecondary)
            .padding(
                horizontal = AppTheme.dimens.medium,
                vertical = AppTheme.dimens.medium
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (trackIcon != null) {
            Icon(
                painter = painterResource(id = trackIcon),
                contentDescription = circuit.name,
                modifier = Modifier.size(circuitIconSize),
                tint = AppTheme.colors.contentPrimary
            )
        } else {
            Box(Modifier.size(circuitIconSize))
        }
        TextBody1(
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = AppTheme.dimens.small),
            text = circuit.name,
            bold = true,
            maxLines = 3,
        )
    }
}

private val racesTrackIcon: Dp = 80.dp
@Composable
private fun SearchRaces(
    races: OverviewRace,
    itemClicked: (OverviewRace) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .width(racesTrackIcon + (AppTheme.dimens.medium * 2))
            .clip(RoundedCornerShape(AppTheme.dimens.radiusSmall))
            .clickable { itemClicked(races) }
            .background(AppTheme.colors.backgroundSecondary)
            .padding(
                horizontal = AppTheme.dimens.medium,
                vertical = AppTheme.dimens.medium
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Flag(
            iso = races.countryISO,
            modifier = Modifier.size(32.dp)
        )
        TextBody1(
            modifier = Modifier.padding(top = AppTheme.dimens.xsmall),
            textAlign = TextAlign.Center,
            text = races.raceName,
            bold = true,
            maxLines = 2
        )
        TextBody1(
            modifier = Modifier.padding(top = AppTheme.dimens.xsmall),
            text = "${races.season} #${races.round}",
            textAlign = TextAlign.Center
        )
    }
}
