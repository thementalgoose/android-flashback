package tmg.flashback.drivers.presentation.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import tmg.flashback.formula1.R
import tmg.flashback.formula1.model.Constructor
import tmg.flashback.formula1.model.Driver
import tmg.flashback.formula1.model.DriverEntry
import tmg.flashback.providers.DriverConstructorProvider
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.badge.Badge
import tmg.flashback.style.badge.BadgeView
import tmg.flashback.ui.utils.DrawableUtils.getFlagResourceAlpha3
import tmg.flashback.ui.utils.isInPreview
import tmg.utilities.extensions.format

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DriverBadges(
    driver: Driver,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    spacedBy: Dp = AppTheme.dimens.small,
    constructors: List<Constructor> = emptyList()
) {
    FlowRow(
        modifier = Modifier
            .padding(top = 4.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(spacedBy, verticalAlignment),
        horizontalArrangement = Arrangement.spacedBy(spacedBy, horizontalAlignment)
    ) {
        val context = LocalContext.current
        val resourceId = when (isInPreview()) {
            true -> com.murgupluoglu.flagkit.R.drawable.gb
            false -> context.getFlagResourceAlpha3(driver.nationalityISO)
        }
        BadgeView(model = Badge(label = driver.nationality, icon = resourceId), tintIcon = null)

        val birthday = driver.dateOfBirth.format("dd MMMM yyyy")!!
        BadgeView(model = Badge(label = birthday, icon = R.drawable.ic_driver_birthday))

        if (driver.code != null && driver.number != null) {
            BadgeView(
                model = Badge(
                    label = "${driver.code} ${driver.number}",
                    icon = R.drawable.ic_driver_code
                )
            )
        }

        constructors.forEach {
            BadgeView(
                model = Badge(
                    label = it.name,
                    icon = tmg.flashback.ui.R.drawable.ic_circle
                ),
                tintIcon = it.colour
            )
        }
    }
}

@PreviewTheme
@Composable
private fun Preview(
    @PreviewParameter(DriverConstructorProvider::class) entry: DriverEntry
) {
    AppThemePreview {
        DriverBadges(
            driver = entry.driver,
            constructors = listOf(entry.constructor)
        )
    }
}