package tmg.flashback.stats.ui.dashboard.drivers

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
import tmg.flashback.formula1.extensions.pointsDisplay
import tmg.flashback.formula1.model.SeasonDriverStandingSeason
import tmg.flashback.formula1.model.SeasonDriverStandings
import tmg.flashback.formula1.utils.getFlagResourceAlpha3
import tmg.flashback.providers.SeasonDriverStandingSeasonProvider
import tmg.flashback.providers.SeasonDriverStandingsProvider
import tmg.flashback.style.AppTheme
import tmg.flashback.style.text.TextBody2
import tmg.flashback.style.text.TextTitle
import tmg.flashback.stats.R
import tmg.flashback.stats.ui.messaging.Banner
import tmg.flashback.stats.ui.messaging.ProvidedBy
import tmg.flashback.style.AppThemePreview
import tmg.flashback.ui.components.header.Header
import tmg.flashback.ui.components.layouts.Container
import tmg.flashback.ui.components.progressbar.ProgressBar
import tmg.flashback.ui.utils.isInPreview
import kotlin.math.roundToInt

@Composable
fun DriverStandingsScreenVM(
    showMenu: Boolean,
    menuClicked: (() -> Unit)? = null,
    season: Int
) {
    val viewModel: DriversStandingViewModel by viewModel()
    viewModel.inputs.load(season)

    val isRefreshing = viewModel.outputs.isRefreshing.observeAsState(false)
    val items = viewModel.outputs.items.observeAsState(emptyList())
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = isRefreshing.value),
        onRefresh = viewModel.inputs::refresh
    ) {
        DriverStandingsScreen(
            showMenu = showMenu,
            menuClicked = menuClicked,
            itemClicked = viewModel.inputs::clickItem,
            season = season,
            items = items.value ?: emptyList()
        )
    }
}

@Composable
fun DriverStandingsScreen(
    showMenu: Boolean,
    menuClicked: (() -> Unit)? = null,
    itemClicked: (DriverStandingsModel) -> Unit,
    season: Int,
    items: List<DriverStandingsModel>
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.backgroundPrimary),
        content = {
            item(key = "header") {
                Header(
                    text = stringResource(id = R.string.season_standings_driver, season.toString()),
                    icon = when (showMenu) {
                        true -> painterResource(id = R.drawable.ic_menu)
                        false -> null
                    },
                    iconContentDescription = when (showMenu) {
                        true -> stringResource(id = R.string.ab_menu)
                        false -> null
                    },
                    actionUpClicked = {
                        menuClicked?.invoke()
                    }
                )
            }
            item(key = "info") {
                Banner()
                ProvidedBy()
                val content = items.firstOrNull { it.standings.inProgressContent != null }?.standings?.inProgressContent
                if (content != null) {
                    val (name, round) = content
                    Banner(
                        message = stringResource(R.string.results_accurate_for, name, round),
                        showLink = false
                    )
                }
            }
            items(items, key = { it.id }) { item ->
                DriverStandings(
                    model = item,
                    itemClicked = itemClicked,
                    maxPoints = (items.maxOfOrNull { it.standings.points } ?: 625.0),
                )
            }
            item(key = "footer") {
                Spacer(Modifier.height(72.dp))
            }
        }
    )
}

@Composable
fun DriverStandingsCard(
    model: DriverStandingsModel,
    itemClicked: (DriverStandingsModel) -> Unit,
    maxPoints: Double,
    modifier: Modifier = Modifier
) {
    Container(
        modifier = Modifier.padding(
            horizontal = AppTheme.dimensions.paddingMedium,
            vertical = AppTheme.dimensions.paddingXXSmall
        ),
        isSelected = model.isSelected
    ) {
        DriverStandings(
            model = model,
            itemClicked = itemClicked,
            maxPoints = maxPoints
        )
    }
}

@Composable
fun DriverStandings(
    model: DriverStandingsModel,
    itemClicked: (DriverStandingsModel) -> Unit,
    maxPoints: Double,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .clickable(onClick = {
                itemClicked(model)
            }),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TextTitle(
            text = model.standings.championshipPosition?.toString() ?: "-",
            bold = true,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(36.dp)
        )
        Box(modifier = Modifier
            .size(36.dp)
            .clip(RoundedCornerShape(AppTheme.dimensions.radiusSmall))
            .background(AppTheme.colors.backgroundSecondary)
        ) {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                model = model.standings.driver.photoUrl ?: R.drawable.unknown_avatar,
                contentDescription = null
            )
        }
        Row(modifier = Modifier
            .padding(
                top = AppTheme.dimensions.paddingSmall,
                start = AppTheme.dimensions.paddingSmall,
                end = AppTheme.dimensions.paddingMedium,
                bottom = AppTheme.dimensions.paddingSmall
            )
            .wrapContentHeight()
        ) {
            Column(modifier = Modifier.weight(2f)) {
                TextTitle(
                    text = model.standings.driver.name,
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
                        false -> LocalContext.current.getFlagResourceAlpha3(model.standings.driver.nationalityISO)
                    }
                    Image(
                        modifier = Modifier.size(16.dp),
                        painter = painterResource(id = resourceId),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                    )
                    Spacer(Modifier.width(4.dp))
                    TextBody2(
                        text = model.standings.constructors.joinToString { it.constructor.name },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            Spacer(Modifier.width(AppTheme.dimensions.paddingSmall))
            val progress = (model.standings.points / maxPoints).toFloat().coerceIn(0f, 1f)
            ProgressBar(
                modifier = Modifier
                    .weight(2f)
                    .height(48.dp)
                    .fillMaxHeight(),
                endProgress = progress,
                barColor = model.standings.constructors.lastOrNull()?.constructor?.colour ?: AppTheme.colors.primary,
                label = {
                    when (it) {
                        0f -> "0"
                        progress -> model.standings.points.pointsDisplay()
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
    @PreviewParameter(SeasonDriverStandingSeasonProvider::class) driverStandings: SeasonDriverStandingSeason
) {
    AppThemePreview(isLight = true) {
        DriverStandings(
            model = DriverStandingsModel(
                standings = driverStandings
            ),
            itemClicked = { },
            maxPoints = 25.0
        )
    }
}

@Preview
@Composable
private fun PreviewDark(
    @PreviewParameter(SeasonDriverStandingSeasonProvider::class) driverStandings: SeasonDriverStandingSeason
) {
    AppThemePreview(isLight = false) {
        DriverStandings(
            model = DriverStandingsModel(
                standings = driverStandings
            ),
            itemClicked = { },
            maxPoints = 25.0
        )
    }
}