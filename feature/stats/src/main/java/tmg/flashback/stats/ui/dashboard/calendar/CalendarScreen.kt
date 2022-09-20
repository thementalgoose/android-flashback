package tmg.flashback.stats.ui.dashboard.calendar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import org.threeten.bp.Month
import org.threeten.bp.format.TextStyle
import tmg.flashback.formula1.enums.SeasonTyres
import tmg.flashback.formula1.enums.getBySeason
import tmg.flashback.formula1.extensions.icon
import tmg.flashback.formula1.extensions.label
import tmg.flashback.formula1.model.OverviewRace
import tmg.flashback.formula1.model.Schedule
import tmg.flashback.formula1.utils.getFlagResourceAlpha3
import tmg.flashback.providers.OverviewRaceProvider
import tmg.flashback.stats.R
import tmg.flashback.stats.repository.models.NotificationSchedule
import tmg.flashback.stats.ui.dashboard.DashboardQuickLinks
import tmg.flashback.stats.ui.shared.Flag
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextBody2
import tmg.flashback.style.text.TextTitle
import tmg.flashback.ui.components.errors.NetworkError
import tmg.flashback.ui.components.header.Header
import tmg.flashback.ui.components.layouts.Container
import tmg.flashback.ui.components.loading.SkeletonViewList
import tmg.flashback.ui.utils.isInPreview
import tmg.utilities.extensions.format
import tmg.utilities.extensions.ordinalAbbreviation
import java.util.*

private val countryBadgeSize = 32.dp
private const val listAlpha = 0.6f
private const val pastScheduleAlpha = 0.2f

@Composable
fun CalendarScreenVM(
    showMenu: Boolean,
    menuClicked: (() -> Unit)? = null,
    season: Int
) {
    val viewModel: CalendarViewModel = hiltViewModel()
    viewModel.inputs.load(season)

    val isRefreshing = viewModel.outputs.isRefreshing.observeAsState(false)
    val items = viewModel.outputs.items.observeAsState(listOf(CalendarModel.Loading))
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = isRefreshing.value),
        onRefresh = viewModel.inputs::refresh
    ) {
        CalendarScreen(
            showMenu = showMenu,
            tyreClicked = viewModel.inputs::clickTyre,
            menuClicked = menuClicked,
            itemClicked = viewModel.inputs::clickItem,
            autoScrollToUpcoming = false, // TODO: Fix
            season = season,
            items = items.value
        )
    }
}


@Composable
fun CalendarScreen(
    showMenu: Boolean,
    tyreClicked: (season: Int) -> Unit,
    menuClicked: (() -> Unit)? = null,
    itemClicked: (CalendarModel) -> Unit,
    season: Int,
    autoScrollToUpcoming: Boolean,
    items: List<CalendarModel>?
) {
    val indexOf: Int? = items
        ?.indexOfFirst { it is CalendarModel.List && it.shouldShowScheduleList }
        ?.takeIf { autoScrollToUpcoming}

    val scrollState = rememberLazyListState(
        initialFirstVisibleItemIndex = indexOf?.coerceIn(0, items.size - 1) ?: 0
    )

    LazyColumn(
        state = scrollState,
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
               DashboardQuickLinks()
            }

            if (items == null) {
                item(key = "network") {
                    NetworkError()
                }
            }

            items(items ?: emptyList(), key = { it.key }) { item ->
                when (item) {
                    is CalendarModel.List -> {
                        Schedule(
                            model = item,
                            itemClicked = itemClicked
                        )
                    }
                    is CalendarModel.Event -> {
                        Event(event = item)
                    }
                    is CalendarModel.Week -> {
                        Week(model = item)
                    }
                    CalendarModel.Loading -> {
                        SkeletonViewList()
                    }
                }
            }
            item(key = "footer") {
                Spacer(Modifier.height(72.dp))
            }
        }
    )
}

//region Schedule view

