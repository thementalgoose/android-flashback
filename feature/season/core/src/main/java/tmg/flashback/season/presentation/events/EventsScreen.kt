package tmg.flashback.season.presentation.events

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import tmg.flashback.googleanalytics.constants.AnalyticsConstants.analyticsSeason
import tmg.flashback.formula1.extensions.icon
import tmg.flashback.formula1.extensions.label
import tmg.flashback.formula1.model.Event
import tmg.flashback.providers.EventProvider
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextBody2
import tmg.flashback.googleanalytics.presentation.ScreenView
import tmg.flashback.strings.R.string
import tmg.flashback.ui.components.layouts.BottomSheetContainer
import tmg.utilities.extensions.format

@Composable
fun EventsScreenVM(
    season: Int,
    modifier: Modifier = Modifier,
    viewModel: EventsViewModel = hiltViewModel()
) {
    val events = viewModel.outputs.events.collectAsState(emptyList())
    viewModel.inputs.setup(season)

    EventsScreen(
        season = season,
        events = events.value,
        modifier = modifier
    )
}

@Composable
private fun EventsScreen(
    season: Int,
    events: List<Event>,
    modifier: Modifier = Modifier,
) {
    ScreenView(screenName = "Events", args = mapOf(
        analyticsSeason to season.toString()
    ))

    BottomSheetContainer(
        modifier = modifier
            .background(AppTheme.colors.backgroundPrimary)
            .defaultMinSize(
                minWidth = Dp.Unspecified,
                minHeight = 200.dp
            ),
        title = stringResource(id = string.events_list_title, season.toString()),
        subtitle = stringResource(id = string.events_list_subtitle),
        backClicked = null
    ) {
        LazyColumn(
            modifier = Modifier.nestedScroll(rememberNestedScrollInteropConnection()),
            content = {
                items(events) {
                    Event(it)
                }
            }
        )
    }
}

@Composable
private fun Event(
    event: Event,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier
        .padding(
            vertical = AppTheme.dimens.xsmall,
            horizontal = AppTheme.dimens.medium
        )
    ) {
        println("ITEM ${event.label}")
        Icon(
            painter = painterResource(id = event.type.icon),
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = AppTheme.colors.contentSecondary
        )
        TextBody1(
            text = "${stringResource(id = event.type.label)}: ${event.label}",
            modifier = Modifier
                .padding(horizontal = AppTheme.dimens.small)
                .weight(1f)
        )
        TextBody2(
            text = event.date.format("dd MMM") ?: "",
        )
    }
}

@PreviewTheme
@Composable
private fun Preview(
    @PreviewParameter(EventProvider::class) event: Event
) {
    AppThemePreview {
        EventsScreen(
            season = 2021,
            events = listOf(event)
        )
    }
}