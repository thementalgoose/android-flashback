package tmg.flashback.ui.components.drivers

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.text.TextBody2
import tmg.flashback.style.text.TextCaption
import tmg.flashback.ui.R
import tmg.flashback.ui.components.flag.Flag
import tmg.flashback.ui.utils.pluralResource
import tmg.flashback.ui.utils.pointsDisplay
import kotlin.math.roundToInt

@Composable
fun DriverPoints(
    name: String,
    nationality: String,
    nationalityISO: String,
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
                iso = nationalityISO,
                nationality = nationality,
                modifier = Modifier.size(16.dp)
            )
        }
        TextBody2(
            text = name,
            modifier = Modifier.padding(horizontal = AppTheme.dimens.xsmall)
        )
        val points = pluralResource(R.plurals.race_points, points.takeIf { !it.isNaN() }?.roundToInt() ?: 0, points.pointsDisplay())
        TextCaption(text = "- $points")
    }
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        DriverPoints(
            name = "firstName lastName",
            nationality = "",
            nationalityISO = "",
            points = 3.0
        )
    }
}