@Composable
private fun Schedule(
    model: CalendarModel.List,
    itemClicked: (CalendarModel.List) -> Unit,
    modifier: Modifier = Modifier,
    card: Boolean = false
) {
    val alpha = when (model.fadeItem) {
        true -> listAlpha
        false -> 1f
    }
    Container(
        modifier = modifier
            .padding(
                top = if (model.shouldShowScheduleList) AppTheme.dimensions.paddingXSmall else 0.dp,
                bottom = if (model.shouldShowScheduleList) AppTheme.dimensions.paddingSmall else 0.dp
            )
            .alpha(alpha)
            .clickable(onClick = {
                itemClicked(model)
            }),
        isOutlined = model.shouldShowScheduleList
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
        ) {
            Row {
                Box(modifier = Modifier
                    .padding(
                        top = AppTheme.dimensions.paddingMedium,
                        end = AppTheme.dimensions.paddingNSmall,
                        start = AppTheme.dimensions.paddingNSmall
                    )
                ) {
                    Flag(
                        iso = model.model.countryISO,
                        nationality = model.model.country,
                        modifier = Modifier.size(countryBadgeSize),
                    )
                }
                Column(modifier = Modifier
                    .weight(1f)
                    .padding(
                        top = AppTheme.dimensions.paddingSmall,
                        bottom = AppTheme.dimensions.paddingSmall,
                        end = AppTheme.dimensions.paddingMedium
                    )
                ) {
                    Row {
                        TextTitle(
                            text = model.model.raceName,
                            bold = true,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(Modifier.width(AppTheme.dimensions.paddingSmall))
                        TextTitle(
                            text = "#${model.model.round}",
                            bold = true
                        )
                    }
                    Row {
                        TextBody1(
                            text = model.model.circuitName,
                            modifier = Modifier
                                .weight(1f)
                                .padding(top = 2.dp)
                        )
                        Spacer(Modifier.width(AppTheme.dimensions.paddingSmall))
                        IconRow(
                            showQualifying = model.model.hasQualifying && model.model.season > 2000,
                            showSprint = model.model.hasSprint && model.model.season > 2020,
                            showRace = model.model.hasResults
                        )
                    }

                    if (!model.shouldShowScheduleList) {
                        TextBody2(
                            text = model.model.date.format("'${model.model.date.dayOfMonth.ordinalAbbreviation}' MMMM") ?: "",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 2.dp)
                        )
                    }
                }
            }
            if (model.shouldShowScheduleList) {
                Dates(
                    scheduleList = model.model.schedule,
                    notificationSchedule = model.notificationSchedule,
                    modifier = Modifier.padding(top = AppTheme.dimensions.paddingXSmall)
                )
            }
        }
    }
}

@Composable
private fun Event(
    event: CalendarModel.Event
) {
    Row(modifier = Modifier
        .alpha(listAlpha)
        .padding(
            vertical = AppTheme.dimensions.paddingXSmall,
            horizontal = AppTheme.dimensions.paddingMedium
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
                .padding(horizontal = AppTheme.dimensions.paddingSmall)
                .weight(1f)
        )
        TextBody2(
            text = event.event.date.format("dd MMM") ?: "",
        )
    }
}

@Composable
private fun RowScope.IconRow(
    showQualifying: Boolean,
    showSprint: Boolean,
    showRace: Boolean,
    iconSize: Dp = 16.dp
) {
    Icon(
        modifier = Modifier
            .size(iconSize)
            .align(Alignment.CenterVertically),
        painter = painterResource(id = R.drawable.ic_status_results_qualifying),
        contentDescription = stringResource(id = R.string.ab_has_qualifying_results),
        tint = if (showQualifying) AppTheme.colors.f1ResultsFull else AppTheme.colors.backgroundTertiary
    )
    Spacer(Modifier.width(2.dp))
    if (showSprint) {
        Icon(
            modifier = Modifier
                .size(iconSize)
                .align(Alignment.CenterVertically),
            painter = painterResource(id = R.drawable.ic_status_results_sprint),
            contentDescription = stringResource(id = R.string.ab_has_sprint_results),
            tint = AppTheme.colors.f1ResultsFull
        )
        Spacer(Modifier.width(2.dp))
    }
    Icon(
        modifier = Modifier
            .size(iconSize)
            .align(Alignment.CenterVertically),
        painter = painterResource(id = R.drawable.ic_status_results_race),
        contentDescription = stringResource(id = R.string.ab_has_race_results),
        tint = if (showRace) AppTheme.colors.f1ResultsFull else AppTheme.colors.backgroundTertiary
    )
}

@Composable
private fun Dates(
    scheduleList: List<Schedule>,
    notificationSchedule: NotificationSchedule,
    modifier: Modifier = Modifier
) {
    val schedule = scheduleList.groupBy { it.timestamp.deviceLocalDateTime.toLocalDate() }
    var targetIndex = schedule
        .map { it.value.any { !it.timestamp.isInPast } }
        .indexOfFirst { it }
    if (targetIndex == -1) targetIndex = schedule.size - 1
    val scrollState = rememberLazyListState(
        initialFirstVisibleItemIndex = targetIndex.coerceIn(0, schedule.size - 1)
    )

    LazyRow(
        modifier = modifier.padding(bottom = AppTheme.dimensions.paddingSmall),
        state = scrollState,
        content = {
            item {
                Spacer(Modifier.width(countryBadgeSize + (AppTheme.dimensions.paddingNSmall * 2)))
            }
            for ((date, list) in schedule) {
                val alpha = if (date.isBefore(LocalDate.now()) || list.all { it.timestamp.isInPast }) pastScheduleAlpha else 1f
                item {
                    Column {
                        TextBody2(
                            text = date.format("EEE '${date.dayOfMonth.ordinalAbbreviation}' MMMM")
                                ?: "",
                            modifier = Modifier
                                .alpha(alpha)
                                .padding(
                                    bottom = AppTheme.dimensions.paddingXSmall
                                )
                        )
                        Row {
                            list.forEach {
                                DateCard(
                                    schedule = it,
                                    showNotificationBadge = notificationSchedule.getByLabel(it.label)
                                )
                                Spacer(Modifier.width(AppTheme.dimensions.paddingSmall))
                            }
                        }
                    }
                    Spacer(Modifier.width(AppTheme.dimensions.paddingMedium))
                }
            }
        }
    )
}

