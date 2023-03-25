package tmg.flashback.stats.ui.shared

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import tmg.flashback.formula1.model.Driver

@Composable
fun DriverImage(
    driver: Driver,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp
) {
    tmg.flashback.ui.components.drivers.DriverImage(
        photoUrl = driver.photoUrl,
        number = driver.number,
        code = driver.code,
        modifier = modifier,
        size = size
    )
}