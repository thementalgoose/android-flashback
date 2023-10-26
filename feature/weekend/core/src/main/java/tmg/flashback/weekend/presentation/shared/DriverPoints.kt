package tmg.flashback.weekend.presentation.shared

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import tmg.flashback.formula1.extensions.pointsDisplay
import tmg.flashback.formula1.model.Driver
import tmg.flashback.providers.DriverProvider
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.text.TextBody2
import tmg.flashback.style.text.TextCaption
import tmg.flashback.ui.components.flag.Flag
import tmg.flashback.ui.utils.pluralResource
import tmg.flashback.weekend.R
import kotlin.math.roundToInt

@Deprecated(
    message = "Use copy of this in presentation module",
    replaceWith = ReplaceWith("""
DriverPoints(
    name = driver.name,
    nationality = driver.nationality,
    nationalityISO = driver.nationalityISO,
    points = points,
)
    """, "tmg.flashback.ui.components.drivers.DriverPoints"))
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
        Column(modifier = Modifier
            .padding(
                vertical = AppTheme.dimens.xxsmall
            )
        ) {
            Flag(
                iso = driver.nationalityISO,
                nationality = driver.nationality,
                modifier = Modifier.size(16.dp)
            )
        }
        TextBody2(
            text = driver.name,
            modifier = Modifier.padding(horizontal = AppTheme.dimens.xsmall)
        )
        val points = pluralResource(R.plurals.race_points, points.takeIf { !it.isNaN() }?.roundToInt() ?: 0, points.pointsDisplay())
        TextCaption(text = "- $points")
    }
}

@PreviewTheme
@Composable
private fun Preview(
    @PreviewParameter(DriverProvider::class) driver: Driver
) {
    AppThemePreview {
        DriverPoints(driver = driver, points = 3.0)
    }
}