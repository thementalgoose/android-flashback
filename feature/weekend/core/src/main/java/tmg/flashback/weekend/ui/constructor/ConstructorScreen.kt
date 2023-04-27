package tmg.flashback.weekend.ui.constructor

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import tmg.flashback.formula1.extensions.pointsDisplay
import tmg.flashback.formula1.model.DriverEntry
import tmg.flashback.providers.DriverConstructorProvider
import tmg.flashback.weekend.ui.shared.DriverPoints
import tmg.flashback.weekend.ui.fakeWeekendInfo
import tmg.flashback.weekend.ui.info.RaceInfoHeader
import tmg.flashback.ui.components.errors.NotAvailable
import tmg.flashback.ui.components.errors.NotAvailableYet
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.text.TextTitle
import tmg.flashback.ui.components.loading.SkeletonViewList
import tmg.flashback.ui.components.progressbar.ProgressBar
import tmg.flashback.weekend.R
import tmg.flashback.weekend.contract.model.WeekendInfo
import tmg.utilities.extensions.ordinalAbbreviation
import kotlin.math.roundToInt

@Composable
fun ConstructorScreenVM(
    info: WeekendInfo,
    actionUpClicked: () -> Unit,
    viewModel: ConstructorViewModel = hiltViewModel()
) {
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

            this.constructor(
                list = list,
                itemClicked = itemClicked
            )
        }
    )
}

internal fun LazyListScope.constructor(
    list: List<ConstructorModel>,
    itemClicked: (ConstructorModel.Constructor) -> Unit,
) {
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

@Composable
private fun ConstructorResult(
    model: ConstructorModel.Constructor,
    itemClicked: (ConstructorModel.Constructor) -> Unit,
    modifier: Modifier = Modifier,
) {
    val contentDescription = "${model.position?.ordinalAbbreviation}. ${stringResource(id = R.string.ab_scored, model.constructor.name, model.points.pointsDisplay())}."
    val drivers = model.drivers
        .map {
            stringResource(id = R.string.ab_scored, it.first.name, it.second.pointsDisplay())
        }
        .joinToString(separator = ",")

    Row(
        modifier = modifier
            .semantics(mergeDescendants = true) {  }
            .clearAndSetSemantics {
                this.contentDescription = contentDescription + drivers
            }
            .clickable(onClick = {
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
            top = AppTheme.dimens.small,
            start = AppTheme.dimens.small,
            end = AppTheme.dimens.medium,
            bottom = AppTheme.dimens.small
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
            Spacer(Modifier.width(AppTheme.dimens.small))
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
    @PreviewParameter(DriverConstructorProvider::class) both: DriverEntry
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