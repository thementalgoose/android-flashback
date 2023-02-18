package tmg.flashback.stats.ui.constructors.overview

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import tmg.flashback.formula1.constants.Formula1
import tmg.flashback.formula1.extensions.pointsDisplay
import tmg.flashback.formula1.model.ConstructorHistorySeasonDriver
import tmg.flashback.formula1.model.DriverConstructor
import tmg.flashback.providers.DriverConstructorProvider
import tmg.flashback.stats.R
import tmg.flashback.stats.analytics.AnalyticsConstants.analyticsConstructorId
import tmg.flashback.stats.components.TimelineTop
import tmg.flashback.stats.components.dotDiameter
import tmg.flashback.stats.components.heightOfTopDot
import tmg.flashback.stats.ui.shared.Flag
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.buttons.ButtonTertiary
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextBody2
import tmg.flashback.style.text.TextCaption
import tmg.flashback.ui.components.analytics.ScreenView
import tmg.flashback.ui.components.errors.NetworkError
import tmg.flashback.ui.components.header.Header
import tmg.flashback.ui.components.loading.SkeletonViewList
import tmg.flashback.ui.components.navigation.PipeType
import tmg.flashback.ui.components.swiperefresh.SwipeRefresh
import tmg.utilities.extensions.ordinalAbbreviation


private val pointsWidth: Dp = 64.dp
private val positionWidth: Dp = 64.dp


@Composable
fun ConstructorOverviewScreenVM(
    constructorId: String,
    constructorName: String,
    actionUpClicked: () -> Unit,
    viewModel: ConstructorOverviewViewModel = hiltViewModel()
) {
    ScreenView(screenName = "Constructor Overview", args = mapOf(
        analyticsConstructorId to constructorId
    ))

    viewModel.inputs.setup(constructorId, constructorName)

    val list = viewModel.outputs.list.observeAsState(emptyList())
    val isLoading = viewModel.outputs.showLoading.observeAsState(false)
    SwipeRefresh(
        isLoading = isLoading.value,
        onRefresh = viewModel.inputs::refresh
    ) {
        ConstructorOverviewScreen(
            list = list.value,
            constructorName = constructorName,
            seasonClicked = viewModel.inputs::openSeason,
            actionUpClicked = actionUpClicked,
            linkClicked = viewModel.inputs::openUrl
        )
    }
}

@Composable
fun ConstructorOverviewScreen(
    list: List<ConstructorOverviewModel>,
    constructorName: String,
    seasonClicked: (Int) -> Unit,
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
                    text = constructorName,
                    icon = painterResource(id = R.drawable.ic_back),
                    iconContentDescription = stringResource(id = R.string.ab_back),
                    actionUpClicked = actionUpClicked
                )
            }
            items(list, key = { it.key }) {
                when (it) {
                    is ConstructorOverviewModel.Header -> {
                        HeaderTop(
                            model = it,
                            wikipediaClicked = linkClicked
                        )
                    }
                    is ConstructorOverviewModel.History -> {
                        History(model = it, seasonClicked = seasonClicked)
                    }
                    is ConstructorOverviewModel.Message -> {
                        tmg.flashback.ui.components.messages.Message(title = stringResource(id = it.label, *it.args.toTypedArray()))
                    }
                    is ConstructorOverviewModel.Stat -> {
                        Stat(model = it)
                    }
                    ConstructorOverviewModel.InternalError -> {
                        NetworkError(error = NetworkError.INTERNAL_ERROR)
                    }
                    ConstructorOverviewModel.ListHeader -> {
                        HistoryHeader()
                    }
                    ConstructorOverviewModel.Loading -> {
                        SkeletonViewList()
                    }
                    ConstructorOverviewModel.NetworkError -> {
                        NetworkError(error = NetworkError.NETWORK_ERROR)
                    }
                }
            }
        }
    )
}

