package tmg.flashback.results.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import tmg.flashback.formula1.extensions.pointsDisplay
import tmg.flashback.formula1.model.Driver
import tmg.flashback.providers.DriverProvider
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.text.TextBody2
import tmg.flashback.style.text.TextCaption
import tmg.flashback.ui.components.flag.Flag

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
        Flag(
            iso = driver.nationalityISO,
            nationality = driver.nationality,
            modifier = Modifier.size(16.dp)
        )
        TextBody2(
            text = driver.name,
            modifier = Modifier.padding(2.dp)
        )
        val pointsLabel = points.takeIf { !it.isNaN() }?.pointsDisplay() ?: ""
        TextCaption("- $pointsLabel")
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