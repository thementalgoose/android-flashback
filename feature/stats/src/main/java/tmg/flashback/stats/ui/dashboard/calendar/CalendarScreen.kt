package tmg.flashback.stats.ui.dashboard.calendar

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import org.koin.androidx.compose.inject
import org.koin.androidx.compose.viewModel
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import org.threeten.bp.format.TextStyle
import tmg.flashback.formula1.model.OverviewRace
import tmg.flashback.formula1.model.Schedule
import tmg.flashback.formula1.utils.getFlagResourceAlpha3
import tmg.flashback.notifications.di.notificationModule
import tmg.flashback.providers.OverviewRaceProvider
import tmg.flashback.stats.R
import tmg.flashback.stats.repository.NotificationRepository
import tmg.flashback.stats.repository.models.NotificationSchedule
import tmg.flashback.stats.ui.dashboard.DashboardQuickLinks
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextBody2
import tmg.flashback.style.text.TextTitle
import tmg.flashback.ui.components.loading.SkeletonView
import tmg.flashback.ui.components.errors.NetworkError
import tmg.flashback.ui.components.header.Header
import tmg.flashback.ui.components.layouts.Container
import tmg.flashback.ui.utils.isInPreview
import tmg.utilities.extensions.format
import tmg.utilities.extensions.ordinalAbbreviation
import java.util.*

private val countryBadgeSize = 32.dp
private const val pastScheduleAlpha = 0.2f

@Composable
fun CalendarScreenVM(
    showMenu: Boolean,
    menuClicked: (() -> Unit)? = null,
    season: Int
) {
    val viewModel: CalendarViewModel by viewModel()
    viewModel.inputs.load(season)

    val isRefreshing = viewModel.outputs.isRefreshing.observeAsState(false)
    val items = viewModel.outputs.items.observeAsState(listOf(CalendarModel.Loading))
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = isRefreshing.value),
        onRefresh = viewModel.inputs::refresh
    ) {
        CalendarScreen(
            showMenu = showMenu,
            menuClicked = menuClicked,
            itemClicked = viewModel.inputs::clickItem,
            season = season,
            items = items.value
        )
    }
}


@Composable
fun CalendarScreen(
    showMenu: Boolean,
    menuClicked: (() -> Unit)? = null,
    itemClicked: (CalendarModel) -> Unit,
    season: Int,
    items: List<CalendarModel>?
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
                    actionUpClicked = { menuClicked?.invoke() }
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

            items(items ?: emptyList()) { item ->
                when (item) {
                    is CalendarModel.List -> {
                        Schedule(
                            model = item,
                            itemClicked = itemClicked
                        )
                    }
                    is CalendarModel.Month -> {
                        Month(model = item)
                    }
                    is CalendarModel.Week -> {
                        Week(model = item)
                    }
                    CalendarModel.Loading -> {
                        SkeletonView()
                        SkeletonView()
                        SkeletonView()
                        SkeletonView()
                        SkeletonView()
                        SkeletonView()
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
        true -> 0.6f
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
                val colorForPanel = when {
                    model.model.hasResults -> AppTheme.colors.f1ResultsFull
                    model.model.hasQualifying -> AppTheme.colors.f1ResultsPartial
                    else -> AppTheme.colors.f1ResultsNeutral
                }

                Box(modifier = Modifier
                    .padding(
                        top = AppTheme.dimensions.paddingMedium,
                        end = AppTheme.dimensions.paddingNSmall,
                        start = AppTheme.dimensions.paddingNSmall
                    )
                ) {
                    val resourceId = when (isInPreview()) {
                        true -> R.drawable.gb
                        false -> LocalContext.current.getFlagResourceAlpha3(model.model.countryISO)
                    }
                    Image(
                        painter = painterResource(id = resourceId),
                        modifier = Modifier.size(countryBadgeSize),
                        contentDescription = null
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
                            // TODO: Remove this check for 2000 and fix the API response data!
                            showQualifying = model.model.hasQualifying && model.model.season > 2000,
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
private fun RowScope.IconRow(
    showQualifying: Boolean,
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
    // TODO: Add support for this in the API response and then here!
//    Icon(
//        modifier = Modifier
//            .size(iconSize)
//            .align(Alignment.CenterVertically),
//        painter = painterResource(id = R.drawable.ic_status_results_sprint),
//        contentDescription = stringResource(id = R.string.ab_has_sprint_results),
//        tint = AppTheme.colors.contentTertiary
//    )
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
    Row(modifier = modifier
        .horizontalScroll(rememberScrollState())
        .padding(bottom = AppTheme.dimensions.paddingSmall)
    ) {
        Spacer(Modifier.width(countryBadgeSize + (AppTheme.dimensions.paddingNSmall * 2)))
        for ((date, list) in schedule) {
            val alpha = if (date.isBefore(LocalDate.now())) pastScheduleAlpha else 1f
            Column {
                TextBody2(
                    text = date.format("EEE '${date.dayOfMonth.ordinalAbbreviation}' MMMM") ?: "",
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
private fun Month(
    model: CalendarModel.Month
) {
    TextBody1(
        modifier = Modifier.padding(
            horizontal = AppTheme.dimensions.paddingMedium,
            vertical = AppTheme.dimensions.paddingXSmall
        ),
        text = model.month.getDisplayName(TextStyle.FULL, Locale.getDefault()),
        bold = true
    )
}

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
        for (x in 0 until 7) {
            val date = model.weekBeginning.plusDays(x.toLong())
            if (date.month != model.month) {
                Box(modifier = Modifier.weight(1f))
            } else {
                Box(modifier = Modifier.weight(1f)) {
                    Container(
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.Center),
                        boxRadius = 12.dp,
                        isOutlined = true
                    ) {
                        TextBody2(
                            modifier = Modifier
                                .width(24.dp)
                                .align(Alignment.Center),
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            text = date.dayOfMonth.toString()
                        )
                    }
                }
            }
        }
    }
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


@Preview
@Composable
private fun PreviewLightSchedule(
    @PreviewParameter(OverviewRaceProvider::class) race: OverviewRace
) {
    AppThemePreview(isLight = true) {
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

@Preview
@Composable
private fun PreviewDarkSchedule(
    @PreviewParameter(OverviewRaceProvider::class) race: OverviewRace
) {
    AppThemePreview(isLight = false) {
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