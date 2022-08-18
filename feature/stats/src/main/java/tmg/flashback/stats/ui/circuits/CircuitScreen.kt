package tmg.flashback.stats.ui.circuits

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.faltenreich.skeletonlayout.Skeleton
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import androidx.hilt.navigation.compose.hiltViewModel
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.formula1.enums.TrackLayout
import tmg.flashback.formula1.extensions.positionIcon
import tmg.flashback.formula1.model.CircuitHistoryRace
import tmg.flashback.formula1.model.CircuitHistoryRaceResult
import tmg.flashback.formula1.model.Location
import tmg.flashback.formula1.utils.getFlagResourceAlpha3
import tmg.flashback.providers.CircuitHistoryRaceProvider
import tmg.flashback.stats.R
import tmg.flashback.stats.analytics.AnalyticsConstants.analyticsCircuitId
import tmg.flashback.stats.components.DriverNumber
import tmg.flashback.stats.ui.weekend.shared.NotAvailable
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.buttons.ButtonTertiary
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextBody2
import tmg.flashback.ui.components.analytics.ScreenView
import tmg.flashback.ui.components.header.Header
import tmg.flashback.ui.components.loading.SkeletonView
import tmg.flashback.ui.components.loading.SkeletonViewList
import tmg.flashback.ui.utils.isInPreview
import tmg.utilities.extensions.ordinalAbbreviation

private val trackImageSize: Dp = 180.dp
private val resultIconSize = 16.dp
private val countryBadgeSize = 32.dp

private val driverPodiumHeightP1 = 18.dp
private val driverPodiumHeightP2 = 12.dp
private val driverPodiumHeightP3 = 6.dp

@Composable
fun CircuitScreenVM(
    circuitId: String,
    circuitName: String,
    actionUpClicked: () -> Unit
) {
    ScreenView(screenName = "Circuit Overview", args = mapOf(
        analyticsCircuitId to circuitId
    ))

    val viewModel = hiltViewModel<CircuitViewModel>()
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
            itemClicked = viewModel.inputs::itemClicked,
            linkClicked = viewModel.inputs::linkClicked,
            actionUpClicked = actionUpClicked
        )
    }
}

@Composable
fun CircuitScreen(
    circuitName: String,
    list: List<CircuitModel>,
    itemClicked: (CircuitModel.Item) -> Unit,
    linkClicked: (String) -> Unit,
    actionUpClicked: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.backgroundPrimary),
        content = {
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
                    model = it,
                    itemClicked = itemClicked
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
    itemClicked: (CircuitModel.Item) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clickable(onClick = { itemClicked(model) })
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
                text = "${model.data.season} ${model.data.name}",
                bold = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
            )
            TextBody2(
                text = stringResource(id = R.string.circuit_race_round,model.data.date.format(DateTimeFormatter.ofPattern("'${model.data.date.dayOfMonth.ordinalAbbreviation}' MMMM yyyy")), model.data.round),
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
        Standings(
            modifier = Modifier.width(130.dp),
            preview = model.data.preview
        )
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

        Row(modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = AppTheme.dimensions.paddingMedium)
        ) {
            model.wikipedia?.let { wiki ->
                ButtonTertiary(
                    text = stringResource(id = R.string.details_link_wikipedia),
                    onClick = { linkClicked(wiki) },
                    icon = R.drawable.ic_details_wikipedia
                )
                Spacer(Modifier.width(AppTheme.dimensions.paddingMedium))
            }
            model.location?.let { location ->
                ButtonTertiary(
                    text = stringResource(id = R.string.details_link_map),
                    onClick = { linkClicked("geo:0,0?q=${location.lat},${location.lng} (${model.name})") },
                    icon = R.drawable.ic_details_maps
                )
                Spacer(Modifier.width(AppTheme.dimensions.paddingMedium))
            }
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
    preview: List<CircuitHistoryRaceResult>,
    modifier: Modifier = Modifier
) {
    val first = preview.firstOrNull { it.position == 1 } ?: return
    val second = preview.firstOrNull { it.position == 2 } ?: return
    val third = preview.firstOrNull { it.position == 3 } ?: return

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Bottom
    ) {
        Column(modifier = Modifier.weight(1f)) {
            DriverCode(
                code = second.driver.code ?: second.driver.lastName.substring(0..2),
                colour = second.constructor.colour
            )
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(driverPodiumHeightP2)
                .background(AppTheme.colors.f1Podium2))
        }
        Column(modifier = Modifier.weight(1f)) {
            DriverCode(
                code = first.driver.code ?: first.driver.lastName.substring(0..2),
                colour = first.constructor.colour
            )
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(driverPodiumHeightP1)
                .background(AppTheme.colors.f1Podium1))
        }
        Column(modifier = Modifier.weight(1f)) {
            DriverCode(
                code = third.driver.code ?: third.driver.lastName.substring(0..2),
                colour = third.constructor.colour
            )
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(driverPodiumHeightP3)
                .background(AppTheme.colors.f1Podium3))
        }
    }
}

@Composable
private fun DriverCode(
    code: String,
    colour: Color
) {
    Row(Modifier
        .height(IntrinsicSize.Min)
        .padding(2.dp)
    ) {
        Box(modifier = Modifier
            .fillMaxHeight()
            .width(4.dp)
            .background(colour))
        DriverNumber(
            textAlign = TextAlign.Center,
            small = true,
            modifier = Modifier.weight(1f),
            number = code
        )
        Box(modifier = Modifier
            .fillMaxHeight()
            .width(4.dp)
            .background(Color.Transparent))
    }
}

@Composable
private fun StandingResult(
    model: CircuitHistoryRaceResult,
    modifier: Modifier = Modifier
) {
    val icon = model.position.positionIcon
    Row(modifier = modifier
        .height(IntrinsicSize.Min)
    ){
        Icon(
            modifier = Modifier.size(resultIconSize),
            painter = painterResource(id = icon),
            contentDescription = null,
            tint = if (model.position == 1) AppTheme.colors.f1Championship else AppTheme.colors.contentSecondary
        )
        Spacer(Modifier.width(AppTheme.dimensions.paddingXSmall))
        Box(modifier = Modifier
            .fillMaxHeight()
            .width(6.dp)
            .background(model.constructor.colour))
        Spacer(Modifier.width(AppTheme.dimensions.paddingXSmall))
        DriverNumber(
            modifier = Modifier.width(resultIconSize * 3),
            small = true,
            number = model.driver.code ?: model.driver.lastName.substring(0..3)
        )
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
                CircuitModel.Item(
                    circuitId = "circuitId",
                    circuitName = "name",
                    country = "country",
                    countryISO = "countryISO",
                    race
                )
            ),
            itemClicked = { },
            linkClicked = { },
            actionUpClicked = { }
        )
    }
}