package tmg.flashback.stats.ui.weekend.info

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import tmg.flashback.formula1.enums.TrackLayout
import tmg.flashback.formula1.model.Race
import tmg.flashback.providers.RaceProvider
import tmg.flashback.stats.R
import tmg.flashback.ui.components.flag.Flag
import tmg.flashback.stats.ui.weekend.from
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextBody2
import tmg.flashback.style.text.TextHeadline1
import tmg.flashback.weekend.contract.model.WeekendInfo
import tmg.utilities.extensions.format
import tmg.utilities.extensions.ordinalAbbreviation

private val trackSizeLarge = 180.dp
private val trackSizeSmall = 80.dp

@Composable
fun RaceInfoHeader(
    model: WeekendInfo,
    modifier: Modifier = Modifier,
    actionUpClicked: () -> Unit = { },
    largeTrack: Boolean = false,
) {
    Column(modifier = modifier.padding(
        bottom = AppTheme.dimens.small
    )) {
        IconButton(
            onClick = actionUpClicked
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = stringResource(id = R.string.ab_back),
                tint = AppTheme.colors.contentPrimary
            )
        }
        Column(modifier = Modifier.padding(
            horizontal = AppTheme.dimens.medium
        )) {
            val track = TrackLayout.getTrack(model.circuitId, model.season, model.raceName)
            Icon(
                tint = AppTheme.colors.contentPrimary,
                modifier = Modifier.size(if (largeTrack) trackSizeLarge else trackSizeSmall),
                painter = painterResource(id = track?.icon ?: R.drawable.circuit_unknown),
                contentDescription = null
            )
            TextHeadline1(
                modifier = Modifier.padding(vertical = 2.dp),
                text = "${model.season} ${model.raceName}"
            )
            RaceDetails(model = model)
        }
    }
}

@Composable
private fun RaceDetails(
    model: WeekendInfo,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Column(modifier = Modifier.weight(1f)) {
            TextBody1(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = AppTheme.dimens.xsmall),
                text = model.circuitName
            )
            TextBody2(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = AppTheme.dimens.xsmall),
                text = model.country
            )
            TextBody2(
                bold = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = AppTheme.dimens.xsmall),
                text = model.date.format("'${model.date.dayOfMonth.ordinalAbbreviation}' MMMM yyyy") ?: ""
            )
        }
        Column(
            horizontalAlignment = Alignment.End
        ) {
            Flag(
                iso = model.countryISO,
                nationality = model.country,
                modifier = Modifier.size(42.dp),
            )
            TextBody2(
                text = stringResource(id = R.string.weekend_race_round, model.round),
                bold = true,
                modifier = Modifier.padding(vertical = 2.dp)
            )
        }
    }
}

@PreviewTheme
@Composable
private fun PreviewCompact(
    @PreviewParameter(RaceProvider::class) race: Race
) {
    AppThemePreview {
        RaceInfoHeader(model = WeekendInfo.from(race.raceInfo), largeTrack = true)
    }
}

@PreviewTheme
@Composable
private fun PreviewExpanded(
    @PreviewParameter(RaceProvider::class) race: Race
) {
    AppThemePreview {
        RaceInfoHeader(model = WeekendInfo.from(race.raceInfo), largeTrack = false)
    }
}