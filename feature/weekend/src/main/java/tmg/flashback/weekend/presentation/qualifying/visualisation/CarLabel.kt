package tmg.flashback.weekend.presentation.qualifying.visualisation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tmg.flashback.formula1.model.DriverEntry
import tmg.flashback.providers.DriverConstructorProvider
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.weekend.R

@Composable
internal fun CarLabel(
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
                contentDescription = null,
                tint = AppTheme.colors.backgroundTertiary
            )
            Icon(
                modifier = Modifier.size(width = 40.dp, height = 18.dp),
                painter = painterResource(R.drawable.ic_qualifying_car_overlay),
                tint = entry.constructor.colour,
                contentDescription = null
            )
        }
        val label = (entry.driver.code ?: entry.driver.lastName.take(3)).uppercase()
        DriverCode(
            text = label
        )
    }
}

@Composable
private fun RowScope.DriverCode(
    text: String,
    modifier: Modifier = Modifier
) {
    BasicText(
        text = text,
        modifier = modifier,
        maxLines = 1,
        style = AppTheme.typography.block.copy(
            fontWeight = FontWeight.Bold,
            color = AppTheme.colors.contentSecondary
        ),
        autoSize = TextAutoSize.StepBased(minFontSize = 6.sp, maxFontSize = 16.sp, stepSize = 2.sp)
    )
}

@PreviewTheme
@Composable
private fun Preview(
    @PreviewParameter(DriverConstructorProvider::class) driverEntry: DriverEntry
) {
    AppThemePreview {
        CarLabel(
            modifier = Modifier.width(85.dp).height(24.dp),
            entry = driverEntry
        )
    }
}
