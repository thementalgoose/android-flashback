package tmg.flashback.stats.ui.circuits

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.viewModel
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.formula1.model.CircuitHistoryRace
import tmg.flashback.providers.CircuitHistoryRaceProvider
import tmg.flashback.stats.R
import tmg.flashback.stats.ui.shared.DriverImage
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextBody2
import tmg.flashback.ui.components.header.Header
import tmg.utilities.extensions.ordinalAbbreviation

private val driverImageBoxP1: Dp = 48.dp
private val driverImageBoxP2: Dp = 42.dp
private val driverImageBoxP3: Dp = 36.dp

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
        list = emptyList(),
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
                is CircuitModel.Item -> Item(it)
            }
        }
    })
}

@Composable
private fun Item(
    model: CircuitModel.Item,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            TextBody1(
                text = model.data.name,
                bold = true
            )
            TextBody2(
                text = model.data.date.format(DateTimeFormatter.ofPattern("'${model.data.date.dayOfMonth.ordinalAbbreviation}' MMMM yyyy"))
            )
        }

        model.data.preview.firstOrNull { it.position == 1 }?.let { position ->
            Box(
                Modifier
                    .size(driverImageBoxP1)
                    .background(Color.Red)
                    .clip(RoundedCornerShape(AppTheme.dimensions.radiusSmall))
                    .padding(2.dp)
            ) {
                DriverImage(
                    driver = position.driver,
                    size = driverImageBoxP1 - 4.dp
                )
            }
        }
        model.data.preview.firstOrNull { it.position == 2 }?.let { position ->
            Box(
                Modifier
                    .size(driverImageBoxP2)
                    .background(Color.Blue)
                    .clip(RoundedCornerShape(AppTheme.dimensions.radiusSmall))
                    .padding(2.dp)
            ) {
                DriverImage(
                    driver = position.driver,
                    size = driverImageBoxP2 - 4.dp
                )
            }
        }
        model.data.preview.firstOrNull { it.position == 3 }?.let { position ->
            Box(
                Modifier
                    .size(driverImageBoxP3)
                    .background(Color.Green)
                    .clip(RoundedCornerShape(AppTheme.dimensions.radiusSmall))
                    .padding(2.dp)
            ) {
                DriverImage(
                    driver = position.driver,
                    size = driverImageBoxP3 - 4.dp
                )
            }
        }
    }
}

@PreviewTheme
@Composable
private fun Preview(
    @PreviewParameter(CircuitHistoryRaceProvider::class) race: CircuitHistoryRace
) {
    AppThemePreview {
        CircuitScreen(
            circuitName = "Circuit Name",
            list = listOf(
                CircuitModel.Item(race)
            ),
            actionUpClicked = { }
        )
    }
}