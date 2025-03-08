@file:OptIn(ExperimentalMaterial3Api::class)

package tmg.flashback.season.presentation.dashboard.races

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.serialization.json.Json
import org.threeten.bp.LocalDate
import tmg.flashback.ads.ads.components.AdvertProvider
import tmg.flashback.formula1.extensions.icon
import tmg.flashback.formula1.extensions.label
import tmg.flashback.formula1.model.OverviewRace
import tmg.flashback.providers.OverviewRaceProvider
import tmg.flashback.season.R
import tmg.flashback.season.contract.repository.models.NotificationSchedule
import tmg.flashback.season.presentation.dashboard.DashboardQuickLinks
import tmg.flashback.season.presentation.dashboard.shared.seasonpicker.SeasonTitleVM
import tmg.flashback.season.presentation.events.EventsScreenVM
import tmg.flashback.season.presentation.messaging.ProvidedBy
import tmg.flashback.season.presentation.tyres.TyreCompounds
import tmg.flashback.strings.R.string
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.buttons.ButtonSecondary
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextBody2
import tmg.flashback.ui.components.errors.NetworkError
import tmg.flashback.ui.components.flag.Flag
import tmg.flashback.ui.components.header.Header
import tmg.flashback.ui.components.header.HeaderAction
import tmg.flashback.ui.components.layouts.MasterDetailsPane
import tmg.flashback.ui.components.loading.Fade
import tmg.flashback.ui.components.loading.SkeletonViewList
import tmg.flashback.ui.components.now.Now
import tmg.flashback.ui.components.swiperefresh.SwipeRefresh
import tmg.flashback.weekend.contract.WeekendNavigationComponent
import tmg.flashback.weekend.contract.model.ScreenWeekendData
import tmg.flashback.weekend.contract.requireWeekendNavigationComponent
import tmg.flashback.weekend.contract.stripWeekendJsonData
import tmg.utilities.extensions.format
import tmg.utilities.extensions.startOfWeek

private const val listAlpha = 0.6f
private val expandIcon = 20.dp

@Composable
fun RacesScreen(
    actionUpClicked: () -> Unit,
    windowSizeClass: WindowSizeClass,
    deeplink: String?,
    isRoot: (Boolean) -> Unit,
    advertProvider: AdvertProvider,
    viewModel: RacesViewModel = hiltViewModel(),
    weekendNavigationComponent: WeekendNavigationComponent = requireWeekendNavigationComponent()
) {
    val uiState = viewModel.outputs.uiState.collectAsState()
    LaunchedEffect(uiState.value.currentRace != null, block = {
        isRoot(uiState.value.currentRace != null)
    })

    LaunchedEffect(Unit, block = {
        try {
            if (deeplink?.startsWith("weekend/") == true) {
                val json = deeplink.stripWeekendJsonData()
                val model = Json.decodeFromString(ScreenWeekendData.serializer(), json)
                viewModel.inputs.deeplinkToo(model)
            }
        } catch (e: RuntimeException) { /* Do nothing */ }
    })

    MasterDetailsPane(
        windowSizeClass = windowSizeClass,
        master = {
            ScheduleScreen(
                actionUpClicked = actionUpClicked,
                windowSizeClass = windowSizeClass,
                advertProvider = advertProvider,
                uiState = uiState.value,
                refresh = viewModel.inputs::refresh,
                itemClicked = viewModel.inputs::clickItem,
            )
        },
        detailsShow = uiState.value.currentRace != null,
        detailsActionUpClicked = viewModel.inputs::back,
        details = {
            val race = uiState.value.currentRace!!
            weekendNavigationComponent.Weekend(
                actionUpClicked = viewModel.inputs::back,
                windowSizeClass = windowSizeClass,
                weekendData = race
            )
        }
    )
}

