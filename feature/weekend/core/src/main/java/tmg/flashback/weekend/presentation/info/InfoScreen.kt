package tmg.flashback.weekend.presentation.info

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
import tmg.flashback.formula1.model.Race
import tmg.flashback.providers.RaceProvider
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextBody2
import tmg.flashback.style.text.TextHeadline1
import tmg.flashback.ui.components.flag.Flag
import tmg.flashback.weekend.R
import tmg.flashback.weekend.contract.model.ScreenWeekendData
import tmg.flashback.weekend.presentation.toWeekendInfo
import tmg.utilities.extensions.format
import tmg.utilities.extensions.ordinalAbbreviation

private val trackSizeLarge = 180.dp
private val trackSizeSmall = 80.dp

@Composable
fun RaceInfoHeader(
    model: ScreenWeekendData,
    modifier: Modifier = Modifier,
    showBack: Boolean = true,
    actionUpClicked: () -> Unit = { },
    icons: @Composable RowScope.() -> Unit = { }
) {
    Column(modifier = modifier.padding(
        bottom = AppTheme.dimens.small
    )) {
        if (showBack) {
            Row {
                IconButton(
                    onClick = actionUpClicked
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = stringResource(id = R.string.ab_back),
                        tint = AppTheme.colors.contentPrimary
                    )
                }
                Spacer(Modifier.weight(1f))
                icons()
            }
        }
        Column(modifier = Modifier.padding(
            start = AppTheme.dimens.medium,
            end = AppTheme.dimens.medium,
            top = AppTheme.dimens.medium
        )) {
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
    model: ScreenWeekendData,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Column(modifier = Modifier.weight(1f)) {
            TextBody1(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = AppTheme.dimens.xsmall,
                        bottom = AppTheme.dimens.xsmall
                    ),
                text = model.circuitName
            )
            TextBody1(
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
                modifier = Modifier.size(48.dp),
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
        RaceInfoHeader(
            model = race.raceInfo.toWeekendInfo()
        )
    }
}

@PreviewTheme
@Composable
private fun PreviewExpanded(
    @PreviewParameter(RaceProvider::class) race: Race
) {
    AppThemePreview {
        RaceInfoHeader(
            showBack = false,
            model = race.raceInfo.toWeekendInfo()
        )
    }
}