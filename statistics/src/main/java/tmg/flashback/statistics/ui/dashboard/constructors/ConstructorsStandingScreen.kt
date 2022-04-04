package tmg.flashback.statistics.ui.dashboard.constructors

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import org.koin.androidx.compose.viewModel
import tmg.flashback.formula1.extensions.pointsDisplay
import tmg.flashback.formula1.model.SeasonConstructorStandingSeason
import tmg.flashback.formula1.model.SeasonConstructorStandingSeasonDriver
import tmg.flashback.formula1.utils.getFlagResourceAlpha3
import tmg.flashback.providers.SeasonConstructorStandingSeasonProvider
import tmg.flashback.statistics.R
import tmg.flashback.statistics.composables.DriverPoints
import tmg.flashback.statistics.composables.TextRanking
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.text.TextBody2
import tmg.flashback.style.text.TextTitle
import tmg.flashback.ui.components.header.Header
import tmg.flashback.ui.components.progressbar.ProgressBar
import tmg.flashback.ui.utils.isInPreview
import tmg.flashback.ui.utils.pluralResource
import kotlin.math.roundToInt

@Composable
fun ConstructorsStandingScreen(
    season: Int,
    menuClicked: (() -> Unit)?
) {
    val viewModel by viewModel<ConstructorsStandingViewModel>()

    val list = viewModel.outputs.items.observeAsState()
    val isRefreshing = viewModel.outputs.isRefreshing.observeAsState(false)
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = isRefreshing.value),
        onRefresh = viewModel.inputs::refresh
    ) {
        ConstructorsStandingScreenImpl(
            season = season,
            list = list.value ?: emptyList(),
            menuClicked = menuClicked
        )
    }
}

@Composable
private fun ConstructorsStandingScreenImpl(
    season: Int,
    list: List<SeasonConstructorStandingSeason>,
    menuClicked: (() -> Unit)?,
) {
    var maxPoints: Double = list.maxByOrNull { it.points }?.points ?: 0.0
    if (maxPoints == 0.0) {
        maxPoints = 625.0
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.backgroundPrimary),
        content = {
            item {
                Header(
                    text = stringResource(id = R.string.dashboard_standings_constructor, season.toString()),
                    icon = menuClicked?.let { painterResource(id = R.drawable.ic_menu) },
                    iconContentDescription = stringResource(id = R.string.ab_menu),
                    actionUpClicked = {
                        menuClicked?.invoke()
                    }
                )
            }
            items(list, key = { it.constructor.id }) {
                ConstructorView(
                    model = it,
                    maxPoints = maxPoints,
                    itemClicked = { }
                )
            }
            item {
                Spacer(Modifier.height(AppTheme.dimensions.paddingXLarge))
            }
        }
    )
}

@Composable
private fun ConstructorView(
    model: SeasonConstructorStandingSeason,
    itemClicked: (SeasonConstructorStandingSeason) -> Unit,
    maxPoints: Double,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.clickable(onClick = {
            itemClicked(model)
        }),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TextRanking(model.championshipPosition)
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
                model.drivers.forEach {
                    DriverPoints(
                        driver = it.driver,
                        points = it.points
                    )
                }
            }
            Spacer(Modifier.width(AppTheme.dimensions.paddingSmall))
            val progress = (model.points / maxPoints).toFloat().coerceIn(0f, 1f)
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
                        else -> (it * maxPoints).roundToInt().toString()
                    }
                }
            )
        }
    }
}

@Preview
@Composable
private fun PreviewLight(
    @PreviewParameter(SeasonConstructorStandingSeasonProvider::class) model: SeasonConstructorStandingSeason
) {
    AppThemePreview(isLight = true) {
        ConstructorsStandingScreenImpl(
            season = 2022,
            list = List(5) { model.copy(constructor = model.constructor.copy(id = "$it")) },
            menuClicked = {}
        )
    }
}

@Preview
@Composable
private fun PreviewDark(
    @PreviewParameter(SeasonConstructorStandingSeasonProvider::class) model: SeasonConstructorStandingSeason
) {
    AppThemePreview(isLight = false) {
        ConstructorsStandingScreenImpl(
            season = 2022,
            list = List(5) { model.copy(constructor = model.constructor.copy(id = "$it")) },
            menuClicked = {}
        )
    }
}