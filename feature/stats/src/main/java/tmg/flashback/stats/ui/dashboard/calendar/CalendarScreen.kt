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
import tmg.flashback.stats.ui.dashboard.calendar.calendar.Week
import tmg.flashback.stats.ui.dashboard.calendar.schedule.Schedule
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
                        Week(
                            model = item,
                            itemClicked = itemClicked
                        )
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