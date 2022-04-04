package tmg.flashback.statistics.ui.weekend.constructor

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.viewModel
import tmg.flashback.formula1.extensions.pointsDisplay
import tmg.flashback.statistics.composables.DriverPoints
import tmg.flashback.statistics.composables.TextRanking
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.text.TextTitle
import tmg.flashback.ui.components.progressbar.ProgressBar
import kotlin.math.roundToInt

@Composable
fun ConstructorScreen(
    season: Int,
    round: Int
) {
    val viewModel by viewModel<ConstructorViewModel>()
    viewModel.inputs.load(season, round)

    val standings = viewModel.outputs.list.observeAsState(emptyList())
    ConstructorScreenImpl(
        list = standings.value
    )
}

@Composable
private fun ConstructorScreenImpl(
    list: List<ConstructorModel>
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        list.forEach {
            ConstructorView(
                model = it,
                itemClicked = { }
            )
        }
    }
}

@Composable
private fun ConstructorView(
    model: ConstructorModel,
    itemClicked: (ConstructorModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.clickable(onClick = {
            itemClicked(model)
        }),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TextRanking(model.position)
        Row(modifier = Modifier.padding(
            top = AppTheme.dimensions.paddingSmall,
            start = AppTheme.dimensions.paddingSmall,
            end = AppTheme.dimensions.paddingMedium,
            bottom = AppTheme.dimensions.paddingSmall
        )) {
            Column(modifier = Modifier.weight(3f)) {
                TextTitle(
                    text = model.constructor.name,
                    bold = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(2.dp))
                model.drivers.forEach { (driver, points) ->
                    DriverPoints(
                        driver = driver,
                        points = points,
                    )
                }
            }
            Spacer(Modifier.width(AppTheme.dimensions.paddingSmall))
            val progress = (model.points / model.maxTeamPoints).toFloat().coerceIn(0f, 1f)
            ProgressBar(
                modifier = Modifier
                    .weight(2f)
                    .height(IntrinsicSize.Max),
                endProgress = progress,
                barColor = model.constructor.colour,
                label = {
                    when (it) {
                        0f -> "0"
                        progress -> model.points.pointsDisplay()
                        else -> (it * model.maxTeamPoints).roundToInt().toString()
                    }
                }
            )
        }
    }
}


@Preview
@Composable
private fun PreviewLight() {
    AppThemePreview(isLight = true) {
        ConstructorScreenImpl(
            list = emptyList()
        )
    }
}

@Preview
@Composable
private fun PreviewDark() {
    AppThemePreview(isLight = false) {
        ConstructorScreenImpl(
            list = emptyList()
        )
    }
}