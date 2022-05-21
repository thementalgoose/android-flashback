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
import tmg.flashback.formula1.model.Race
import tmg.flashback.formula1.model.Schedule
import tmg.flashback.providers.RaceProvider
import tmg.flashback.stats.R
import tmg.flashback.stats.ui.weekend.WeekendInfo
import tmg.flashback.stats.ui.weekend.from
import tmg.flashback.stats.ui.weekend.info.RaceInfoHeader
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextBody2
import tmg.utilities.extensions.format
import tmg.utilities.extensions.ordinalAbbreviation

@Composable
fun ScheduleScreenVM(
    info: WeekendInfo,
    actionUpClicked: () -> Unit
) {
    val viewModel by viewModel<ScheduleViewModel>()
    viewModel.inputs.load(
        season = info.season,
        round = info.round
    )

    val items = viewModel.outputs.list.observeAsState(initial = emptyList())
    ScheduleScreen(
        info = info,
        actionUpClicked = actionUpClicked,
        items = items.value
    )
}

@Composable
fun ScheduleScreen(
    info: WeekendInfo,
    items: List<ScheduleModel>,
    actionUpClicked: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        content = {
            item("header") {
                RaceInfoHeader(
                    model = info,
                    actionUpClicked = actionUpClicked
                )
            }
            items(items, key = { it.toString() }) {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppTheme.dimensions.paddingMedium)
                ) {
                    Title(it.date)
                    it.schedules.forEach { (schedule, isNotificationSet) ->
                        EventItem(item = schedule, showNotificationBell = isNotificationSet)
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
        if (showNotificationBell) {
            Icon(
                painter = painterResource(id = R.drawable.ic_notification_indicator_bell),
                contentDescription = stringResource(id = R.string.ab_notifications_enabled),
                tint = AppTheme.colors.contentSecondary,
                modifier = Modifier
                    .size(16.dp)
                    .align(Alignment.CenterVertically)
            )
        } else {
            Box(Modifier.size(16.dp))
        }
        TextBody2(
            text = timestamp.format("HH:mm"),
            modifier = Modifier
                .padding(
                    start = AppTheme.dimensions.paddingSmall
                )
        )
    }
}

@PreviewTheme
@Composable
private fun Preview(
    @PreviewParameter(RaceProvider::class) race: Race
) {
    AppThemePreview {
        ScheduleScreen(
            actionUpClicked = { },
            info = WeekendInfo.from(race.raceInfo),
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