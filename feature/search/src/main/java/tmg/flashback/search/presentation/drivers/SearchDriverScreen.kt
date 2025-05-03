package tmg.flashback.search.presentation.drivers

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import tmg.flashback.formula1.model.Driver
import tmg.flashback.style.AppTheme
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextBody2
import tmg.flashback.ui.components.drivers.DriverIcon
import tmg.flashback.ui.components.flag.Flag

private val driverIconSize = 42.dp

fun LazyListScope.Drivers(
    driverClicked: (Driver) -> Unit,
    drivers: List<Driver>,
) {
    items(drivers, key = { it.id }) {
        DriverRecord(
            driverClicked = driverClicked,
            driver = it,
            modifier = Modifier
        )
    }
}

fun LazyGridScope.Drivers(
    driverClicked: (Driver) -> Unit,
    drivers: List<Driver>,
) {
    items(drivers, key = { it.id }) {
        DriverRecord(
            driverClicked = driverClicked,
            driver = it,
            modifier = Modifier
        )
    }
}

@Composable
private fun DriverRecord(
    driverClicked: (Driver) -> Unit,
    driver: Driver,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier
        .clickable(
            onClick = { driverClicked(driver) }
        )
        .padding(
            horizontal = AppTheme.dimens.medium,
            vertical = AppTheme.dimens.small
        )
    ) {
        DriverIcon(
            photoUrl = driver.photoUrl,
            modifier = Modifier,
            number = driver.number,
            code = driver.code,
            size = driverIconSize,
        )
        Column(
            modifier = Modifier.padding(
                start = AppTheme.dimens.medium,
                top = AppTheme.dimens.xsmall
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextBody1(
                text = driver.name,
                modifier = Modifier.fillMaxWidth(),
                bold = true,
            )
            Row(modifier = Modifier.fillMaxWidth()) {
                Flag(
                    iso = driver.nationalityISO,
                    nationality = driver.nationality,
                    modifier = Modifier.size(16.dp),
                )
                Spacer(Modifier.width(8.dp))
                TextBody2(
                    text = driver.nationality,
                    modifier = Modifier
                )
            }
        }
    }
}