@Composable
private fun HeaderTop(
    model: ConstructorOverviewModel.Header,
    wikipediaClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier
        .fillMaxWidth()
        .padding(horizontal = AppTheme.dimens.medium)
    ) {
        if (model.constructorPhotoUrl == null) {
            Flag(
                iso = model.constructorNationalityISO,
                nationality = model.constructorNationality,
                modifier = Modifier.size(108.dp)
            )
        } else {
            Box(modifier = Modifier
                .size(108.dp)
                .clip(RoundedCornerShape(AppTheme.dimens.radiusSmall))
                .background(AppTheme.colors.backgroundSecondary)
            ) {
                AsyncImage(
                    model = model.constructorPhotoUrl,
                    contentDescription = stringResource(id = R.string.ab_constructor_logo, model.constructorName),
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            model.constructorWikiUrl?.let { wiki ->
                ButtonTertiary(
                    text = stringResource(id = R.string.details_link_wikipedia),
                    onClick = { wikipediaClicked(wiki) },
                    icon = R.drawable.ic_details_wikipedia
                )
            }
            if (model.constructorPhotoUrl != null) {
                Spacer(Modifier.width(8.dp))
                Flag(
                    iso = model.constructorNationalityISO,
                    nationality = model.constructorNationality,
                    modifier = Modifier
                        .size(32.dp)
                        .align(Alignment.CenterVertically)
                )
            }
        }

    }
}

@Composable
private fun Stat(
    model: ConstructorOverviewModel.Stat,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(
                vertical = AppTheme.dimens.xsmall,
                horizontal = AppTheme.dimens.medium
            )
    ) {
        Icon(
            painter = painterResource(id = model.icon),
            contentDescription = null,
            tint = when (model.isWinning) {
                true -> AppTheme.colors.f1Championship
                false -> AppTheme.colors.contentSecondary
            }
        )
        Spacer(Modifier.width(8.dp))
        TextBody1(
            text = stringResource(id = model.label),
            modifier = Modifier.weight(1f)
        )
        Spacer(Modifier.width(8.dp))
        TextBody1(
            text = model.value,
            bold = true
        )
    }
}

@Composable
private fun HistoryHeader(
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier
        .padding(top = AppTheme.dimens.small)
    ) {
        Box(Modifier.weight(1f))
        Box(modifier = Modifier.width(pointsWidth)) {
            Icon(
                modifier = Modifier.align(Alignment.Center),
                painter = painterResource(id = R.drawable.ic_race_points),
                contentDescription = null,
                tint = AppTheme.colors.contentSecondary
            )
        }
        Box(modifier = Modifier.width(positionWidth)) {
            Icon(
                modifier = Modifier.align(Alignment.Center),
                painter = painterResource(id = R.drawable.ic_race_finishes),
                contentDescription = null,
                tint = AppTheme.colors.contentSecondary
            )
        }
    }
}