@Composable
fun ScheduleScreen(
    actionUpClicked: () -> Unit,
    windowSizeClass: WindowSizeClass,
    advertProvider: AdvertProvider,
    uiState: RacesScreenState,
    refresh: () -> Unit,
    itemClicked: (RacesModel) -> Unit,
) {
    var showEvents = rememberSaveable { mutableStateOf(false) }
    if (showEvents.value && uiState.showEvents) {
        ModalBottomSheet(
            containerColor = AppTheme.colors.backgroundContainer,
            onDismissRequest = {
                showEvents.value = false
            },
            content = {
                EventsScreenVM(
                    season = uiState.season,
                    actionUpClicked = { showEvents.value = false }
                )
            }
        )
    }

    var showTyres = rememberSaveable { mutableStateOf(false) }
    if (showTyres.value && uiState.showTyres) {
        ModalBottomSheet(
            containerColor = AppTheme.colors.backgroundContainer,
            onDismissRequest = {
                showTyres.value = false
            },
            content = {
                TyreCompounds(
                    season = uiState.season,
                    actionUpClicked = { showTyres.value = false }
                )
            }
        )
    }

    SwipeRefresh(
        isLoading = uiState.isLoading,
        onRefresh = refresh
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(AppTheme.colors.backgroundPrimary),
            content = {
                item(key = "header") {
                    Header(
                        content = {
                            SeasonTitleVM(subtitle = null)
                        },
                        action = when (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) {
                            true -> HeaderAction.MENU
                            false -> null
                        },
                        actionUpClicked = actionUpClicked,
                        overrideIcons = {
                            Fade(
                                visible = uiState.showEvents
                            ) {
                                IconButton(onClick = { showEvents.value = true }) {
                                    Icon(
                                        painter = painterResource(id = tmg.flashback.formula1.R.drawable.ic_event_type_other),
                                        contentDescription = stringResource(id = string.events_title),
                                        tint = AppTheme.colors.contentSecondary
                                    )
                                }
                            }
                            Fade(
                                visible = uiState.showTyres
                            ) {
                                IconButton(onClick = { showTyres.value = true }) {
                                    Icon(
                                        painter = painterResource(id = tmg.flashback.formula1.R.drawable.ic_tyre),
                                        contentDescription = stringResource(id = string.tyres_label),
                                        tint = AppTheme.colors.contentSecondary
                                    )
                                }
                            }
                        }
                    )
                }

                item(key = "info") {
                    DashboardQuickLinks(season = uiState.season)
                }

                if (uiState.items.isNullOrEmpty()) {
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

                items(uiState.items ?: emptyList(), key = { it.key }) { item ->
                    when (item) {
                        is RacesModel.RaceWeek -> {
                            Races(
                                model = item,
                                itemClicked = itemClicked
                            )
                        }

                        is RacesModel.Event -> {
                            Event(event = item)
                        }

                        is RacesModel.GroupedCompletedRaces -> {
                            Spacer(Modifier.height(AppTheme.dimens.xsmall))
                            CollapsableList(
                                model = item,
                                itemClicked = itemClicked
                            )
                            Spacer(Modifier.height(AppTheme.dimens.xsmall))
                        }

                        RacesModel.AllEvents -> {
                            PreseasonEvents(
                                model = RacesModel.AllEvents,
                                itemClicked = itemClicked
                            )
                        }

                        is RacesModel.EmptyWeek -> {
                            EmptyWeek(model = item)
                        }
                    }
                }

                if (uiState.showAdvert) {
                    item(key = "advert") {
                        advertProvider.NativeBanner(
                            horizontalPadding = 16.dp,
                            adIconSpacing = 12.dp,
                            adIconSize = null,
                            adIndex = 0
                        )
                    }
                }

                item(key = "footer") {
                    ProvidedBy()
                }
            }
        )
    }
}

