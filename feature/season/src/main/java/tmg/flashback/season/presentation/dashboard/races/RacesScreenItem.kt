package tmg.flashback.season.presentation.dashboard.races

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.CollectionInfo
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.collectionInfo
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import tmg.flashback.formula1.constants.Formula1.qualifyingDataAvailableFrom
import tmg.flashback.formula1.constants.Formula1.sprintsIntroducedIn
import tmg.flashback.formula1.model.OverviewRace
import tmg.flashback.formula1.model.Schedule
import tmg.flashback.providers.OverviewRaceProvider
import tmg.flashback.formula1.model.notifications.NotificationSchedule
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextBody2
import tmg.flashback.style.text.TextSection
import tmg.flashback.style.text.TextTitle
import tmg.flashback.ui.components.flag.Flag
import tmg.flashback.ui.components.layouts.Container
import tmg.flashback.ui.components.now.Now
import tmg.utilities.extensions.format
import tmg.utilities.extensions.ordinalAbbreviation
import tmg.utilities.extensions.startOfWeek
import tmg.flashback.season.R
import tmg.flashback.formula1.R.drawable
import tmg.flashback.strings.R.string

private val countryBadgeSize = 32.dp
private const val listAlpha = 0.6f
private const val pastScheduleAlpha = 0.2f
private val weatherIconSize = 42.dp

//region Schedule view

@Composable
internal fun Races(
    model: RacesModel.RaceWeek,
    itemClicked: (RacesModel.RaceWeek) -> Unit,
    modifier: Modifier = Modifier,
    card: Boolean = false
) {
    val alpha = when (model.fadeItem) {
        true -> listAlpha
        false -> 1f
    }
    Container(
        modifier = modifier
            .padding(
                top = if (model.shouldShowScheduleList) AppTheme.dimens.xsmall else 0.dp,
                bottom = if (model.shouldShowScheduleList) AppTheme.dimens.small else 0.dp
            )
            .alpha(alpha)
            .clickable(onClick = {
                itemClicked(model)
            }),
        isOutlined = model.shouldShowScheduleList
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
        ) {
            Row {
                Box(modifier = Modifier
                    .padding(
                        top = AppTheme.dimens.medium,
                        end = AppTheme.dimens.nsmall
                    )
                ) {
                    if (model.model.date.startOfWeek() == LocalDate.now().startOfWeek()) {
                        Now(Modifier.align(Alignment.CenterStart))
                    }
                    Flag(
                        iso = model.model.countryISO,
                        nationality = null,
                        modifier = Modifier
                            .padding(start = AppTheme.dimens.medium)
                            .size(countryBadgeSize),
                    )
                }
                Column(modifier = Modifier
                    .weight(1f)
                    .padding(
                        top = AppTheme.dimens.small,
                        bottom = AppTheme.dimens.small,
                        end = AppTheme.dimens.medium
                    )
                ) {
                    Row {
                        TextTitle(
                            text = model.model.raceName,
                            bold = true,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(Modifier.width(AppTheme.dimens.small))
                        Round(
                            round = model.model.round
                        )
                    }
                    Row {
                        TextBody1(
                            text = model.model.circuitName,
                            modifier = Modifier
                                .weight(1f)
                                .padding(top = 2.dp)
                        )
                        Spacer(Modifier.width(AppTheme.dimens.small))
                        IconRow(
                            hasQualifying = model.model.hasQualifying && model.model.season >= qualifyingDataAvailableFrom,
                            showSprint = (model.containsSprintEvent || model.model.hasSprint) && model.model.season > sprintsIntroducedIn,
                            hasSprint = model.model.hasSprint && model.model.season > sprintsIntroducedIn,
                            hasRace = model.model.hasResults
                        )
                    }

                    if (!model.shouldShowScheduleList) {
                        TextBody2(
                            text = model.model.date.format("'${model.model.date.dayOfMonth.ordinalAbbreviation}' MMMM") ?: "",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 2.dp)
                        )
                    }
                }
            }
            if (model.shouldShowScheduleList) {
                Dates(
                    scheduleList = model.model.schedule,
                    notificationSchedule = model.notificationSchedule,
                    modifier = Modifier.padding(top = AppTheme.dimens.xsmall)
                )
            }
        }
    }
}

