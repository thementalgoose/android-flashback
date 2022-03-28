package tmg.flashback.statistics.ui.weekend

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.viewModel
import tmg.flashback.formula1.model.RaceInfo
import tmg.flashback.formula1.utils.getFlagResourceAlpha3
import tmg.flashback.statistics.R
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.text.TextBody2
import tmg.flashback.ui.utils.isInPreview

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
private fun RaceDetails(
    model: RaceInfo
) {
    Row {
        Column(modifier = Modifier.weight(1f)) {
            TextBody2(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = AppTheme.dimensions.paddingXSmall),
                text = model.name
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
                    text = stringResource(id = R.string.circuit_info_laps, it)
                )
            }
        }
        Column {
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
        }
    }
}



@Preview
@Composable
private fun Preview(
//    @PreviewParameter(RaceProvider::class) race: Race
) {
    AppThemePreview(isLight = true) {
//        RaceDetails(race.raceInfo)
    }
}