@Composable
private fun CollapsableList(
    model: RacesModel.GroupedCompletedRaces,
    itemClicked: (RacesModel.GroupedCompletedRaces) -> Unit,
    modifier: Modifier = Modifier
) {
    val contentDescription = stringResource(id = string.ab_collapsed_section,
        model.first.raceName,
        model.first.round,
        model.last?.raceName ?: model.first.raceName,
        model.last?.round ?: model.first.round
    )
    Row(modifier = modifier
        .clickable { itemClicked(model) }
        .semantics(mergeDescendants = true) { }
        .clearAndSetSemantics { this.stateDescription = contentDescription }
        .padding(
            horizontal = AppTheme.dimens.xsmall,
            vertical = AppTheme.dimens.xsmall
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Expand()

        Row(
            modifier = modifier
                .weight(1f)
                .clip(RoundedCornerShape(AppTheme.dimens.radiusSmall))
                .padding(
                    horizontal = AppTheme.dimens.small,
                    vertical = AppTheme.dimens.small
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(Modifier.weight(1f)) {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Flag(
                        iso = model.first.countryISO,
                        nationality = null,
                        modifier = Modifier.size(20.dp)
                    )
                    TextBody1(
                        modifier = Modifier
                            .padding(horizontal = AppTheme.dimens.small)
                            .weight(1f),
                        text = model.first.raceName
                    )
                    Round(model.first.round)
                }
                if (model.last != null) {
                    Spacer(Modifier.height(8.dp))
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Flag(
                            iso = model.last.countryISO,
                            nationality = null,
                            modifier = Modifier.size(20.dp)
                        )
                        TextBody1(
                            modifier = Modifier
                                .padding(horizontal = AppTheme.dimens.small)
                                .weight(1f),
                            text = model.last.raceName
                        )
                        Round(model.last.round)
                    }
                }
            }
        }

        Expand()
    }
}

@Composable
private fun PreseasonEvents(
    model: RacesModel.AllEvents,
    itemClicked: (RacesModel) -> Unit
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(
            horizontal = AppTheme.dimens.medium,
            vertical = AppTheme.dimens.small
        )
    ) {
        ButtonSecondary(
            text = stringResource(id = string.ab_preseason_events),
            onClick = { itemClicked(model) }
        )
    }
}

@Composable
private fun EmptyWeek(
    model: RacesModel.EmptyWeek
) { 
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(AppTheme.dimens.medium)
    ) {
        if (model.monday == LocalDate.now().startOfWeek()) {
            Now(
                Modifier
                    .align(Alignment.CenterStart)
                    .alpha(0.5f))
        }
        Box(
            Modifier
                .fillMaxWidth()
                .height(2.dp)
                .align(Alignment.Center)
                .padding(horizontal = AppTheme.dimens.medium)
                .background(AppTheme.colors.backgroundSecondary)
                .alpha(0.3f)
        )
    }
}

@Composable
private fun Expand(
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxHeight()) {
        Icon(
            painter = painterResource(id = R.drawable.ic_collapsible_icon_top),
            contentDescription = null,
            modifier = Modifier.size(expandIcon),
            tint = AppTheme.colors.contentTertiary
        )
        Spacer(Modifier.height(8.dp))
        Icon(
            painter = painterResource(id = R.drawable.ic_collapsible_icon_bottom),
            contentDescription = null,
            modifier = Modifier.size(expandIcon),
            tint = AppTheme.colors.contentTertiary
        )
    }
}

@Composable
private fun Event(
    event: RacesModel.Event
) {
    Row(modifier = Modifier
        .alpha(if (event.date <= LocalDate.now()) 1f else listAlpha)
        .padding(
            vertical = AppTheme.dimens.xsmall,
            horizontal = AppTheme.dimens.medium
        )
    ) {
        Icon(
            painter = painterResource(id = event.event.type.icon),
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = AppTheme.colors.contentSecondary
        )
        TextBody1(
            text = "${stringResource(id = event.event.type.label)}: ${event.event.label}",
            modifier = Modifier
                .padding(horizontal = AppTheme.dimens.small)
                .weight(1f)
        )
        TextBody2(
            text = event.event.date.format("dd MMM") ?: "",
        )
    }
}

@PreviewTheme
@Composable
private fun PreviewSchedule(
    @PreviewParameter(OverviewRaceProvider::class) race: OverviewRace
) {
    AppThemePreview {
        Column(Modifier.fillMaxWidth()) {
            Races(
                model = RacesModel.RaceWeek(race, notificationSchedule = fakeNotificationScheduleModel),
                itemClicked = { }
            )
            Spacer(Modifier.height(16.dp))
            Races(
                model = RacesModel.RaceWeek(race, notificationSchedule = fakeNotificationScheduleModel, showScheduleList = true),
                itemClicked = { }
            )
        }
    }
}

private val fakeNotificationScheduleModel = NotificationSchedule(
    freePractice = true,
    qualifying = true,
    sprint = true,
    race = true,
    sprintQualifying = true,
    other = true,
)