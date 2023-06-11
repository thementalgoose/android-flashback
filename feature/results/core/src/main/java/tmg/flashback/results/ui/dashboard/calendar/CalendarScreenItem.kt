package tmg.flashback.results.ui.dashboard.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.threeten.bp.LocalDate
import org.threeten.bp.Month
import org.threeten.bp.format.TextStyle
import tmg.flashback.formula1.model.OverviewRace
import tmg.flashback.providers.OverviewRaceProvider
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextTitle
import tmg.flashback.ui.components.flag.Flag
import java.util.*

private val circleSize: Dp = 28.dp

@Composable
internal fun Week(
    model: CalendarModel.Week,
    itemClicked: (CalendarModel.Week) -> Unit,
    modifier: Modifier = Modifier
) {
    Column {
        if (model.season == model.startOfWeek.year) {
            CalendarWeek(
                modifier = Modifier.clickable(
                    onClick = { itemClicked(model) },
                    enabled = model.race != null
                ),
                startOfWeek = model.startOfWeek,
                month = model.startOfWeek.month,
                content = { date ->
                    Day(date, isToday = date == LocalDate.now())
                },
                endContent = {
                    if (model.race != null) {
                        Flag(iso = model.race.countryISO)
                    }
                }
            )
        }
        if (model.startOfWeek.dayOfMonth == 1) {
            Month(model.startOfWeek.month)
        }
        if (model.startOfWeek.month != model.endOfWeek.month && model.endOfWeek.year == model.season) {
            Month(model.endOfWeek.month)
            CalendarWeek(
                modifier = Modifier.clickable(
                    onClick = { itemClicked(model) },
                    enabled = model.race != null
                ),
                startOfWeek = model.startOfWeek,
                month = model.endOfWeek.month,
                content = { date ->
                    Day(date, isToday = date == LocalDate.now())
                },
                endContent = {
                    if (model.race != null) {
                        Flag(iso = model.race.countryISO)
                    }
                }
            )
        }
    }
}

@Composable
private fun Month(
    model: Month,
    modifier: Modifier = Modifier
) {
    TextTitle(
        text = model.getDisplayName(TextStyle.FULL, Locale.getDefault()),
        modifier = modifier.padding(
            horizontal = AppTheme.dimens.medium,
            vertical = AppTheme.dimens.small
        )
    )
}

@Composable
private fun CalendarWeek(
    startOfWeek: LocalDate,
    month: Month,
    content: @Composable (date: LocalDate) -> Unit,
    modifier: Modifier = Modifier,
    endContent: @Composable () -> Unit = { },
    showMismatchedMonths: Boolean = false,
) {
    Row(
        modifier.fillMaxWidth()
            .padding(
                vertical = AppTheme.dimens.xxsmall,
                horizontal = AppTheme.dimens.small
            )
    ) {
        for (x in 0 until 7) {
            Box(modifier = Modifier
                .weight(1f)
                .height(circleSize),
                contentAlignment = Alignment.Center,
                content = {
                    val date = startOfWeek.plusDays(x.toLong())
                    if (date.month == month || showMismatchedMonths) {
                        content(date)
                    }
                }
            )
        }
        Box(modifier = Modifier
            .weight(0.7f)
            .height(circleSize),
            contentAlignment = Alignment.Center,
            content = {
                if (startOfWeek.plusDays(6L).month == month) {
                    endContent()
                }
            }
        )
    }
}

@Composable
private fun Day(
    date: LocalDate,
    isToday: Boolean = false
) {
    Box(
        Modifier
            .size(circleSize)
            .clip(CircleShape)
            .background(
                when (isToday) {
                    false -> AppTheme.colors.backgroundSecondary
                    true -> AppTheme.colors.primaryDark
                }
            )
    ) {
        TextBody1(
            textColor = when (isToday) {
                true -> Color.White
                false -> AppTheme.colors.contentPrimary
            },
            modifier = Modifier.align(Alignment.Center),
            text = date.dayOfMonth.toString()
        )
    }
}

@PreviewTheme
@Composable
private fun Preview(
    @PreviewParameter(OverviewRaceProvider::class) overviewRace: OverviewRace
) {
    AppThemePreview {
        Column(Modifier.fillMaxWidth()) {
            Week(
                model = CalendarModel.Week(
                    season = 2022,
                    startOfWeek = LocalDate.of(2022, 9, 19),
                    race = null
                ),
                itemClicked = {}
            )
            Week(
                model = CalendarModel.Week(
                    season = 2022,
                    startOfWeek = LocalDate.of(2022, 9, 26),
                    race = null
                ),
                itemClicked = {})
            Week(
                model = CalendarModel.Week(
                    season = 2022,
                    startOfWeek = LocalDate.of(2022, 10, 3),
                    race = overviewRace
                ),
                itemClicked = {}
            )
        }
    }
}