@Composable
private fun RowScope.IconRow(
    hasQualifying: Boolean,
    showSprint: Boolean,
    hasSprint: Boolean,
    hasRace: Boolean,
    iconSize: Dp = 16.dp
) {
    Icon(
        modifier = Modifier
            .size(iconSize)
            .align(Alignment.CenterVertically),
        painter = painterResource(id = drawable.ic_status_results_qualifying),
        contentDescription = when (hasQualifying) {
            true -> stringResource(id = string.ab_has_qualifying_results)
            false -> stringResource(id = string.ab_no_qualifying_results)
        },
        tint = when (hasQualifying) {
            true -> AppTheme.colors.f1ResultsFull
            false -> AppTheme.colors.backgroundTertiary
        }
    )
    Spacer(Modifier.width(2.dp))
    if (showSprint) {
        Icon(
            modifier = Modifier
                .size(iconSize)
                .align(Alignment.CenterVertically),
            painter = painterResource(id = drawable.ic_status_results_sprint),
            contentDescription = when (hasSprint) {
                true -> stringResource(id = string.ab_has_sprint_results)
                false -> stringResource(id = string.ab_no_sprint_results)
            },
            tint = when (hasSprint) {
                true -> AppTheme.colors.f1ResultsFull
                false -> AppTheme.colors.backgroundTertiary
            }
        )
        Spacer(Modifier.width(2.dp))
    }
    Icon(
        modifier = Modifier
            .size(iconSize)
            .align(Alignment.CenterVertically),
        painter = painterResource(id = drawable.ic_status_results_race),
        contentDescription = when (hasRace) {
            true -> stringResource(id = string.ab_has_race_results)
            false -> stringResource(id = string.ab_no_race_results)
        },
        tint = when (hasRace) {
            true -> AppTheme.colors.f1ResultsFull
            false -> AppTheme.colors.backgroundTertiary
        }
    )
}

@Composable
private fun Dates(
    scheduleList: List<Schedule>,
    notificationSchedule: NotificationSchedule,
    modifier: Modifier = Modifier
) {
    val schedule = scheduleList.groupBy { it.timestamp.deviceLocalDateTime.toLocalDate() }
    var targetIndex = schedule
        .map { it.value.any { !it.timestamp.isInPast } }
        .indexOfFirst { it }
    if (targetIndex == -1) targetIndex = schedule.size - 1
    val scrollState = rememberLazyListState(
        initialFirstVisibleItemIndex = targetIndex.coerceIn(0, schedule.size - 1)
    )

    val showWeather = schedule.values.map { it }.flatten().all { it.weather != null }

    LazyRow(
        modifier = modifier.padding(bottom = AppTheme.dimens.small),
        state = scrollState,
        content = {
            item {
                Spacer(Modifier.width(countryBadgeSize + (AppTheme.dimens.nsmall + AppTheme.dimens.medium)))
            }
            for ((date, list) in schedule) {
                val alpha = if (date.isBefore(LocalDate.now()) || list.all { it.timestamp.isInPast }) pastScheduleAlpha else 1f
                item {
                    Column(Modifier.semantics {
                        this.collectionInfo = CollectionInfo(list.size, 1)
                    }) {
                        TextBody2(
                            text = date.format("EEE '${date.dayOfMonth.ordinalAbbreviation}' MMMM")
                                ?: "",
                            modifier = Modifier
                                .alpha(alpha)
                                .padding(
                                    bottom = AppTheme.dimens.xsmall
                                )
                        )
                        Row {
                            list.forEach {
                                DateCard(
                                    schedule = it,
                                    showWeather = showWeather,
                                    showNotificationBadge = notificationSchedule.getByLabel(it.label)
                                )
                                Spacer(Modifier.width(AppTheme.dimens.small))
                            }
                        }
                    }
                    Spacer(Modifier.width(AppTheme.dimens.medium))
                }
            }
        }
    )
}

