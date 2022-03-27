package tmg.flashback.statistics.ui.dashboard.drivers

import android.view.RoundedCorner
import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import org.koin.androidx.compose.viewModel
import org.threeten.bp.LocalDate
import tmg.flashback.formula1.extensions.pointsDisplay
import tmg.flashback.formula1.model.Driver
import tmg.flashback.formula1.model.OverviewRace
import tmg.flashback.formula1.model.SeasonDriverStandingSeason
import tmg.flashback.formula1.model.SeasonDriverStandingSeasonConstructor
import tmg.flashback.formula1.utils.getFlagResourceAlpha3
import tmg.flashback.providers.DriverProvider
import tmg.flashback.providers.SeasonDriverStandingSeasonProvider
import tmg.flashback.statistics.R
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextBody2
import tmg.flashback.style.text.TextTitle
import tmg.flashback.ui.components.header.Header
import tmg.flashback.ui.components.progressbar.ProgressBar
import tmg.flashback.ui.utils.isInPreview
import kotlin.math.roundToInt

@Composable
fun DriversStandingScreen(
    season: Int,
    menuClicked: (() -> Unit)?
) {
    val viewModel by viewModel<DriversStandingViewModel>()

    val list = viewModel.outputs.items.observeAsState()
    val isRefreshing = viewModel.outputs.isRefreshing.observeAsState(false)
    DriversStandingScreenImpl(
        season = season,
        list = list.value ?: emptyList(),
        isRefreshing = isRefreshing.value,
        onRefresh = viewModel.inputs::refresh,
        menuClicked = menuClicked
    )
}

@Composable
private fun DriversStandingScreenImpl(
    season: Int,
    list: List<SeasonDriverStandingSeason>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    menuClicked: (() -> Unit)?
) {
    var maxPoints: Double = list.maxByOrNull { it.points }?.points ?: 0.0
    if (maxPoints == 0.0) {
        maxPoints = 625.0
    }

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
                        text = stringResource(id = R.string.dashboard_standings_driver, season.toString()),
                        icon = menuClicked?.let { painterResource(id = R.drawable.ic_menu) },
                        iconContentDescription = stringResource(id = R.string.ab_menu),
                        actionUpClicked = {
                            menuClicked?.invoke()
                        }
                    )
                }
                items(list, key = { it.driver.id }) {
                    DriverView(
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
}

@Composable
private fun DriverView(
    model: SeasonDriverStandingSeason,
    itemClicked: (SeasonDriverStandingSeason) -> Unit,
    maxPoints: Double,
    modifier: Modifier = Modifier,
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
            modifier = Modifier.width(42.dp)
        )
        Box(modifier = Modifier
            .size(36.dp)
            .clip(RoundedCornerShape(AppTheme.dimensions.radiusSmall))
            .background(AppTheme.colors.backgroundSecondary)
        ) {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                model = model.driver.photoUrl ?: R.drawable.unknown_avatar,
                contentDescription = null
            )
        }
        Row(modifier = Modifier.padding(
            top = AppTheme.dimensions.paddingSmall,
            start = AppTheme.dimensions.paddingSmall,
            end = AppTheme.dimensions.paddingMedium,
            bottom = AppTheme.dimensions.paddingSmall
        )) {
            Column(modifier = Modifier.weight(2f)) {
                TextTitle(
                    text = model.driver.name,
                    bold = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(modifier = Modifier
                    .fillMaxHeight()
                    .padding(
                        vertical = AppTheme.dimensions.paddingXXSmall,
                    )
                ) {
                    val resourceId = when (isInPreview()) {
                        true -> R.drawable.gb
                        false -> LocalContext.current.getFlagResourceAlpha3(model.driver.nationalityISO)
                    }
                    Image(
                        modifier = Modifier.size(16.dp),
                        painter = painterResource(id = resourceId),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                    )
                    Spacer(Modifier.width(4.dp))
                    TextBody2(
                        text = model.constructors.joinToString { it.constructor.name },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            Spacer(Modifier.width(AppTheme.dimensions.paddingSmall))
            val progress = (model.points / maxPoints).toFloat().coerceIn(0f, 1f)
            ProgressBar(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxHeight(),
                endProgress = progress,
                barColor = model.constructors.lastOrNull()?.constructor?.colour ?: AppTheme.colors.primary,
                label = {
                    when (it) {
                        0f -> "0"
                        progress -> model.points.pointsDisplay()
                        else -> (it * maxPoints).pointsDisplay()
                    }
                }
            )
        }
    }
}


@Preview
@Composable
private fun PreviewLight(
    @PreviewParameter(SeasonDriverStandingSeasonProvider::class) standing: SeasonDriverStandingSeason
) {
    AppThemePreview(isLight = true) {
        DriversStandingScreenImpl(
            season = 2022,
            list = listOf(standing),
            isRefreshing = false,
            onRefresh = {},
            menuClicked = {}
        )
    }
}

@Preview
@Composable
private fun PreviewDark(
    @PreviewParameter(SeasonDriverStandingSeasonProvider::class) standing: SeasonDriverStandingSeason
) {
    AppThemePreview(isLight = false) {
        DriversStandingScreenImpl(
            season = 2022,
            list = listOf(standing),
            isRefreshing = false,
            onRefresh = {},
            menuClicked = {}
        )
    }
}