@Composable
private fun History(
    model: ConstructorOverviewModel.History,
    seasonClicked: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier
        .height(IntrinsicSize.Min)
        .clickable(
            onClick = { seasonClicked.invoke(model.season) }
        )
    ) {
        Box(modifier = Modifier
            .width(AppTheme.dimens.medium)
            .height(dotDiameter)
            .offset(y = heightOfTopDot)
        ) {
            if (model.season == Formula1.currentSeasonYear) {
                Icon(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .alpha(0.4f),
                    painter = painterResource(id = R.drawable.ic_current_indicator),
                    contentDescription = null,
                    tint = AppTheme.colors.contentPrimary
                )
            }
        }
        TimelineTop(
            modifier = modifier
                .fillMaxWidth()
                .padding(start = AppTheme.dimens.medium),
            timelineColor = AppTheme.colors.contentTertiary,
            overrideDotColor = when {
                !model.isInProgress && model.championshipPosition == 1 -> AppTheme.colors.f1Championship
                else -> null
            },
            isEnabled = true,
            showTop = model.pipe.showTop,
            showBottom = model.pipe.showBottom
        ) {
            Column(Modifier.padding(vertical = 8.dp)) {
                Row(modifier = modifier) {
                    Column(Modifier.weight(1f)) {
                        TextBody1(
                            text = model.season.toString(),
                            bold = true,
                            modifier = Modifier.padding(bottom = 2.dp)
                        )
                        if (model.isInProgress) {
                            TextCaption(
                                modifier = Modifier.padding(bottom = 2.dp),
                                text = stringResource(id = R.string.constructor_overview_in_progress)
                            )
                        }
                    }
                    Box(modifier = Modifier.width(pointsWidth)) {
                        TextBody1(
                            modifier = Modifier.align(Alignment.Center),
                            text = model.points.pointsDisplay(),
                            bold = true,
                            textAlign = TextAlign.Center
                        )
                    }
                    Box(modifier = Modifier.width(positionWidth)) {
                        TextBody1(
                            modifier = Modifier.align(Alignment.Center),
                            text = model.championshipPosition?.ordinalAbbreviation ?: "-",
                            bold = true,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(Modifier.height(8.dp))
                model.drivers.forEach {
                    DriverPerSeason(model = it, isInProgress = model.isInProgress)
                }
            }
        }
    }
}

@Composable
private fun DriverPerSeason(
    model: ConstructorHistorySeasonDriver,
    isInProgress: Boolean,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Row(Modifier.weight(1f)) {
            Flag(
                iso = model.driver.driver.nationalityISO,
                nationality = model.driver.driver.nationality,
                modifier = Modifier
                    .size(16.dp)
                    .align(Alignment.CenterVertically),
            )
            val textColor = if (!isInProgress && model.championshipStanding == 1) {
                AppTheme.colors.f1Championship
            } else {
                null
            }
            TextBody2(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 4.dp),
                textColor = textColor,
                text = model.driver.driver.name
            )
        }
        TextBody2(
            modifier = Modifier.width(pointsWidth),
            text = model.points.pointsDisplay(),
            textAlign = TextAlign.Center,
        )
        TextBody2(
            modifier = Modifier.width(positionWidth),
            text = model.championshipStanding?.ordinalAbbreviation ?: "-",
            textAlign = TextAlign.Center
        )
    }
}

@PreviewTheme
@Composable
private fun Preview(
    @PreviewParameter(DriverConstructorProvider::class) driverConstructor: DriverConstructor
) {
    AppThemePreview {
        ConstructorOverviewScreen(
            actionUpClicked = { },
            constructorName = "name",
            linkClicked = { },
            seasonClicked = { },
            list = listOf(
                fakeStat,
                fakeStatWinning,
                ConstructorOverviewModel.ListHeader,
                driverConstructor.history1(),
                driverConstructor.history2(),
                driverConstructor.history3(),
            )
        )
    }
}

private val fakeStatWinning = ConstructorOverviewModel.Stat(
    isWinning = true,
    icon = R.drawable.ic_status_front_wing,
    label = R.string.driver_overview_stat_career_points_finishes,
    value = "12"
)
private val fakeStat = ConstructorOverviewModel.Stat(
    isWinning = false,
    icon = R.drawable.ic_status_battery,
    label = R.string.driver_overview_stat_career_points,
    value = "4"
)
private fun DriverConstructor.history1() = ConstructorOverviewModel.History(
    season = 2022,
    pipe = PipeType.START,
    isInProgress = true,
    championshipPosition = 1,
    points = 123.5,
    drivers = listOf(
        this.driver(),
        this.driver()
    )
)

private fun DriverConstructor.history2() = ConstructorOverviewModel.History(
    season = 2021,
    pipe = PipeType.START_END,
    isInProgress = false,
    championshipPosition = 2,
    points = 123.5,
    drivers = listOf(
        this.driver(),
        this.driver()
    )
)

private fun DriverConstructor.history3() = ConstructorOverviewModel.History(
    season = 2020,
    pipe = PipeType.END,
    isInProgress = false,
    championshipPosition = 1,
    points = 45.0,
    drivers = listOf(
        this.driver(),
        this.driver()
    )
)

private fun DriverConstructor.driver() = ConstructorHistorySeasonDriver(
    driver = this,
    points = 1.0,
    wins = 1,
    races = 3,
    podiums = 4,
    finishesInPoints = 4,
    polePosition = 4,
    championshipStanding = 3
)