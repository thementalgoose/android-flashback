package tmg.flashback.statistics.ui.weekend

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import org.koin.androidx.compose.viewModel
import tmg.flashback.formula1.enums.TrackLayout
import tmg.flashback.formula1.model.OverviewRace
import tmg.flashback.formula1.model.Race
import tmg.flashback.formula1.model.RaceInfo
import tmg.flashback.formula1.model.Schedule
import tmg.flashback.formula1.utils.getFlagResourceAlpha3
import tmg.flashback.providers.RaceProvider
import tmg.flashback.statistics.R
import tmg.flashback.statistics.ui.weekend.constructor.ConstructorScreen
import tmg.flashback.statistics.ui.weekend.qualifying.QualifyingScreen
import tmg.flashback.statistics.ui.weekend.race.RaceScreen
import tmg.flashback.statistics.ui.weekend.schedule.ScheduleModel
import tmg.flashback.statistics.ui.weekend.schedule.ScheduleScreen
import tmg.flashback.statistics.ui.weekend.sprint.SprintScreen
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextBody2
import tmg.flashback.style.text.TextHeadline1
import tmg.flashback.style.text.TextTitle
import tmg.flashback.ui.utils.isInPreview
import tmg.utilities.extensions.format
import tmg.utilities.extensions.ordinalAbbreviation

@Composable
fun WeekendScreen(
    model: OverviewRace,
    backClicked: (() -> Unit)? = null
) {
    val viewModel by viewModel<WeekendViewModel>()
    viewModel.inputs.loadRace(model)

    val raceInfo = viewModel.outputs.raceInfo.observeAsState(model.toRaceInfo())
    val tabs = viewModel.outputs.tabs.observeAsState(WeekendViewModel.defaultTabs)
    WeekendScreenImpl(
        model = raceInfo.value,
        initialScheduleList = model.schedule,
        backClicked = backClicked,
        tabs = tabs.value
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun WeekendScreenImpl(
    model: RaceInfo,
    initialScheduleList: List<Schedule>,
    tabs: List<WeekendTabs>,
    backClicked: (() -> Unit)? = null,

    schedule: @Composable (season: Int, round: Int, initialScheduleList: List<Schedule>) -> Unit = { season, round, list -> ScheduleScreen(season = season, round = round, initialScheduleList = list) },
    qualifying: @Composable (season: Int, round: Int) -> Unit = { season, round -> QualifyingScreen(season = season, round = round) },
    sprint: @Composable (season: Int, round: Int) -> Unit = { season, round -> SprintScreen(season = season, round = round) },
    race: @Composable (season: Int, round: Int) -> Unit = { season, round -> RaceScreen(season = season, round = round) },
    constructor: @Composable (season: Int, round: Int) -> Unit = { season, round -> ConstructorScreen(season = season, round = round) }
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        BackButton(backClicked)
        RaceHeader(model = model)

        val pagerState = rememberPagerState()
        ScrollableTabRow(
            backgroundColor = AppTheme.colors.backgroundPrimary,
            contentColor = AppTheme.colors.contentPrimary,
            // Our selected tab is our current page
            selectedTabIndex = pagerState.currentPage,
            // Override the indicator, using the provided pagerTabIndicatorOffset modifier
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage])
                )
            },
            edgePadding = 0.dp
        ) {
            // Add tabs for all of our pages
            tabs.forEachIndexed { index, tab ->
                Text(
                    modifier = Modifier.padding(
                        vertical = AppTheme.dimensions.paddingNSmall,
                        horizontal = AppTheme.dimensions.paddingMedium
                    ),
                    text = stringResource(id = tab.label)
                )
            }
        }

        HorizontalPager(
            count = tabs.size,
            state = pagerState,
        ) { page ->
            when (tabs[page]) {
                WeekendTabs.SCHEDULE -> schedule(
                    season = model.season,
                    round = model.round,
                    initialScheduleList = initialScheduleList
                )
                WeekendTabs.QUALIFYING -> qualifying(
                    season = model.season,
                    round = model.round
                )
                WeekendTabs.SPRINT_QUALIFYING,
                WeekendTabs.SPRINT -> sprint(
                    season = model.season,
                    round = model.round
                )
                WeekendTabs.RACE -> race(
                    season = model.season,
                    round = model.round
                )
                WeekendTabs.CONSTRUCTOR -> constructor(
                    season = model.season,
                    round = model.round
                )
            }
        }

    }
}

