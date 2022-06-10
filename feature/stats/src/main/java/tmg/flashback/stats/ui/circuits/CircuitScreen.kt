package tmg.flashback.stats.ui.circuits

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import org.koin.androidx.compose.viewModel
import tmg.flashback.stats.R
import tmg.flashback.style.text.TextBody1
import tmg.flashback.ui.components.header.Header

@Composable
fun CircuitScreenVM(
    circuitId: String,
    circuitName: String,
    actionUpClicked: () -> Unit
) {
    val viewModel by viewModel<CircuitViewModel>()
    viewModel.inputs.load(circuitId)

    CircuitScreen(
        circuitName = circuitName,
        actionUpClicked = actionUpClicked
    )
}

@Composable
fun CircuitScreen(
    circuitName: String,
    list: List<CircuitModel>,
    actionUpClicked: () -> Unit
) {
    LazyColumn(content = {
        item(key = "header") {
            Header(
                text = circuitName,
                icon = painterResource(id = R.drawable.ic_back),
                iconContentDescription = stringResource(id = R.string.ab_back),
                actionUpClicked = actionUpClicked
            )
        }
        items(list, key = { it.id }) {
            when (it) {
                is CircuitModel.Item -> {

                }
            }
        }
    })
}

@Composable
private fun Item(
    model: CircuitModel.Item,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        TextBody1(text = model.data.)
    }
}