@Composable
internal fun Round(
    round: Int
) {
    val contentDescription = stringResource(id = string.weekend_race_round, round)
    TextSection(
        modifier = Modifier.semantics {
            this.contentDescription = contentDescription
        },
        text = "#${round}"
    )
}

@Composable
private fun DateCard(
    schedule: Schedule,
    showNotificationBadge: Boolean,
    showWeather: Boolean,
    modifier: Modifier = Modifier
) {
    val alpha = if (schedule.timestamp.isInPast) pastScheduleAlpha else 1f

    val time = schedule.timestamp.deviceLocalDateTime.toLocalTime().format("HH:mm")
    val contentDescription = when (showNotificationBadge) {
        true -> stringResource(id = string.ab_schedule_date_card_notifications_enabled, schedule.label, time)
        false -> stringResource(id = string.ab_schedule_date_card, schedule.label, time)
    }
    Column(modifier = modifier
        .semantics(mergeDescendants = true) { }
        .clearAndSetSemantics { this.contentDescription = contentDescription }
        .width(IntrinsicSize.Max)
        .clip(RoundedCornerShape(AppTheme.dimens.radiusSmall))
        .background(AppTheme.colors.backgroundTertiary)
        .alpha(alpha)
        .padding(
            bottom = AppTheme.dimens.nsmall,
            start = AppTheme.dimens.nsmall,
            end = AppTheme.dimens.nsmall
        )
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = AppTheme.dimens.nsmall,
                bottom = AppTheme.dimens.small
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextBody1(
                textAlign = TextAlign.Center,
                text = schedule.label
            )
            if (showNotificationBadge) {
                Spacer(Modifier.width(4.dp))
                Icon(
                    modifier = Modifier.size(16.dp),
                    tint = AppTheme.colors.contentSecondary,
                    painter = painterResource(id = tmg.flashback.notifications.R.drawable.ic_notification_indicator_bell),
                    contentDescription = stringResource(id = string.ab_notifications_enabled)
                )
            }
        }
        TextBody1(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start,
            text = time,
            bold = true
        )
        if (showWeather && schedule.weather != null) {
            Spacer(Modifier.height(AppTheme.dimens.small))

            val summary = schedule.weather!!.summary.firstOrNull()
            Image(
                painter = painterResource(id = summary?.icon ?: drawable.weather_unknown),
                contentDescription = stringResource(id = summary?.label ?: string.empty),
                modifier = Modifier.size(weatherIconSize)
            )
        } else {
            Spacer(Modifier.height(AppTheme.dimens.nsmall))
        }
    }
}

//endregion

@PreviewTheme
@Composable
private fun Preview(
    @PreviewParameter(OverviewRaceProvider::class) overviewRace: OverviewRace
) {
    AppThemePreview {
        Column(Modifier.fillMaxWidth()) {
            Races(
                model = RacesModel.RaceWeek(
                    model = overviewRace,
                    showScheduleList = false,
                    notificationSchedule = fakeNotificationSchedule,
                    id = "1"
                ),
                itemClicked = {}
            )
            Races(
                model = RacesModel.RaceWeek(
                    model = overviewRace,
                    showScheduleList = true,
                    notificationSchedule = fakeNotificationSchedule,
                    id = "2"
                ),
                itemClicked = {}
            )
        }
    }
}

val fakeNotificationSchedule = NotificationSchedule(
    freePractice = false,
    qualifying = true,
    sprintQualifying = true,
    sprint = true,
    race = true,
    other = true
)