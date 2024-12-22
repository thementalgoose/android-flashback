package tmg.flashback.search.presentation.circuits

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import tmg.flashback.formula1.model.Circuit
import tmg.flashback.style.AppTheme
import tmg.flashback.style.text.TextBody2

fun LazyListScope.Circuits(
    circuitClicked: (Circuit) -> Unit,
    circuits: List<Circuit>,
) {
    items(circuits, key = { it.id }) {
        CircuitRecord(
            circuitClicked = circuitClicked,
            circuit = it,
            modifier = Modifier.padding(
                horizontal = AppTheme.dimens.medium,
                vertical = AppTheme.dimens.small
            )
        )
    }
}

@Composable
private fun CircuitRecord(
    circuitClicked: (Circuit) -> Unit,
    circuit: Circuit,
    modifier: Modifier = Modifier,
) {
    TextBody2("Circuit ${circuit.name}")
}