@Composable
private fun BackButton(
    backClicked: (() -> Unit)?
) {
    if (backClicked != null) {
        IconButton(
            onClick = backClicked
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = stringResource(id = R.string.ab_back),
                tint = AppTheme.colors.contentPrimary
            )
        }
    } else {
        Spacer(modifier = Modifier.height(AppTheme.dimensions.paddingLarge + 16.dp))
    }
}

@Composable
private fun RaceHeader(
    model: RaceInfo,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier
        .padding(horizontal = AppTheme.dimensions.paddingMedium)
    ) {
        val track = TrackLayout.getTrack(model.circuit.id, model.season, model.name)
        Icon(
            tint = AppTheme.colors.contentPrimary,
            modifier = Modifier.size(180.dp),
            painter = painterResource(id = track?.icon ?: R.drawable.circuit_unknown),
            contentDescription = null
        )
        TextHeadline1(
            modifier = Modifier.padding(vertical = 2.dp),
            text = model.name
        )
        RaceDetails(model = model)
    }
}

@Composable
private fun RaceDetails(
    model: RaceInfo,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Column(modifier = Modifier.weight(1f)) {
            TextBody1(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = AppTheme.dimensions.paddingXSmall),
                text = model.circuit.name
            )
            TextBody2(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = AppTheme.dimensions.paddingXSmall),
                text = model.circuit.country
            )
            model.laps?.let {
                TextBody2(
                    modifier = Modifier.fillMaxWidth(),
                    bold = true,
                    text = stringResource(id = R.string.circuit_info_laps, it)
                )
            }
        }
        Column(
            horizontalAlignment = Alignment.End
        ) {
            val resourceId = when (isInPreview()) {
                true -> R.drawable.gb
                false -> LocalContext.current.getFlagResourceAlpha3(model.circuit.countryISO)
            }
            Image(
                modifier = Modifier.size(32.dp),
                painter = painterResource(id = resourceId),
                contentDescription = null,
                contentScale = ContentScale.Fit,
            )
            TextBody2(
                text = stringResource(id = R.string.race_round, model.round),
                bold = true,
                modifier = Modifier.padding(vertical = 2.dp)
            )
            TextBody2(text = model.date.format("'${model.date.dayOfMonth.ordinalAbbreviation}' MMMM") ?: "")
        }
    }
}

@Preview
@Composable
private fun PreviewLight(
    @PreviewParameter(RaceProvider::class) race: Race
) {
    AppThemePreview(isLight = true) {
        WeekendScreenImpl(
            model = race.raceInfo,
            initialScheduleList = race.schedule,
            tabs = WeekendTabs.values().toList(),

            schedule = { _, _, _ -> TextHeadline1("Schedule") },
            qualifying = { _, _ -> TextHeadline1("Qualifying") },
            sprint = { _, _ -> TextHeadline1("Sprint") },
            race = { _, _ -> TextHeadline1("Race") },
            constructor = { _, _ -> TextHeadline1("Constructor") },
        )
    }
}

@Preview
@Composable
private fun PreviewDark(
    @PreviewParameter(RaceProvider::class) race: Race
) {
    AppThemePreview(isLight = false) {
        WeekendScreenImpl(
            model = race.raceInfo,
            initialScheduleList = race.schedule,
            tabs = WeekendTabs.values().toList(),

            schedule = { _, _, _ -> TextHeadline1("Schedule") },
            qualifying = { _, _ -> TextHeadline1("Qualifying") },
            sprint = { _, _ -> TextHeadline1("Sprint") },
            race = { _, _ -> TextHeadline1("Race") },
            constructor = { _, _ -> TextHeadline1("Constructor") },
        )
    }
}