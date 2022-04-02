package tmg.flashback.statistics.ui.weekend.schedule

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.viewModel
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import tmg.flashback.formula1.model.Schedule
import tmg.flashback.providers.ScheduleListProvider
import tmg.flashback.statistics.R
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextBody2
import tmg.utilities.extensions.format
import tmg.utilities.extensions.ordinalAbbreviation

@Composable
fun ScheduleScreen(
    season: Int,
    round: Int,
    initialScheduleList: List<Schedule>
) {
    val viewModel by viewModel<ScheduleViewModel>()
    viewModel.inputs.load(season, round)

    val schedule = viewModel.outputs.list.observeAsState(viewModel.initialSchedule(initialScheduleList))
    ScheduleScreenImpl(initialScheduleList = schedule.value)
}

@Composable
private fun ScheduleScreenImpl(
    initialScheduleList: List<ScheduleModel>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier
        .fillMaxSize()
    ) {
        initialScheduleList.forEach { cell ->
            ScheduleDate(date = cell.date)
            cell.schedules.forEach { (schedule, notificationsEnabled) ->
                ScheduleItem(
                    time = schedule.timestamp.deviceLocalDateTime.toLocalTime(),
                    label = schedule.label,
                    isNotificationEnabled = notificationsEnabled
                )
            }
        }
        Spacer(Modifier.height(AppTheme.dimensions.paddingMedium))
    }
}

@Composable
private fun ScheduleDate(
    date: LocalDate,
    modifier: Modifier = Modifier
) {
    TextBody1(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                top = AppTheme.dimensions.paddingNSmall,
                bottom = AppTheme.dimensions.paddingSmall,
                start = AppTheme.dimensions.paddingMedium,
                end = AppTheme.dimensions.paddingMedium,
            ),
        text = date.format("EEEE '${date.dayOfMonth.ordinalAbbreviation}' MMMM") ?: "",
        bold = true
    )
}

@Composable
private fun ScheduleItem(
    time: LocalTime,
    label: String,
    isNotificationEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(
            vertical = AppTheme.dimensions.paddingXSmall,
            horizontal = AppTheme.dimensions.paddingMedium
        )
    ) {
        TextBody2(
            text = label,
            modifier = Modifier.weight(1f)
        )
        TextBody2(
            text = time.format("HH:mm"),
            modifier = Modifier.padding(horizontal = AppTheme.dimensions.paddingSmall)
        )
        if (isNotificationEnabled) {
            Icon(
                modifier = Modifier.size(16.dp),
                painter = painterResource(
                    id = R.drawable.ic_notification_indicator_bell,
                ),
                tint = AppTheme.colors.contentSecondary,
                contentDescription = stringResource(id = R.string.ab_up_next_bell_indicator)
            )
        } else {
            Box(modifier = Modifier.size(16.dp))
        }
    }
}

@Preview
@Composable
private fun PreviewLight() {
    AppThemePreview(isLight = true) {
        ScheduleScreenImpl(initialScheduleList = fakeSchedules)
    }
}

@Preview
@Composable
private fun PreviewDark() {
    AppThemePreview(isLight = false) {
        ScheduleScreenImpl(initialScheduleList = fakeSchedules)
    }
}

private val fakeSchedules = listOf(
    ScheduleModel(
        date = LocalDate.of(2020, 1, 1),
        schedules = listOf(
            Schedule("FP1", LocalDate.of(2020, 1, 1), LocalTime.of(11, 0)) to false,
            Schedule("FP2", LocalDate.of(2020, 1, 1), LocalTime.of(15, 0)) to false
        )
    ),
    ScheduleModel(
        date = LocalDate.of(2020, 1, 2),
        schedules = listOf(
            Schedule("FP3", LocalDate.of(2020, 1, 2), LocalTime.of(12, 0)) to false,
            Schedule("Qualifying", LocalDate.of(2020, 1, 2), LocalTime.of(15, 0)) to true
        )
    ),
    ScheduleModel(
        date = LocalDate.of(2020, 1, 3),
        schedules = listOf(
            Schedule("Race", LocalDate.of(2020, 1, 3), LocalTime.of(15, 0)) to true,
        )
    )
)