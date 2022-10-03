package tmg.flashback.stats.ui.dashboard.schedule.schedule

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.threeten.bp.LocalDate
import tmg.flashback.formula1.model.OverviewRace
import tmg.flashback.formula1.model.Schedule
import tmg.flashback.providers.OverviewRaceProvider
import tmg.flashback.stats.R
import tmg.flashback.stats.repository.models.NotificationSchedule
import tmg.flashback.stats.ui.dashboard.schedule.ScheduleModel
import tmg.flashback.stats.ui.shared.Flag
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextBody2
import tmg.flashback.style.text.TextTitle
import tmg.flashback.ui.components.layouts.Container
import tmg.utilities.extensions.format
import tmg.utilities.extensions.ordinalAbbreviation

private val countryBadgeSize = 32.dp
private const val listAlpha = 0.6f
private const val pastScheduleAlpha = 0.2f

//region Schedule view

@Composable
internal fun Schedule(
    model: ScheduleModel.List,
    itemClicked: (ScheduleModel.List) -> Unit,
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
                        end = AppTheme.dimens.nsmall,
                        start = AppTheme.dimens.nsmall
                    )
                ) {
                    Flag(
                        iso = model.model.countryISO,
                        nationality = model.model.country,
                        modifier = Modifier.size(countryBadgeSize),
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
                        TextTitle(
                            text = "#${model.model.round}",
                            bold = true
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
                            showQualifying = model.model.hasQualifying && model.model.season > 2000,
                            showSprint = model.model.hasSprint && model.model.season > 2020,
                            showRace = model.model.hasResults
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
    showQualifying: Boolean,
    showSprint: Boolean,
    showRace: Boolean,
    iconSize: Dp = 16.dp
) {
    Icon(
        modifier = Modifier
            .size(iconSize)
            .align(Alignment.CenterVertically),
        painter = painterResource(id = R.drawable.ic_status_results_qualifying),
        contentDescription = stringResource(id = R.string.ab_has_qualifying_results),
        tint = if (showQualifying) AppTheme.colors.f1ResultsFull else AppTheme.colors.backgroundTertiary
    )
    Spacer(Modifier.width(2.dp))
    if (showSprint) {
        Icon(
            modifier = Modifier
                .size(iconSize)
                .align(Alignment.CenterVertically),
            painter = painterResource(id = R.drawable.ic_status_results_sprint),
            contentDescription = stringResource(id = R.string.ab_has_sprint_results),
            tint = AppTheme.colors.f1ResultsFull
        )
        Spacer(Modifier.width(2.dp))
    }
    Icon(
        modifier = Modifier
            .size(iconSize)
            .align(Alignment.CenterVertically),
        painter = painterResource(id = R.drawable.ic_status_results_race),
        contentDescription = stringResource(id = R.string.ab_has_race_results),
        tint = if (showRace) AppTheme.colors.f1ResultsFull else AppTheme.colors.backgroundTertiary
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

    LazyRow(
        modifier = modifier.padding(bottom = AppTheme.dimens.small),
        state = scrollState,
        content = {
            item {
                Spacer(Modifier.width(countryBadgeSize + (AppTheme.dimens.nsmall * 2)))
            }
            for ((date, list) in schedule) {
                val alpha = if (date.isBefore(LocalDate.now()) || list.all { it.timestamp.isInPast }) pastScheduleAlpha else 1f
                item {
                    Column {
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
private fun DateCard(
    schedule: Schedule,
    showNotificationBadge: Boolean,
    modifier: Modifier = Modifier
) {
    val alpha = if (schedule.timestamp.isInPast) pastScheduleAlpha else 1f
    Column(modifier = modifier
        .width(IntrinsicSize.Max)
        .clip(RoundedCornerShape(AppTheme.dimens.radiusSmall))
        .background(AppTheme.colors.backgroundTertiary)
        .alpha(alpha)
        .padding(
            vertical = AppTheme.dimens.nsmall,
            horizontal = AppTheme.dimens.nsmall
        )
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = AppTheme.dimens.small),
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
                    painter = painterResource(id = R.drawable.ic_notification_indicator_bell),
                    contentDescription = stringResource(id = R.string.ab_notifications_enabled)
                )
            }
        }
        TextBody2(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = schedule.timestamp.deviceLocalDateTime.toLocalTime().format("HH:mm"),
            bold = true
        )
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
            Schedule(
                model = ScheduleModel.List(
                    model = overviewRace,
                    showScheduleList = false,
                    notificationSchedule = fakeNotificationSchedule,
                    id = "1"
                ),
                itemClicked = {}
            )
            Schedule(
                model = ScheduleModel.List(
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

private val fakeNotificationSchedule = NotificationSchedule(
    freePractice = false,
    qualifying = true,
    sprint = true,
    race = true,
    other = true
)