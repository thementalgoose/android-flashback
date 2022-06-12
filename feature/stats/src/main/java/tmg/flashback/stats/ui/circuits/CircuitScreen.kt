package tmg.flashback.stats.ui.circuits

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.faltenreich.skeletonlayout.Skeleton
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import org.koin.androidx.compose.viewModel
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.formula1.enums.TrackLayout
import tmg.flashback.formula1.model.CircuitHistoryRace
import tmg.flashback.formula1.model.CircuitHistoryRaceResult
import tmg.flashback.formula1.model.Location
import tmg.flashback.formula1.utils.getFlagResourceAlpha3
import tmg.flashback.providers.CircuitHistoryRaceProvider
import tmg.flashback.stats.R
import tmg.flashback.stats.ui.weekend.shared.NotAvailable
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextBody2
import tmg.flashback.ui.components.header.Header
import tmg.flashback.ui.components.loading.SkeletonView
import tmg.flashback.ui.components.loading.SkeletonViewList
import tmg.flashback.ui.utils.isInPreview
import tmg.utilities.extensions.ordinalAbbreviation

private val driverImageBoxP1: Dp = 48.dp
private val driverImageBoxP2: Dp = 42.dp
private val driverImageBoxP3: Dp = 36.dp

private val trackImageSize: Dp = 180.dp

private val countryBadgeSize = 32.dp

@Composable
fun CircuitScreenVM(
    circuitId: String,
    circuitName: String,
    actionUpClicked: () -> Unit
) {
    val viewModel by viewModel<CircuitViewModel>()
    viewModel.inputs.load(circuitId)

    val list = viewModel.outputs.list.observeAsState(listOf(CircuitModel.Loading))
    val isLoading = viewModel.outputs.showLoading.observeAsState(false)
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = isLoading.value),
        onRefresh = viewModel.inputs::refresh
    ) {
        CircuitScreen(
            circuitName = circuitName,
            list = list.value,
            linkClicked = viewModel.inputs::linkClicked,
            actionUpClicked = actionUpClicked
        )
    }
}

@Composable
fun CircuitScreen(
    circuitName: String,
    list: List<CircuitModel>,
    linkClicked: (String) -> Unit,
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
                is CircuitModel.Item -> Item(
                    model = it
                )
                is CircuitModel.Stats -> Stats(
                    model = it,
                    linkClicked = linkClicked
                )
                CircuitModel.Error -> {
                    NotAvailable()
                }
                CircuitModel.Loading -> {
                    SkeletonViewList()
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
    Row(
        modifier = modifier
            .padding(
                horizontal = AppTheme.dimensions.paddingMedium,
                vertical = AppTheme.dimensions.paddingSmall
            )
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            TextBody1(
                text = model.data.name,
                bold = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 2.dp)
            )
            TextBody2(
                text = model.data.date.format(DateTimeFormatter.ofPattern("'${model.data.date.dayOfMonth.ordinalAbbreviation}' MMMM yyyy")),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 2.dp)
            )
            TextBody2(
                text = stringResource(id = R.string.weekend_race_round_season, model.data.round, model.data.season),
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
private fun Stats(
    model: CircuitModel.Stats,
    linkClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        val trackIcon = TrackLayout.getTrack(circuitId = model.circuitId)?.icon ?: R.drawable.circuit_unknown
        Row(modifier = Modifier.padding(horizontal = AppTheme.dimensions.paddingMedium)) {
            Icon(
                painter = painterResource(id = trackIcon),
                contentDescription = null,
                tint = AppTheme.colors.contentSecondary,
                modifier = modifier
                    .size(trackImageSize)
            )
        }
        Spacer(Modifier.height(AppTheme.dimensions.paddingMedium))
        Row {
            Box(
                modifier = Modifier
                    .padding(
                        top = AppTheme.dimensions.paddingMedium,
                        end = AppTheme.dimensions.paddingNSmall,
                        start = AppTheme.dimensions.paddingNSmall
                    )
            ) {
                val resourceId = when (isInPreview()) {
                    true -> R.drawable.gb
                    false -> LocalContext.current.getFlagResourceAlpha3(model.countryISO)
                }
                Image(
                    painter = painterResource(id = resourceId),
                    modifier = Modifier.size(countryBadgeSize),
                    contentDescription = null
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(
                        top = AppTheme.dimensions.paddingSmall,
                        bottom = AppTheme.dimensions.paddingSmall,
                        end = AppTheme.dimensions.paddingMedium
                    )
            ) {
                TextBody1(
                    text = model.country,
                    bold = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 2.dp)
                )
                if (model.startYear != null && model.endYear != null) {
                    TextBody2(
                        text = stringResource(id = R.string.circuit_hosted_grand_prix_from, model.numberOfGrandPrix, model.startYear, model.endYear),
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    TextBody2(
                        text = stringResource(id = R.string.circuit_hosted_grand_prix, model.numberOfGrandPrix),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        model.wikipedia?.let { wiki ->
            Link(
                icon = R.drawable.ic_details_wikipedia,
                label = R.string.details_link_wikipedia,
                url = wiki,
                linkClicked = linkClicked
            )
        }
        model.location?.let { location ->
            Link(
                icon = R.drawable.ic_details_maps,
                label = R.string.details_link_map,
                url = "geo:0,0?q=${location.lat},${location.lng} (${model.name})",
                linkClicked = linkClicked
            )
        }
    }
}

@Composable
private fun Link(
    @DrawableRes
    icon: Int,
    @StringRes
    label: Int,
    url: String,
    linkClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                onClick = { linkClicked(url) }
            )
            .padding(
                vertical = AppTheme.dimensions.paddingSmall,
                horizontal = AppTheme.dimensions.paddingMedium
            )
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = AppTheme.colors.contentSecondary
        )
        Spacer(Modifier.width(AppTheme.dimensions.paddingSmall))
        TextBody1(
            bold = true,
            modifier = Modifier.weight(1f),
            text = stringResource(id = label)
        )
        Spacer(Modifier.width(AppTheme.dimensions.paddingSmall))
        Icon(
            painter = painterResource(id = R.drawable.arrow_more),
            contentDescription = null,
            modifier = Modifier
                .size(24.dp)
                .alpha(0.4f),
            tint = AppTheme.colors.contentTertiary
        )
    }
}

@Composable
private fun Standings(
    preview: List<CircuitHistoryRaceResult>
) {
    // TODO!
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
                CircuitModel.Stats(
                    circuitId = "circuitId",
                    name = "name",
                    country = "England",
                    countryISO = "GBR",
                    numberOfGrandPrix = 2,
                    startYear = 2019,
                    endYear = 2020,
                    wikipedia = "wikipediaUrl",
                    location = null,
                ),
                CircuitModel.Item(race)
            ),
            linkClicked = { },
            actionUpClicked = { }
        )
    }
}