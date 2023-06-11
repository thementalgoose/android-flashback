package tmg.flashback.results.ui.dashboard.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import org.threeten.bp.LocalDate
import tmg.flashback.formula1.enums.SeasonTyres
import tmg.flashback.formula1.enums.getBySeason
import tmg.flashback.formula1.model.OverviewRace
import tmg.flashback.providers.OverviewRaceProvider
import tmg.flashback.results.R
import tmg.flashback.results.repository.models.NotificationSchedule
import tmg.flashback.results.ui.dashboard.DashboardQuickLinks
import tmg.flashback.results.ui.messaging.ProvidedBy
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.ui.components.errors.NetworkError
import tmg.flashback.ui.components.header.Header
import tmg.flashback.ui.components.loading.SkeletonViewList
import tmg.flashback.ui.components.navigation.appBarHeight
import tmg.flashback.ui.components.swiperefresh.SwipeRefresh

private val countryBadgeSize = 32.dp
private const val listAlpha = 0.6f
private const val pastScheduleAlpha = 0.2f

@Composable
fun CalendarScreenVM(
    showMenu: Boolean,
    menuClicked: (() -> Unit)? = null,
    season: Int,
    viewModel: CalendarViewModel = hiltViewModel()
) {
    viewModel.inputs.load(season)

    val isRefreshing = viewModel.outputs.isRefreshing.collectAsState(false)
    val items = viewModel.outputs.items.collectAsState(listOf(CalendarModel.Loading))
    SwipeRefresh(
        isLoading = isRefreshing.value,
        onRefresh = viewModel.inputs::refresh
    ) {
        CalendarScreen(
            showMenu = showMenu,
            tyreClicked = viewModel.inputs::clickTyre,
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
    tyreClicked: (season: Int) -> Unit,
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
                DashboardQuickLinks(season = season)
            }

            if (items == null) {
                item(key = "network") {
                    NetworkError()
                }
            }

            items(items ?: emptyList(), key = { it.key }) { item ->
                when (item) {
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
                ProvidedBy()
                Spacer(Modifier.height(appBarHeight))
            }
        }
    )
}

@PreviewTheme
@Composable
private fun PreviewSchedule(
    @PreviewParameter(OverviewRaceProvider::class) race: OverviewRace
) {
    AppThemePreview {
        Column(Modifier.fillMaxWidth()) {
            Week(
                model = CalendarModel.Week(
                    season = 2022,
                    startOfWeek = LocalDate.of(2022, 9, 26),
                    race = null
                ),
                itemClicked = { }
            )
            Spacer(Modifier.height(16.dp))
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