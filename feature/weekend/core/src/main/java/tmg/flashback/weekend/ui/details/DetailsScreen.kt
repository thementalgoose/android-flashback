package tmg.flashback.weekend.ui.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import tmg.flashback.formula1.enums.TrackLayout
import tmg.flashback.formula1.model.Race
import tmg.flashback.formula1.model.Schedule
import tmg.flashback.formula1.model.ScheduleWeather
import tmg.flashback.formula1.model.WeatherType
import tmg.flashback.providers.RaceProvider
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.badge.Badge
import tmg.flashback.style.badge.BadgeView
import tmg.flashback.style.buttons.ButtonSecondary
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextBody2
import tmg.flashback.style.text.TextTitle
import tmg.flashback.ui.components.navigation.appBarHeight
import tmg.flashback.weekend.R
import tmg.flashback.weekend.contract.model.ScreenWeekendData
import tmg.flashback.weekend.ui.toWeekendInfo
import tmg.utilities.extensions.format
import tmg.utilities.extensions.keySet
import tmg.utilities.extensions.ordinalAbbreviation
import kotlin.math.roundToInt

private val trackSize: Dp = 200.dp

internal fun LazyListScope.details(
    weekendInfo: ScreenWeekendData,
    items: List<DetailsModel>,
    linkClicked: (DetailsModel.Link) -> Unit
) {
    items(items, key = { it.id }) {
        when (it) {
            is DetailsModel.Links -> {
                Links(it, linkClicked)
            }
            is DetailsModel.Label -> {
                DetailsLabel(it)
            }
            is DetailsModel.ScheduleWeekend -> {
                Weekend(it)
            }
            is DetailsModel.Track -> {
                Track(it)
            }
        }
    }
    item(key = "footer") {
        Spacer(Modifier.height(appBarHeight))
    }
}

@Composable
private fun CornerLink(
    model: DetailsModel.Links,
    linkClicked: (DetailsModel.Link) -> Unit,
) {
    model.links.forEach { link ->
        IconButton(onClick = { linkClicked(link) }) {
            Icon(
                painter = painterResource(id = link.icon),
                contentDescription = null,
                tint = AppTheme.colors.contentSecondary
            )
        }
    }
}

@Composable
private fun Track(
    model: DetailsModel.Track
) {
    val track = TrackLayout.getTrack(model.circuit.id, model.season, model.raceName)
    Column(Modifier.padding(
        start = AppTheme.dimens.medium,
        end = AppTheme.dimens.medium,
        bottom = AppTheme.dimens.xsmall,
    )) {
        model.laps?.toIntOrNull()?.let { laps ->
            BadgeView(
                model = Badge(stringResource(id = R.string.weekend_info_laps, laps))
            )
        }
        Icon(
            tint = AppTheme.colors.contentPrimary,
            modifier = Modifier.size(trackSize),
            painter = painterResource(id = track?.icon ?: R.drawable.circuit_unknown),
            contentDescription = null
        )
    }
}

@Composable
private fun Links(
    model: DetailsModel.Links,
    linkClicked: (DetailsModel.Link) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier
        .horizontalScroll(rememberScrollState())
        .padding(horizontal = AppTheme.dimens.medium)
    ) {
        model.links.forEach { link ->
            ButtonSecondary(
                text = stringResource(id = link.label),
                onClick = { linkClicked(link) },
//                icon = link.icon
            )
            Spacer(Modifier.width(AppTheme.dimens.medium))
        }
    }
}

@Composable
private fun DetailsLabel(
    model: DetailsModel.Label,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(
                vertical = AppTheme.dimens.small,
                horizontal = AppTheme.dimens.medium
            )
    ) {
        Icon(
            painter = painterResource(id = model.icon),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = AppTheme.colors.contentSecondary
        )
        Spacer(Modifier.width(AppTheme.dimens.small))
        TextBody1(
            bold = true,
            modifier = Modifier.weight(1f),
            text = model.label.resolve(LocalContext.current)
        )
    }
}

@Composable
private fun Weekend(
    model: DetailsModel.ScheduleWeekend,
    modifier: Modifier = Modifier
) {

    var targetIndex = model.days.keySet()
        .indexOfFirst { it == LocalDate.now() }
    if (targetIndex == -1) targetIndex = model.days.size - 1
    val scrollState = rememberScrollState(
        initial = targetIndex.coerceIn(0, model.days.size - 1)
    )

    Row(modifier = modifier.horizontalScroll(scrollState)) {
        model.days.forEach { (date, list) ->
            Column(modifier = modifier
                .fillMaxWidth()
                .padding(
                    top = AppTheme.dimens.xsmall,
                    start = AppTheme.dimens.medium,
                    end = AppTheme.dimens.medium
                )
            ) {
                Title(date)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(AppTheme.dimens.medium)
                ) {
                    list.forEach { (schedule, isNotificationSet) ->
                        EventItem(item = schedule, showNotificationBell = isNotificationSet)
                    }
                }
            }
        }
    }
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
            .padding(
                top = AppTheme.dimens.small,
                bottom = AppTheme.dimens.small
            )
    )
}

