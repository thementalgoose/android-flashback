package tmg.flashback.stats.ui.dashboard.drivers

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.hilt.navigation.compose.hiltViewModel
import tmg.flashback.formula1.extensions.pointsDisplay
import tmg.flashback.formula1.model.SeasonDriverStandingSeason
import tmg.flashback.providers.SeasonDriverStandingSeasonProvider
import tmg.flashback.style.AppTheme
import tmg.flashback.style.text.TextBody2
import tmg.flashback.style.text.TextTitle
import tmg.flashback.stats.R
import tmg.flashback.stats.ui.dashboard.DashboardQuickLinks
import tmg.flashback.stats.ui.messaging.Banner
import tmg.flashback.stats.ui.shared.Flag
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.ui.components.errors.NetworkError
import tmg.flashback.ui.components.header.Header
import tmg.flashback.ui.components.layouts.Container
import tmg.flashback.ui.components.loading.SkeletonViewList
import tmg.flashback.ui.components.progressbar.ProgressBar
import tmg.flashback.ui.components.swiperefresh.SwipeRefresh
import kotlin.math.roundToInt

@Composable
fun DriverStandingsScreenVM(
    showMenu: Boolean,
    menuClicked: (() -> Unit)? = null,
    season: Int,
    viewModel: DriversStandingViewModel = hiltViewModel()
) {
    viewModel.inputs.load(season)

    val isRefreshing = viewModel.outputs.isRefreshing.observeAsState(false)
    val items = viewModel.outputs.items.observeAsState(listOf())
    SwipeRefresh(
        isLoading = isRefreshing.value,
        onRefresh = viewModel.inputs::refresh
    ) {
        DriverStandingsScreen(
            showMenu = showMenu,
            menuClicked = menuClicked,
            itemClicked = viewModel.inputs::clickItem,
            season = season,
            items = items.value
        )
    }
}

@Composable
fun DriverStandingsScreen(
    showMenu: Boolean,
    menuClicked: (() -> Unit)? = null,
    itemClicked: (DriverStandingsModel.Standings) -> Unit,
    season: Int,
    items: List<DriverStandingsModel>?
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
                DashboardQuickLinks(season = season)
                val content = (items ?: emptyList())
                    .filterIsInstance<DriverStandingsModel.Standings>()
                    .firstOrNull { it.standings.inProgressContent != null }?.standings?.inProgressContent
                if (content != null) {
                    val (name, round) = content
                    Banner(
                        message = stringResource(R.string.results_accurate_for, name, round),
                        showLink = false
                    )
                }
            }

            if (items == null) {
                item(key = "network") {
                    NetworkError()
                }
            }

            val maxPoints = (items ?: emptyList())
                .filterIsInstance<DriverStandingsModel.Standings>()
                .maxOfOrNull { it.standings.points } ?: 625.0
            items(items ?: emptyList(), key = { it.id }) { item ->
                when (item) {
                    is DriverStandingsModel.Standings -> {
                        DriverStandings(
                            model = item,
                            itemClicked = itemClicked,
                            maxPoints = maxPoints,
                        )
                    }
                    DriverStandingsModel.Loading -> {
                        SkeletonViewList()
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
fun DriverStandingsCard(
    model: DriverStandingsModel.Standings,
    itemClicked: (DriverStandingsModel.Standings) -> Unit,
    maxPoints: Double,
    modifier: Modifier = Modifier
) {
    Container(
        modifier = modifier.padding(
            horizontal = AppTheme.dimens.medium,
            vertical = AppTheme.dimens.xxsmall
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
    model: DriverStandingsModel.Standings,
    itemClicked: (DriverStandingsModel.Standings) -> Unit,
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
            .clip(RoundedCornerShape(AppTheme.dimens.radiusSmall))
            .background(AppTheme.colors.backgroundSecondary)
        ) {
            AsyncImage(
                error = painterResource(id = R.drawable.unknown_avatar),
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                model = model.standings.driver.photoUrl ?: R.drawable.unknown_avatar,
                contentDescription = null
            )
        }
        Row(modifier = Modifier
            .padding(
                top = AppTheme.dimens.small,
                start = AppTheme.dimens.small,
                end = AppTheme.dimens.medium,
                bottom = AppTheme.dimens.small
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
                        vertical = AppTheme.dimens.xxsmall,
                    )
                ) {
                    Flag(
                        iso = model.standings.driver.nationalityISO,
                        nationality = model.standings.driver.nationality,
                        modifier = Modifier.size(16.dp),
                    )
                    Spacer(Modifier.width(4.dp))
                    TextBody2(
                        text = model.standings.constructors.joinToString { it.constructor.name },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            Spacer(Modifier.width(AppTheme.dimens.small))
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
                        else -> (it * maxPoints).takeIf { !it.isNaN() }?.roundToInt()?.toString() ?: model.standings.points.pointsDisplay()
                    }
                }
            )
        }
    }
}

@PreviewTheme
@Composable
private fun Preview(
    @PreviewParameter(SeasonDriverStandingSeasonProvider::class) driverStandings: SeasonDriverStandingSeason
) {
    AppThemePreview {
        DriverStandings(
            model = DriverStandingsModel.Standings(
                standings = driverStandings
            ),
            itemClicked = { },
            maxPoints = 25.0
        )
    }
}