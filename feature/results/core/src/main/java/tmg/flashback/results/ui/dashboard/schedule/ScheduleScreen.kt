package tmg.flashback.results.ui.dashboard.schedule

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
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
import tmg.flashback.formula1.enums.SeasonTyres
import tmg.flashback.formula1.enums.getBySeason
import tmg.flashback.formula1.extensions.icon
import tmg.flashback.formula1.extensions.label
import tmg.flashback.formula1.model.OverviewRace
import tmg.flashback.providers.OverviewRaceProvider
import tmg.flashback.results.R
import tmg.flashback.results.repository.models.NotificationSchedule
import tmg.flashback.results.ui.dashboard.DashboardQuickLinks
import tmg.flashback.results.ui.messaging.ProvidedBy
import tmg.flashback.ui.components.flag.Flag
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextBody2
import tmg.flashback.ui.components.errors.NetworkError
import tmg.flashback.ui.components.header.Header
import tmg.flashback.ui.components.loading.SkeletonViewList
import tmg.flashback.ui.components.navigation.appBarHeight
import tmg.flashback.ui.components.swiperefresh.SwipeRefresh
import tmg.utilities.extensions.format

private const val listAlpha = 0.6f
private val expandIcon = 20.dp

@Composable
fun ScheduleScreenVM(
    showMenu: Boolean,
    menuClicked: (() -> Unit)? = null,
    season: Int,
    viewModel: ScheduleViewModel = hiltViewModel()
) {
    viewModel.inputs.load(season)

    val isRefreshing = viewModel.outputs.isRefreshing.observeAsState(false)
    val items = viewModel.outputs.items.observeAsState(listOf(ScheduleModel.Loading))
    val showPreseason = viewModel.outputs.showEvents.observeAsState(false)
    SwipeRefresh(
        isLoading = isRefreshing.value,
        onRefresh = viewModel.inputs::refresh
    ) {
        ScheduleScreen(
            showMenu = showMenu,
            tyreClicked = viewModel.inputs::clickTyre,
            preseasonClicked = viewModel.inputs::clickPreseason,
            menuClicked = menuClicked,
            itemClicked = viewModel.inputs::clickItem,
            season = season,
            showPreseason = showPreseason.value,
            items = items.value
        )
    }
}


@Composable
fun ScheduleScreen(
    showMenu: Boolean,
    tyreClicked: (season: Int) -> Unit,
    preseasonClicked: (season: Int) -> Unit,
    menuClicked: (() -> Unit)? = null,
    itemClicked: (ScheduleModel) -> Unit,
    season: Int,
    showPreseason: Boolean,
    items: List<ScheduleModel>?
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.backgroundPrimary),
        content = {
            item(key = "header") {
                Header(
                    text = season.toString(),
                    icon = when (showMenu) {
                        true -> painterResource(id = R.drawable.ic_menu)
                        false -> null
                    },
                    iconContentDescription = when (showMenu) {
                        true -> stringResource(id = R.string.ab_menu)
                        false -> null
                    },
                    actionUpClicked = { menuClicked?.invoke() },
                    overrideIcons = {
                        if (showPreseason) {
                            IconButton(onClick = { preseasonClicked(season) }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_preseason_events),
                                    contentDescription = stringResource(id = R.string.ab_preseason_events),
                                    tint = AppTheme.colors.contentSecondary
                                )
                            }
                        }
                        SeasonTyres.getBySeason(season)?.let { _ ->
                            IconButton(onClick = { tyreClicked(season) }) {
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
                DashboardQuickLinks(season = season)
            }

            if (items == null) {
                item(key = "network") {
                    NetworkError()
                }
            }

            items(items ?: emptyList(), key = { it.key }) { item ->
                when (item) {
                    is ScheduleModel.List -> {
                        Schedule(
                            model = item,
                            itemClicked = itemClicked
                        )
                    }
                    is ScheduleModel.Event -> {
                        Event(event = item)
                    }
                    is ScheduleModel.CollapsableList -> {
                        Spacer(Modifier.height(AppTheme.dimens.xsmall))
                        CollapsableList(
                            model = item,
                            itemClicked = itemClicked
                        )
                        Spacer(Modifier.height(AppTheme.dimens.xsmall))
                    }
                    ScheduleModel.Loading -> {
                        SkeletonViewList()
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

@Composable
private fun CollapsableList(
    model: ScheduleModel.CollapsableList,
    itemClicked: (ScheduleModel.CollapsableList) -> Unit,
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
        .semantics(mergeDescendants = true) {  }
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
    event: ScheduleModel.Event
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
            Schedule(
                model = ScheduleModel.List(race, notificationSchedule = fakeNotificationSchedule),
                itemClicked = { }
            )
            Spacer(Modifier.height(16.dp))
            Schedule(
                model = ScheduleModel.List(race, notificationSchedule = fakeNotificationSchedule, showScheduleList = true),
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
    other = true,
)