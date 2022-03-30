package tmg.flashback.statistics.ui.weekend

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.viewModel
import tmg.flashback.formula1.model.Race
import tmg.flashback.formula1.model.RaceInfo
import tmg.flashback.formula1.utils.getFlagResourceAlpha3
import tmg.flashback.providers.RaceProvider
import tmg.flashback.statistics.R
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.text.TextBody2
import tmg.flashback.style.text.TextHeadline1
import tmg.flashback.ui.utils.isInPreview
import tmg.utilities.extensions.format
import tmg.utilities.extensions.ordinalAbbreviation

@Composable
fun WeekendScreen() {
    val viewModel by viewModel<WeekendViewModel>()

    WeekendScreenImpl()
}

@Composable
private fun WeekendScreenImpl() {
//    RaceDetails(
//
//    )
}

@Composable
private fun RaceHeader(
    model: RaceInfo,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier
        .padding(horizontal = AppTheme.dimensions.paddingMedium)
    ) {
        Image(
            modifier = Modifier.size(100.dp),
            painter = painterResource(id = R.drawable.circuit_bahrain), contentDescription = null
        )
        TextHeadline1(
            modifier = Modifier.padding(vertical = 2.dp),
            text = model.name
        )
        RaceDetails(model = model)
    }
}

@Composable
private fun RaceDetails(
    model: RaceInfo,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Column(modifier = Modifier.weight(1f)) {
            TextBody2(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = AppTheme.dimensions.paddingXSmall),
                text = model.circuit.name
            )
            TextBody2(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = AppTheme.dimensions.paddingXSmall),
                text = model.circuit.country
            )
            model.laps?.let {
                TextBody2(
                    modifier = Modifier.fillMaxWidth(),
                    bold = true,
                    text = stringResource(id = R.string.circuit_info_laps, it)
                )
            }
        }
        Column(
            horizontalAlignment = Alignment.End
        ) {
            val resourceId = when (isInPreview()) {
                true -> R.drawable.gb
                false -> LocalContext.current.getFlagResourceAlpha3(model.circuit.countryISO)
            }
            Image(
                modifier = Modifier.size(32.dp),
                painter = painterResource(id = resourceId),
                contentDescription = null,
                contentScale = ContentScale.Fit,
            )
            TextBody2(
                text = stringResource(id = R.string.race_round, model.round),
                bold = true,
                modifier = Modifier.padding(vertical = 2.dp)
            )
            TextBody2(text = model.date.format("'${model.date.dayOfMonth.ordinalAbbreviation}' MMMM") ?: "")
        }
    }
}

@Composable
private fun SwipeInfo() {

}

@Preview
@Composable
private fun Preview(
    @PreviewParameter(RaceProvider::class) race: Race
) {
    AppThemePreview(isLight = true) {
        RaceHeader(race.raceInfo)
    }
}