@Composable
private fun DateCard(
    schedule: Schedule,
    showNotificationBadge: Boolean,
    modifier: Modifier = Modifier
) {
    val alpha = if (schedule.timestamp.isInPast) pastScheduleAlpha else 1f
    Column(modifier = modifier
        .width(IntrinsicSize.Max)
        .clip(RoundedCornerShape(AppTheme.dimensions.radiusSmall))
        .background(AppTheme.colors.backgroundTertiary)
        .alpha(alpha)
        .padding(
            vertical = AppTheme.dimensions.paddingNSmall,
            horizontal = AppTheme.dimensions.paddingNSmall
        )
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = AppTheme.dimensions.paddingSmall),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextBody1(
                textAlign = TextAlign.Center,
                text = schedule.label
            )
            if (showNotificationBadge) {
                Spacer(Modifier.width(4.dp))
                Icon(
                    modifier = Modifier.size(16.dp),
                    tint = AppTheme.colors.contentSecondary,
                    painter = painterResource(id = R.drawable.ic_notification_indicator_bell),
                    contentDescription = stringResource(id = R.string.ab_notifications_enabled)
                )
            }
        }
        TextBody2(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = schedule.timestamp.deviceLocalDateTime.toLocalTime().format("HH:mm"),
            bold = true
        )
    }
}

//endregion




//region Calendar view

@Composable
private fun DayHeaders() {
    CalendarContainer {
        DayOfWeek.values().forEach {
            TextBody2(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                text = it.getDisplayName(TextStyle.SHORT, Locale.getDefault())
            )
        }
    }
}

@Composable
private fun Week(
    model: CalendarModel.Week
) {
    CalendarContainer {
        Column {
            if (model.season == model.startOfWeek.year) {
                TextBody1(text = model.startOfWeek.format("dd MMM yyyy") ?: "-")
            }
            if (model.startOfWeek.dayOfMonth == 1) {
                Month(model.startOfWeek.month)
            }
            if (model.startOfWeek.month != model.endOfWeek.month && model.endOfWeek.year == model.season) {
                Month(model.endOfWeek.month)
                TextBody2(text = model.endOfWeek.format("dd MMM yyyy") ?: "-")
            }
        }
    }
}

@Composable
private fun Month(
    model: Month
) {
    TextTitle(text = model.getDisplayName(TextStyle.FULL, Locale.getDefault()))
}

@Composable
private fun CalendarContainer(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    Row(modifier = modifier
        .fillMaxWidth()
        .padding(
            horizontal = AppTheme.dimensions.paddingMedium,
            vertical = AppTheme.dimensions.paddingXSmall
        )
    ) {
        content()
    }
}

//endregion




//@Composable
//private fun Links(
//    events: List<Event>,
//    showTyres: Boolean
//)

//@Preview
//@Composable
//private fun PreviewLightCalendar() {
//    AppThemePreview(isLight = true) {
//        Schedule(
//            model = CalendarModel.List(race),
//            itemClicked = { }
//        )
//    }
//}
//
//@Preview
//@Composable
//private fun PreviewDarkCalendar() {
//    AppThemePreview(isLight = false) {
//        Schedule(
//            model = CalendarModel.List(race),
//            itemClicked = { }
//        )
//    }
//}


@PreviewTheme
@Composable
private fun PreviewSchedule(
    @PreviewParameter(OverviewRaceProvider::class) race: OverviewRace
) {
    AppThemePreview {
        Column(Modifier.fillMaxWidth()) {
            Schedule(
                model = CalendarModel.List(race, notificationSchedule = fakeNotificationSchedule),
                itemClicked = { }
            )
            Spacer(Modifier.height(16.dp))
            Schedule(
                model = CalendarModel.List(race, notificationSchedule = fakeNotificationSchedule, showScheduleList = true),
                itemClicked = { }
            )
        }
    }
}

private val fakeNotificationSchedule = NotificationSchedule(
    freePractice = true,
    qualifying = true,
    race = true,
    other = true,
)