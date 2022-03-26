package tmg.flashback.statistics.ui.dashboard.constructors

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import tmg.flashback.formula1.model.OverviewRace
import tmg.flashback.formula1.model.SeasonConstructorStandingSeason
import tmg.flashback.formula1.model.SeasonConstructorStandingSeasonDriver
import tmg.flashback.formula1.model.SeasonDriverStandingSeason
import tmg.flashback.providers.SeasonConstructorStandingSeasonProvider
import tmg.flashback.statistics.R
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextBody2
import tmg.flashback.style.text.TextTitle
import tmg.flashback.ui.components.header.Header
import tmg.flashback.ui.components.progressbar.ProgressBar
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
    ConstructorsStandingScreenImpl(
        season = season,
        list = list.value ?: emptyList(),
        isRefreshing = isRefreshing.value,
        onRefresh = viewModel.inputs::refresh,
        menuClicked = menuClicked
    )
}

@Composable
private fun ConstructorsStandingScreenImpl(
    season: Int,
    list: List<SeasonConstructorStandingSeason>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    menuClicked: (() -> Unit)?
) {
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
        onRefresh = onRefresh
    ) {
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
                        itemClicked = { }
                    )
                }
                item {
                    Spacer(Modifier.height(AppTheme.dimensions.paddingXLarge))
                }
            }
        )
    }
}

@Composable
private fun ConstructorView(
    model: SeasonConstructorStandingSeason,
    itemClicked: (SeasonConstructorStandingSeason) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.clickable(onClick = {
            itemClicked(model)
        }),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TextTitle(
            text = model.championshipPosition?.toString() ?: "-",
            bold = true,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(32.dp)
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
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(2.dp))
                model.drivers.forEach {
                    DriverPoints(model = it)
                    Spacer(Modifier.height(2.dp))
                }
            }
            Spacer(Modifier.width(AppTheme.dimensions.paddingSmall))
            ProgressBar(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxHeight(),
                endProgress = 0.8f,
                barColor = model.constructor.colour,
                label = { (it * model.points).roundToInt().toString() }
            )
        }
    }
}

@Composable
private fun DriverPoints(
    model: SeasonConstructorStandingSeasonDriver,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextBody2(text = model.driver.name)
        Box(modifier = Modifier
            .size(16.dp)
            .padding(horizontal = AppTheme.dimensions.paddingXSmall)
        )
        TextBody2(text = pluralResource(R.plurals.race_points, model.points.roundToInt(), model.points.pointsDisplay()))
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
            list = listOf(model),
            isRefreshing = false,
            onRefresh = {},
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
            list = listOf(model),
            isRefreshing = false,
            onRefresh = {},
            menuClicked = {}
        )
    }
}