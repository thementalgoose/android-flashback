package tmg.flashback.stats.ui.weekend.constructor

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.viewModel
import tmg.flashback.formula1.extensions.pointsDisplay
import tmg.flashback.formula1.model.DriverConstructor
import tmg.flashback.providers.DriverConstructorProvider
import tmg.flashback.stats.components.DriverPoints
import tmg.flashback.stats.ui.dashboard.constructors.ConstructorStandingsModel
import tmg.flashback.stats.ui.weekend.WeekendInfo
import tmg.flashback.stats.ui.weekend.fakeWeekendInfo
import tmg.flashback.stats.ui.weekend.info.RaceInfoHeader
import tmg.flashback.stats.ui.weekend.shared.NotAvailable
import tmg.flashback.stats.ui.weekend.shared.NotAvailableYet
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.text.TextBody2
import tmg.flashback.style.text.TextTitle
import tmg.flashback.ui.components.loading.SkeletonView
import tmg.flashback.ui.components.loading.SkeletonViewList
import tmg.flashback.ui.components.progressbar.ProgressBar
import kotlin.math.roundToInt

@Composable
fun ConstructorScreenVM(
    info: WeekendInfo,
    actionUpClicked: () -> Unit
) {
    val viewModel by viewModel<ConstructorViewModel>()
    viewModel.inputs.load(
        season = info.season,
        round = info.round
    )

    val list = viewModel.outputs.list.observeAsState(listOf(ConstructorModel.Loading))
    ConstructorScreen(
        info = info,
        itemClicked = viewModel.inputs::clickItem,
        list = list.value,
        actionUpClicked = actionUpClicked
    )
}

@Composable
fun ConstructorScreen(
    info: WeekendInfo,
    list: List<ConstructorModel>,
    itemClicked: (ConstructorModel.Constructor) -> Unit,
    actionUpClicked: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        content = {
            item("header") {
                RaceInfoHeader(
                    model = info,
                    actionUpClicked = actionUpClicked
                )
            }
            items(list) {
                when (it) {
                    is ConstructorModel.Constructor -> {
                        ConstructorResult(
                            model = it,
                            itemClicked = itemClicked
                        )
                    }
                    ConstructorModel.Loading -> {
                        SkeletonViewList()
                    }
                    ConstructorModel.NotAvailable -> {
                        NotAvailable()
                    }
                    ConstructorModel.NotAvailableYet -> {
                        NotAvailableYet()
                    }
                }
            }
            item(key = "footer") {
                Spacer(Modifier.height(72.dp))
            }
        }
    )
}

@Composable
private fun ConstructorResult(
    model: ConstructorModel.Constructor,
    itemClicked: (ConstructorModel.Constructor) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.clickable(onClick = {
            itemClicked(model)
        }),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TextTitle(
            text = model.position?.toString() ?: "-",
            bold = true,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(36.dp)
        )
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
                        points = points
                    )
                }
            }
            Spacer(Modifier.width(AppTheme.dimensions.paddingSmall))
            val progress = (model.points / model.maxTeamPoints).toFloat().coerceIn(0f, 1f)
            ProgressBar(
                modifier = Modifier
                    .weight(2f)
                    .height(48.dp)
                    .fillMaxHeight(),
                endProgress = progress,
                barColor = model.constructor.colour,
                label = {
                    when (it) {
                        0f -> "0"
                        progress -> model.points.pointsDisplay()
                        else -> (it * model.maxTeamPoints).takeIf { !it.isNaN() }?.roundToInt()?.toString() ?: model.points.pointsDisplay()
                    }
                }
            )
        }
    }
}

@PreviewTheme
@Composable
private fun Preview(
    @PreviewParameter(DriverConstructorProvider::class) both: DriverConstructor
) {
    AppThemePreview {
        ConstructorScreen(
            info = fakeWeekendInfo,
            list = listOf(
                ConstructorModel.Constructor(
                    constructor = both.constructor,
                    position = 1,
                    points = 25.0,
                    drivers = listOf(
                        both.driver to 18.0
                    ),
                    maxTeamPoints = 25.0
                )
            ),
            itemClicked = {},
            actionUpClicked = {}
        )
    }
}