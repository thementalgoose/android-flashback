package tmg.flashback.weekend.ui.shared

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import tmg.flashback.formula1.model.DriverConstructor
import tmg.flashback.providers.DriverConstructorProvider
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme

@Composable
fun DriverInfo(
    driver: DriverConstructor,
    position: Int?,
    modifier: Modifier = Modifier,
    grid: Int? = null,
    extraContent: (@Composable RowScope.() -> Unit)? = null
) {
    tmg.flashback.ui.components.drivers.DriverInfo(
        driverName = driver.driver.name,
        driverNationalityISO = driver.driver.nationalityISO,
        constructorName = driver.constructor.name,
        constructorColor = driver.constructor.colour,
        position = position,
        grid = grid,
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