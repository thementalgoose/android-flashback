package tmg.flashback.season.presentation.dashboard.races

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import org.threeten.bp.LocalDate
import tmg.flashback.formula1.extensions.icon
import tmg.flashback.formula1.extensions.label
import tmg.flashback.formula1.model.OverviewRace
import tmg.flashback.providers.OverviewRaceProvider
import tmg.flashback.season.R
import tmg.flashback.season.contract.repository.models.NotificationSchedule
import tmg.flashback.season.presentation.dashboard.DashboardQuickLinks
import tmg.flashback.season.presentation.messaging.ProvidedBy
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
import tmg.flashback.ui.components.loading.SkeletonViewList
import tmg.flashback.ui.components.navigation.appBarHeight
import tmg.flashback.ui.components.now.Now
import tmg.flashback.ui.components.swiperefresh.SwipeRefresh
import tmg.flashback.ui.foldables.isWidthExpanded
import tmg.utilities.extensions.format
import tmg.utilities.extensions.startOfWeek

private const val listAlpha = 0.6f
private val expandIcon = 20.dp

@Composable
fun RacesScreenVM(
    actionUpClicked: () -> Unit,
    windowSizeClass: WindowSizeClass,
    viewModel: RacesViewModel = hiltViewModel()
) {
    val uiState = viewModel.outputs.uiState.collectAsState()

    ScheduleScreen(
        actionUpClicked = actionUpClicked,
        windowSizeClass = windowSizeClass,
        uiState = uiState.value,
        refresh = viewModel.inputs::refresh,
        tyreClicked = viewModel.inputs::clickTyre,
        itemClicked = viewModel.inputs::clickItem,
    )
}

@Composable
fun ScheduleScreen(
    actionUpClicked: () -> Unit,
    windowSizeClass: WindowSizeClass,
    uiState: RacesScreenState,
    refresh: () -> Unit,
    tyreClicked: () -> Unit,
    itemClicked: (RacesModel) -> Unit,
) {
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
                        text = uiState.season.toString(),
                        action = when (windowSizeClass.isWidthExpanded) {
                            false -> HeaderAction.MENU
                            true -> null
                        },
                        actionUpClicked = actionUpClicked,
                        overrideIcons = {
                            if (uiState.showTyres) {
                                IconButton(onClick = tyreClicked) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_tyre),
                                        contentDescription = stringResource(id = R.string.tyres_label),
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

                if (uiState.items == null) {
                    item(key = "network") {
                        NetworkError()
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

                        RacesModel.Loading -> {
                            SkeletonViewList()
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
                item(key = "footer") {
                    ProvidedBy()
                    Spacer(Modifier.height(appBarHeight))
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
    val contentDescription = stringResource(id = R.string.ab_collapsed_section,
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
            text = stringResource(id = R.string.ab_preseason_events),
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
                model = RacesModel.RaceWeek(race, notificationSchedule = fakeNotificationSchedule),
                itemClicked = { }
            )
            Spacer(Modifier.height(16.dp))
            Races(
                model = RacesModel.RaceWeek(race, notificationSchedule = fakeNotificationSchedule, showScheduleList = true),
                itemClicked = { }
            )
        }
    }
}

private val fakeNotificationSchedule = NotificationSchedule(
    freePractice = true,
    qualifying = true,
    sprint = true,
    race = true,
    sprintQualifying = true,
    other = true,
)