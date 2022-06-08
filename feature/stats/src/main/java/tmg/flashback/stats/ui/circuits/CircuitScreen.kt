package tmg.flashback.stats.ui.circuits

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable

@Composable
fun CircuitScreenVM(
    circuitId: String,
    circuitName: String
) {

}

@Composable
fun CircuitScreen() {
    LazyColumn(content = {
        item(key = "header") {  }
    })
}