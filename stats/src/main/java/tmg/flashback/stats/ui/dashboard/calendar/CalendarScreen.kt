package tmg.flashback.stats.ui.dashboard.calendar

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import org.threeten.bp.format.TextStyle
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextBody2
import tmg.flashback.ui.components.layouts.Container
import java.util.*

@Composable
fun CalendarScreen(

) {

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
            DayHeaders()
        }
    }
}

@Preview
@Composable
private fun PreviewDarkSchedule() {
    AppThemePreview(isLight = false) {
        Column(modifier = Modifier.fillMaxWidth()) {
            DayHeaders()
        }
    }
}


@Preview
@Composable
private fun PreviewLightCalendar() {
    AppThemePreview(isLight = true) {
        Column(modifier = Modifier.fillMaxWidth()) {
            DayHeaders()
            Week(model = CalendarModel.Week(LocalDate.of(2020, 12, 30), org.threeten.bp.Month.JANUARY, null))
            Week(model = CalendarModel.Week(LocalDate.of(2020, 1, 5), org.threeten.bp.Month.JANUARY, null))
            Week(model = CalendarModel.Week(LocalDate.of(2020, 1, 12), org.threeten.bp.Month.JANUARY, null))
        }
    }
}

@Preview
@Composable
private fun PreviewDarkCalendar() {
    AppThemePreview(isLight = false) {
        Column(modifier = Modifier.fillMaxWidth()) {
            DayHeaders()
            Week(model = CalendarModel.Week(LocalDate.of(2020, 12, 30), org.threeten.bp.Month.JANUARY, null))
            Week(model = CalendarModel.Week(LocalDate.of(2020, 1, 5), org.threeten.bp.Month.JANUARY, null))
            Week(model = CalendarModel.Week(LocalDate.of(2020, 1, 12), org.threeten.bp.Month.JANUARY, null))
        }
    }
}