@Composable
private fun EventItem(
    item: Schedule,
    showNotificationBell: Boolean,
    modifier: Modifier = Modifier
) {
    val timestamp = item.timestamp.deviceLocalDateTime.format("HH:mm")
    Column(modifier = modifier
        .padding(vertical = AppTheme.dimens.xsmall)
    ) {
        val contentDescription = when (showNotificationBell) {
            true -> stringResource(id = R.string.ab_schedule_date_card_notifications_enabled, item.label, timestamp)
            false -> stringResource(id = R.string.ab_schedule_date_card, item.label, timestamp)
        }
        Column(
            modifier = Modifier
                .semantics(mergeDescendants = true) { }
                .clearAndSetSemantics {
                    this.contentDescription = contentDescription
                }
                .clip(RoundedCornerShape(AppTheme.dimens.radiusSmall))
                .background(AppTheme.colors.backgroundSecondary)
                .padding(
                    vertical = AppTheme.dimens.small,
                    horizontal = AppTheme.dimens.nsmall
                )
        ) {
            Row {
                TextBody1(
                    text = item.label
                )
                if (showNotificationBell) {
                    Spacer(Modifier.width(AppTheme.dimens.small))
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
            }
            TextTitle(
                modifier = Modifier.padding(top = AppTheme.dimens.small),
                text = timestamp.format("HH:mm"),
                bold = true
            )
        }

        Spacer(Modifier.height(AppTheme.dimens.xsmall))
        item.weather?.let { weather ->
            Column(
                modifier = Modifier.padding(
                    horizontal = AppTheme.dimens.xsmall
                ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = item.weather!!.summary.first().icon),
                    contentDescription = null,
                    modifier = Modifier.size(36.dp)
                )

                // Rain Percentage
                val rainPercent = (weather.rainPercent * 100).roundToInt()
                Row {
                    Image(
                        painter = painterResource(id = R.drawable.weather_indicator_rain),
                        contentDescription = null
                    )
                    TextBody2(
                        textColor = AppTheme.colors.contentTertiary,
                        modifier = Modifier.align(Alignment.CenterVertically),
                        text = "$rainPercent%"
                    )
                }
            }
        }
    }
}

@PreviewTheme
@Composable
private fun Preview(
    @PreviewParameter(RaceProvider::class) race: Race
) {
    AppThemePreview {
        LazyColumn(content = {
            details(
                weekendInfo = race.raceInfo.toWeekendInfo(),
                items = listOf(
                    DetailsModel.Links(listOf(
                        DetailsModel.Link(
                            label = R.string.details_link_youtube,
                            icon = R.drawable.ic_details_youtube,
                            url = "https://www.youtube.com"
                        ),
                        DetailsModel.Link(
                            label = R.string.details_link_wikipedia,
                            icon = R.drawable.ic_details_wikipedia,
                            url = "https://www.wiki.com"
                        )
                    )),
                    DetailsModel.ScheduleWeekend(
                        days = listOf(
                            LocalDate.of(2020, 1, 1) to listOf(
                                Schedule("FP1", LocalDate.now(), LocalTime.of(9, 0), weather = weather) to true,
                                Schedule("FP2", LocalDate.now(), LocalTime.of(11, 0), weather = weather) to false
                            ),
                            LocalDate.of(2020, 1, 2) to listOf(
                                Schedule("FP3", LocalDate.now(), LocalTime.of(9, 0), weather = weather) to true,
                                Schedule("Qualifying", LocalDate.now(), LocalTime.of(12, 0), weather = weather) to true
                            )
                        )
                    ),
                    DetailsModel.Track(
                        circuit = race.raceInfo.circuit,
                        raceName = race.raceInfo.name,
                        season = race.raceInfo.season,
                        laps = race.raceInfo.laps
                    )
                ),
                linkClicked = { }
            )
        })
    }
}

private val weather = ScheduleWeather(
    rainPercent = 0.5,
    windMs = 3.0,
    windBearing = 30,
    tempMaxC = 10.0,
    tempMinC = 20.0,
    summary = listOf(WeatherType.CLOUDS_LIGHT)
)