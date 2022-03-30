package tmg.flashback.statistics.ui.dashboard.calendar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import org.koin.androidx.compose.viewModel
import tmg.flashback.formula1.model.OverviewRace
import tmg.flashback.formula1.utils.getFlagResourceAlpha3
import tmg.flashback.providers.OverviewRaceProvider
import tmg.flashback.statistics.R
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextTitle
import tmg.flashback.ui.components.header.Header
import tmg.flashback.ui.utils.isInPreview
import tmg.utilities.extensions.format
import tmg.utilities.extensions.ordinalAbbreviation

@Composable
fun CalendarScreen(
    menuClicked: (() -> Unit)?,
    itemClicked: (OverviewRace) -> Unit,
    season: Int,
) {
    val viewModel by viewModel<CalendarViewModel>()

    val list = viewModel.outputs.items.observeAsState()
    val isRefreshing = viewModel.outputs.isRefreshing.observeAsState(false)

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = isRefreshing.value),
        onRefresh = viewModel.inputs::refresh
    ) {
        CalendarScreenImpl(
            season = season,
            menuClicked = menuClicked,
            itemClicked = itemClicked,
            list = list.value ?: emptyList()
        )
    }
}

@Composable
private fun CalendarScreenImpl(
    season: Int,
    list: List<OverviewRace>,
    itemClicked: (model: OverviewRace) -> Unit,
    menuClicked: (() -> Unit)?,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.backgroundPrimary),
        content = {
            item {
                Header(
                    text = season.toString(),
                    icon = menuClicked?.let { painterResource(id = R.drawable.ic_menu)},
                    iconContentDescription = stringResource(id = R.string.ab_menu),
                    actionUpClicked = {
                        menuClicked?.invoke()
                    }
                )
            }
            items(list, key = { "s${it.season}r${it.round}" }) {
                ScheduleView(
                    weekendOverview = it,
                    itemClicked = itemClicked
                )
            }
            item {
                Spacer(Modifier.height(AppTheme.dimensions.paddingXLarge))
            }
        }
    )
}

@Composable
private fun ScheduleView(
    weekendOverview: OverviewRace,
    itemClicked: (model: OverviewRace) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier
        .height(IntrinsicSize.Min)
        .fillMaxWidth()
        .clickable(onClick = {
            itemClicked(weekendOverview)
        })
    ) {
        val colorForPanel = when {
            weekendOverview.hasResults -> AppTheme.colors.f1ResultsFull
            weekendOverview.hasQualifying -> AppTheme.colors.f1ResultsPartial
            else -> AppTheme.colors.f1ResultsNeutral
        }

        Box(modifier = Modifier
            .alpha(0.2f)
            .background(colorForPanel)
            .width(4.dp)
            .fillMaxHeight())
        Box(modifier = Modifier
            .padding(
                top = AppTheme.dimensions.paddingMedium,
                end = AppTheme.dimensions.paddingNSmall,
                start = AppTheme.dimensions.paddingNSmall
            )
        ) {
            val resourceId = when (isInPreview()) {
                true -> R.drawable.gb
                false -> LocalContext.current.getFlagResourceAlpha3(weekendOverview.countryISO)
            }
            Image(
                painter = painterResource(id = resourceId),
                modifier = Modifier.size(32.dp),
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
                    text = weekendOverview.raceName,
                    bold = true,
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(AppTheme.dimensions.paddingSmall))
                TextTitle(
                    text = "#${weekendOverview.round}",
                    bold = true
                )
            }
            TextBody1(
                text = weekendOverview.circuitName,
                modifier = Modifier.padding(top = 2.dp)
            )
            TextBody1(
                text = weekendOverview.date.format("'${weekendOverview.date.dayOfMonth.ordinalAbbreviation}' MMMM") ?: "",
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}

@Preview
@Composable
private fun PreviewLight(
    @PreviewParameter(OverviewRaceProvider::class) model: OverviewRace
) {
    AppThemePreview(isLight = true) {
        CalendarScreenImpl(
            season = 2022,
            list = List(5) { model.copy(round = it) },
            itemClicked = {},
            menuClicked = {}
        )
    }
}

@Preview
@Composable
private fun PreviewDark(
    @PreviewParameter(OverviewRaceProvider::class) model: OverviewRace
) {
    AppThemePreview(isLight = false) {
        CalendarScreenImpl(
            season = 2022,
            list = List(5) { model.copy(round = it) },
            itemClicked = {},
            menuClicked = {}
        )
    }
}