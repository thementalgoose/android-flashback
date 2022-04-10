package tmg.flashback.stats.ui.dashboard.calendar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import org.koin.androidx.compose.viewModel
import org.threeten.bp.DayOfWeek
import org.threeten.bp.format.TextStyle
import tmg.flashback.formula1.model.OverviewRace
import tmg.flashback.formula1.utils.getFlagResourceAlpha3
import tmg.flashback.providers.OverviewRaceProvider
import tmg.flashback.stats.R
import tmg.flashback.stats.ui.dashboard.constructors.ConstructorStandingsScreen
import tmg.flashback.stats.ui.dashboard.constructors.ConstructorsStandingViewModel
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextBody2
import tmg.flashback.style.text.TextTitle
import tmg.flashback.ui.components.header.Header
import tmg.flashback.ui.components.layouts.Container
import tmg.flashback.ui.utils.isInPreview
import tmg.utilities.extensions.format
import tmg.utilities.extensions.ordinalAbbreviation
import java.util.*

@Composable
fun CalendarScreenVM(
    showMenu: Boolean,
    menuClicked: (() -> Unit)? = null,
    season: Int
) {
    val viewModel: CalendarViewModel by viewModel()
    viewModel.inputs.load(season)

    val isRefreshing = viewModel.outputs.isRefreshing.observeAsState(false)
    val items = viewModel.outputs.items.observeAsState(emptyList())
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = isRefreshing.value),
        onRefresh = viewModel.inputs::refresh
    ) {
        CalendarScreen(
            showMenu = showMenu,
            menuClicked = menuClicked,
            season = season,
            items = items.value ?: emptyList()
        )
    }
}


@Composable
fun CalendarScreen(
    showMenu: Boolean,
    menuClicked: (() -> Unit)? = null,
    season: Int,
    items: List<CalendarModel>
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
            items(items) { item ->
                when (item) {
                    is CalendarModel.List -> {
                        Schedule(model = item, itemClicked = {})
                    }
                    is CalendarModel.Month -> {
                        Month(model = item)
                    }
                    is CalendarModel.Week -> {
                        Week(model = item)
                    }
                }
            }
        }
    )
}

@Composable
private fun Schedule(
    model: CalendarModel.List,
    itemClicked: (CalendarModel.List) -> Unit,
    modifier: Modifier = Modifier,
    card: Boolean = false
) {
    Container(
        modifier = modifier
    ) {
        Row(modifier = Modifier
            .height(IntrinsicSize.Min)
            .fillMaxWidth()
            .clickable(onClick = {
                itemClicked(model)
            })
        ) {
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
                TextBody1(
                    text = model.model.circuitName,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 2.dp)
                )
                TextBody1(
                    text = model.model.date.format("'${model.model.date.dayOfMonth.ordinalAbbreviation}' MMMM") ?: "",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 2.dp)
                )
            }
        }
    }
}

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

@Preview
@Composable
private fun PreviewLightSchedule() {
    AppThemePreview(isLight = true) {
        Column(modifier = Modifier.fillMaxWidth()) {
//            DayHeaders()
        }
    }
}

@Preview
@Composable
private fun PreviewDarkSchedule() {
    AppThemePreview(isLight = false) {
        Column(modifier = Modifier.fillMaxWidth()) {
//            DayHeaders()
        }
    }
}


@Preview
@Composable
private fun PreviewLightCalendar(
    @PreviewParameter(OverviewRaceProvider::class) race: OverviewRace
) {
    AppThemePreview(isLight = true) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Schedule(
                model = CalendarModel.List(model = race),
                itemClicked = {}
            )
        }
    }
}

@Preview
@Composable
private fun PreviewDarkCalendar(
    @PreviewParameter(OverviewRaceProvider::class) race: OverviewRace
) {
    AppThemePreview(isLight = false) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Schedule(
                model = CalendarModel.List(model = race),
                itemClicked = {}
            )
        }
    }
}