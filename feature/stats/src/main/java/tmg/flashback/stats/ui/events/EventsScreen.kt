package tmg.flashback.stats.ui.events

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import org.threeten.bp.LocalDate
import tmg.flashback.formula1.extensions.icon
import tmg.flashback.formula1.extensions.label
import tmg.flashback.formula1.model.Event
import tmg.flashback.providers.EventProvider
import tmg.flashback.stats.R
import tmg.flashback.stats.analytics.AnalyticsConstants
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextBody2
import tmg.flashback.ui.components.analytics.ScreenView
import tmg.flashback.ui.components.layouts.BottomSheet
import tmg.utilities.extensions.format

@Composable
fun EventsScreenVM(
    season: Int,
    modifier: Modifier = Modifier,
    viewModel: EventsViewModel = hiltViewModel()
) {
    val events = viewModel.outputs.events.observeAsState(emptyList())
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
    modifier: Modifier = Modifier
) {
    ScreenView(screenName = "Events", args = mapOf(
        AnalyticsConstants.analyticsSeason to season.toString()
    ))

    BottomSheet(
        modifier = modifier
            .background(AppTheme.colors.backgroundPrimary)
            .defaultMinSize(
                minWidth = Dp.Unspecified,
                minHeight = 200.dp
            ),
        title = stringResource(id = R.string.events_list_title, season.toString()),
        subtitle = stringResource(id = R.string.events_list_subtitle)
    ) {
        events.forEach { event ->
            Event(event)
        }
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
        EventsScreen(season = 2021, events = listOf(event))
    }
}