package tmg.flashback.search.presentation.circuits

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import tmg.flashback.formula1.enums.TrackLayout
import tmg.flashback.formula1.model.Circuit
import tmg.flashback.style.AppTheme
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextBody2
import tmg.flashback.ui.components.flag.Flag

private val circuitIconSize = 48.dp

fun LazyListScope.Circuits(
    circuitClicked: (Circuit) -> Unit,
    circuits: List<Circuit>,
) {
    items(circuits, key = { it.id }) {
        CircuitRecord(
            circuitClicked = circuitClicked,
            circuit = it,
            modifier = Modifier
        )
    }
}
fun LazyGridScope.Circuits(
    circuitClicked: (Circuit) -> Unit,
    circuits: List<Circuit>,
) {
    items(circuits, key = { it.id }) {
        CircuitRecord(
            circuitClicked = circuitClicked,
            circuit = it,
            modifier = Modifier
        )
    }
}

@Composable
private fun CircuitRecord(
    circuitClicked: (Circuit) -> Unit,
    circuit: Circuit,
    modifier: Modifier = Modifier,
) {
    val trackIcon = TrackLayout.getTrack(circuit.id)?.getDefaultIcon()
    Row(
        modifier = modifier
            .clickable { circuitClicked(circuit) }
            .padding(
                horizontal = AppTheme.dimens.medium,
                vertical = AppTheme.dimens.small
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (trackIcon != null) {
            Icon(
                painter = painterResource(id = trackIcon),
                contentDescription = circuit.name,
                modifier = Modifier.size(circuitIconSize),
                tint = AppTheme.colors.contentPrimary
            )
        } else {
            Box(Modifier.width(circuitIconSize))
        }
        Column(
            modifier = Modifier.padding(
                start = AppTheme.dimens.medium,
                top = AppTheme.dimens.xsmall
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextBody1(
                text = circuit.name,
                modifier = Modifier.fillMaxWidth(),
                bold = true,
            )
            Row(modifier = Modifier.fillMaxWidth()) {
                Flag(
                    iso = circuit.countryISO,
                    nationality = circuit.country,
                    modifier = Modifier.size(16.dp),
                )
                Spacer(Modifier.width(8.dp))
                TextBody2(
                    text = circuit.country,
                    modifier = Modifier
                )
            }
        }
    }
}