package tmg.flashback.stats.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import tmg.flashback.formula1.extensions.pointsDisplay
import tmg.flashback.formula1.model.Driver
import tmg.flashback.formula1.utils.getFlagResourceAlpha3
import tmg.flashback.providers.DriverProvider
import tmg.flashback.style.AppTheme
import tmg.flashback.stats.R
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.text.TextBody2
import tmg.flashback.ui.utils.isInPreview
import tmg.flashback.ui.utils.pluralResource
import kotlin.math.roundToInt

@Composable
fun DriverPoints(
    driver: Driver,
    points: Double,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val resourceId = when (isInPreview()) {
            true -> R.drawable.gb
            false -> LocalContext.current.getFlagResourceAlpha3(driver.nationalityISO)
        }

        TextBody2(text = driver.name)
        Column(modifier = Modifier
            .fillMaxHeight()
            .padding(
                vertical = AppTheme.dimensions.paddingXXSmall,
                horizontal = AppTheme.dimensions.paddingXSmall
            )
        ) {
            Image(
                modifier = Modifier.size(16.dp),
                painter = painterResource(id = resourceId),
                contentDescription = null,
                contentScale = ContentScale.Fit,
            )
        }
        TextBody2(text = pluralResource(R.plurals.race_points, points.roundToInt(), points.pointsDisplay()))
    }
}

@Preview
@Composable
private fun PreviewLight(
    @PreviewParameter(DriverProvider::class) driver: Driver
) {
    AppThemePreview(isLight = true) {
        DriverPoints(driver = driver, points = 3.0)
    }
}

@Preview
@Composable
private fun PreviewDark(
    @PreviewParameter(DriverProvider::class) driver: Driver
) {
    AppThemePreview(isLight = false) {
        DriverPoints(driver = driver, points = 3.0)
    }
}