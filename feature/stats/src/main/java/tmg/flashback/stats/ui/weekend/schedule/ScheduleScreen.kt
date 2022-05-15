package tmg.flashback.stats.ui.weekend.schedule

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.viewModel
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.formula1.model.Event
import tmg.flashback.formula1.model.Race
import tmg.flashback.formula1.model.RaceInfo
import tmg.flashback.formula1.model.Schedule
import tmg.flashback.providers.RaceProvider
import tmg.flashback.stats.R
import tmg.flashback.stats.ui.weekend.info.RaceInfoHeader
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextBody2
import tmg.flashback.ui.annotations.PreviewDevices
import tmg.flashback.ui.annotations.PreviewTheme
import tmg.utilities.extensions.format
import tmg.utilities.extensions.ordinalAbbreviation

@Composable
fun ScheduleScreenVM(
    info: RaceInfo
) {
    val viewModel by viewModel<ScheduleViewModel>()
    viewModel.inputs.load(
        season = info.season,
        round = info.round
    )

    val items = viewModel.outputs.list.observeAsState(initial = emptyList())
    ScheduleScreen(
        info = info,
        items = items.value
    )
}

@Composable
fun ScheduleScreen(
    info: RaceInfo,
    items: List<ScheduleModel>
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        content = {
            item("header") {
                RaceInfoHeader(model = info)
            }
            items(items, key = { it.toString() }) {
                Column(Modifier.fillMaxWidth()) {
                    Title(it.date)
                    it.schedules.forEach { (schedule, isNotificationSet) ->
                        EventItem(item = schedule, showNotificationBell = isNotificationSet)
                    }
                }
            }
        }
    )
}

@Composable
private fun Title(
    date: LocalDate,
    modifier: Modifier = Modifier
) {
    TextBody1(
        text = date.format("EEEE '${date.dayOfMonth.ordinalAbbreviation}' MMMM") ?: "",
        bold = true,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = AppTheme.dimensions.paddingXSmall)
    )
}

@Composable
private fun EventItem(
    item: Schedule,
    showNotificationBell: Boolean,
    modifier: Modifier = Modifier
) {
    val timestamp = item.timestamp.deviceLocalDateTime
    Row(modifier = modifier
        .fillMaxWidth()
        .padding(vertical = AppTheme.dimensions.paddingXSmall)
    ) {
        TextBody2(
            text = item.label,
            modifier = Modifier.weight(1f)
        )
        TextBody2(
            text = timestamp.format("HH:mm"),
            modifier = Modifier
                .padding(
                    horizontal = AppTheme.dimensions.paddingSmall
                )
        )
        if (showNotificationBell) {
            Icon(
                painter = painterResource(id = R.drawable.ic_notification_indicator_bell),
                contentDescription = stringResource(id = R.string.ab_notifications_enabled),
                tint = AppTheme.colors.contentSecondary,
                modifier = Modifier
                    .size(12.dp)
                    .align(Alignment.CenterVertically)
            )
        } else {
            Box(Modifier.size(12.dp))
        }
    }
}

@Preview
@Composable
private fun PreviewLight(
    @PreviewParameter(RaceProvider::class) race: Race
) {
    AppThemePreview(isLight = true) {
        ScheduleScreen(
            info = race.raceInfo,
            items = listOf(
                ScheduleModel(
                    date = LocalDate.of(2020, 1, 1),
                    schedules = listOf(
                        Schedule("FP1", LocalDate.now(), LocalTime.of(9, 0)) to true,
                        Schedule("FP2", LocalDate.now(), LocalTime.of(11, 0)) to false
                    )
                ),
                ScheduleModel(
                    date = LocalDate.of(2020, 1, 2),
                    schedules = listOf(
                        Schedule("FP3", LocalDate.now(), LocalTime.of(9, 0)) to true,
                        Schedule("Qualifying", LocalDate.now(), LocalTime.of(12, 0)) to true
                    )
                )
            )
        )
    }
}