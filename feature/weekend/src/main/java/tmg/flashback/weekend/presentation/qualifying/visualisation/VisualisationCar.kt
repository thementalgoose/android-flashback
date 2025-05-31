package tmg.flashback.weekend.presentation.qualifying.visualisation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tmg.flashback.formula1.model.DriverEntry
import tmg.flashback.providers.DriverConstructorProvider
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.text.TextBody2
import tmg.flashback.style.text.TextBodyAutosize
import tmg.flashback.weekend.R

@Composable
fun VisualisationCar(
    entry: DriverEntry,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Box {
            Icon(
                modifier = Modifier.size(width = 40.dp, height = 18.dp),
                painter = painterResource(R.drawable.ic_qualifying_car),
                contentDescription = null
            )
            Icon(
                modifier = Modifier.size(width = 40.dp, height = 18.dp),
                painter = painterResource(R.drawable.ic_qualifying_car_overlay),
                tint = entry.constructor.colour,
                contentDescription = null
            )
        }
        val label = (entry.driver.code ?: entry.driver.lastName.take(3)).uppercase()
        TextBodyAutosize(
            maxLines = 1,
            maxTextSize = 18.sp,
            text = label
        )
    }
}

@PreviewTheme
@Composable
private fun Preview(
    @PreviewParameter(DriverConstructorProvider::class) driverEntry: DriverEntry
) {
    AppThemePreview {
        VisualisationCar(
            modifier = Modifier.width(85.dp).height(24.dp),
            entry = driverEntry
        )
    }
}
