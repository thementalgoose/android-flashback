package tmg.flashback.stats.ui.dashboard.drivers

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import tmg.flashback.formula1.extensions.pointsDisplay
import tmg.flashback.formula1.model.SeasonDriverStandingSeason
import tmg.flashback.formula1.model.SeasonDriverStandings
import tmg.flashback.formula1.utils.getFlagResourceAlpha3
import tmg.flashback.providers.SeasonDriverStandingSeasonProvider
import tmg.flashback.providers.SeasonDriverStandingsProvider
import tmg.flashback.style.AppTheme
import tmg.flashback.style.text.TextBody2
import tmg.flashback.style.text.TextTitle
import tmg.flashback.stats.R
import tmg.flashback.style.AppThemePreview
import tmg.flashback.ui.components.progressbar.ProgressBar
import tmg.flashback.ui.utils.isInPreview
import kotlin.math.roundToInt

@Composable
fun DriverStandingsScreen() {

}

@Composable
fun DriverStandings(
    model: SeasonDriverStandingSeason,
    itemClicked: (SeasonDriverStandingSeason) -> Unit,
    maxPoints: Double,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .clickable(onClick = {
                itemClicked(model)
            }),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TextTitle(
            text = model.championshipPosition?.toString() ?: "-",
            bold = true,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(42.dp)
        )
        Box(modifier = Modifier
            .size(36.dp)
            .clip(RoundedCornerShape(AppTheme.dimensions.radiusSmall))
            .background(AppTheme.colors.backgroundSecondary)
        ) {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                model = model.driver.photoUrl ?: R.drawable.unknown_avatar,
                contentDescription = null
            )
        }
        Row(modifier = Modifier
            .padding(
                top = AppTheme.dimensions.paddingSmall,
                start = AppTheme.dimensions.paddingSmall,
                end = AppTheme.dimensions.paddingMedium,
                bottom = AppTheme.dimensions.paddingSmall
            )
            .wrapContentHeight()
        ) {
            Column(modifier = Modifier.weight(2f)) {
                TextTitle(
                    text = model.driver.name,
                    bold = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(modifier = Modifier
                    .fillMaxHeight()
                    .padding(
                        vertical = AppTheme.dimensions.paddingXXSmall,
                    )
                ) {
                    val resourceId = when (isInPreview()) {
                        true -> R.drawable.gb
                        false -> LocalContext.current.getFlagResourceAlpha3(model.driver.nationalityISO)
                    }
                    Image(
                        modifier = Modifier.size(16.dp),
                        painter = painterResource(id = resourceId),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                    )
                    Spacer(Modifier.width(4.dp))
                    TextBody2(
                        text = model.constructors.joinToString { it.constructor.name },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            Spacer(Modifier.width(AppTheme.dimensions.paddingSmall))
            val progress = (model.points / maxPoints).toFloat().coerceIn(0f, 1f)
            ProgressBar(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxHeight(),
                endProgress = progress,
                barColor = model.constructors.lastOrNull()?.constructor?.colour ?: AppTheme.colors.primary,
                label = {
                    when (it) {
                        0f -> "0"
                        progress -> model.points.pointsDisplay()
                        else -> (it * maxPoints).roundToInt().toString()
                    }
                }
            )
        }
    }
}

@Preview
@Composable
private fun PreviewLight(
    @PreviewParameter(SeasonDriverStandingSeasonProvider::class) driverStandings: SeasonDriverStandingSeason
) {
    AppThemePreview(isLight = true) {
        DriverStandings(
            model = driverStandings,
            itemClicked = {},
            maxPoints = 25.0)
    }
}

@Preview
@Composable
private fun PreviewDark(
    @PreviewParameter(SeasonDriverStandingSeasonProvider::class) driverStandings: SeasonDriverStandingSeason
) {
    AppThemePreview(isLight = false) {
        DriverStandings(
            model = driverStandings,
            itemClicked = {},
            maxPoints = 25.0)
    }
}