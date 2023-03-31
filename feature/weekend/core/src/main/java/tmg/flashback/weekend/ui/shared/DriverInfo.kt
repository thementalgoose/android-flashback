package tmg.flashback.weekend.ui.shared

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import tmg.flashback.formula1.model.DriverConstructor
import tmg.flashback.providers.DriverConstructorProvider
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.text.TextBody2
import tmg.flashback.style.text.TextTitle
import tmg.flashback.ui.components.drivers.DriverImage
import tmg.flashback.ui.components.flag.Flag

private val positionWidth: Dp = 32.dp

@Composable
fun DriverInfoQualifying(
    driver: DriverConstructor,
    position: Int?,
    modifier: Modifier = Modifier
) {

}

@Composable
fun DriverInfoRace(
    driver: DriverConstructor,
    position: Int?,
    modifier: Modifier = Modifier
) {

}

@Composable
private fun Position(
    position: Int?
) {
    Box(modifier = Modifier.size(positionWidth)) {
        TextTitle(
            modifier = Modifier.align(Alignment.Center),
            text = position?.toString() ?: "-"
        )
    }
}

@Composable
fun DriverInfo(
    driver: DriverConstructor,
    position: Int?,
    modifier: Modifier = Modifier,
    extraContent: (@Composable RowScope.() -> Unit)? = null
) {
    tmg.flashback.ui.components.drivers.DriverInfo(
        driverName = driver.driver.name,
        driverNationalityISO = driver.driver.nationalityISO,
        constructorName = driver.constructor.name,
        constructorColor = driver.constructor.colour,
        position = position,
        modifier = modifier,
        extraContent = extraContent
    )
}

@PreviewTheme
@Composable
private fun Preview(
    @PreviewParameter(DriverConstructorProvider::class) driverConstructor: DriverConstructor
) {
    AppThemePreview {
        DriverInfo(driver = driverConstructor, position = 1)
    }
}

@PreviewTheme
@Composable
private fun PreviewShowPosition(
    @PreviewParameter(DriverConstructorProvider::class) driverConstructor: DriverConstructor
) {
    AppThemePreview {
        DriverInfo(driver = driverConstructor, position = null)
    }
}