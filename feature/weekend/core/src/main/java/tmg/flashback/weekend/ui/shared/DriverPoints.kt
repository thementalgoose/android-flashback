package tmg.flashback.weekend.ui.shared

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import tmg.flashback.formula1.extensions.pointsDisplay
import tmg.flashback.formula1.model.Driver
import tmg.flashback.providers.DriverProvider
import tmg.flashback.ui.components.flag.Flag
import tmg.flashback.style.AppTheme
import tmg.flashback.weekend.R
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.text.TextBody2
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
        TextBody2(text = driver.name)
        Column(modifier = Modifier
            .padding(
                vertical = AppTheme.dimens.xxsmall,
                horizontal = AppTheme.dimens.xsmall
            )
        ) {
            Flag(
                iso = driver.nationalityISO,
                nationality = driver.nationality,
                modifier = Modifier.size(16.dp)
            )
        }
        TextBody2(text = pluralResource(R.plurals.race_points, points.takeIf { !it.isNaN() }?.roundToInt() ?: 0, points.pointsDisplay()))
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