package tmg.flashback.constructors.presentation.overview

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import tmg.flashback.constructors.presentation.season.ConstructorSeasonScreenVM
import tmg.flashback.formula1.R.drawable
import tmg.flashback.formula1.constants.Formula1
import tmg.flashback.formula1.extensions.pointsDisplay
import tmg.flashback.formula1.model.ConstructorHistorySeasonDriver
import tmg.flashback.formula1.model.DriverEntry
import tmg.flashback.googleanalytics.constants.AnalyticsConstants.analyticsConstructorId
import tmg.flashback.googleanalytics.presentation.ScreenView
import tmg.flashback.providers.DriverConstructorProvider
import tmg.flashback.strings.R.string
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.buttons.ButtonSecondary
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextBody2
import tmg.flashback.style.text.TextCaption
import tmg.flashback.ui.components.errors.NetworkError
import tmg.flashback.ui.components.flag.Flag
import tmg.flashback.ui.components.header.Header
import tmg.flashback.ui.components.header.HeaderAction
import tmg.flashback.ui.components.layouts.MasterDetailsPane
import tmg.flashback.ui.components.loading.SkeletonViewList
import tmg.flashback.ui.components.navigation.PipeType
import tmg.flashback.ui.components.swiperefresh.SwipeRefresh
import tmg.flashback.ui.components.timeline.TimelineTop
import tmg.flashback.ui.components.timeline.dotDiameter
import tmg.flashback.ui.components.timeline.heightOfTopDot
import tmg.utilities.extensions.ordinalAbbreviation


private val pointsWidth: Dp = 64.dp
private val positionWidth: Dp = 64.dp


@Composable
fun ConstructorOverviewScreenVM(
    paddingValues: PaddingValues,
    actionUpClicked: () -> Unit,
    windowSizeClass: WindowSizeClass,
    constructorId: String,
    constructorName: String,
    viewModel: ConstructorOverviewViewModel = hiltViewModel()
) {
    ScreenView(screenName = "Constructor Overview", args = mapOf(
        analyticsConstructorId to constructorId
    ))

    val uiState = viewModel.outputs.uiState.collectAsState()
    MasterDetailsPane(
        windowSizeClass = windowSizeClass,
        master = {
            SwipeRefresh(
                isLoading = uiState.value.isLoading,
                onRefresh = viewModel.inputs::refresh
            ) {
                ConstructorOverviewScreen(
                    actionUpClicked = actionUpClicked,
                    windowSizeClass = windowSizeClass,
                    paddingValues = paddingValues,
                    uiState = uiState.value,
                    constructorName = constructorName,
                    seasonClicked = viewModel.inputs::openSeason,
                    linkClicked = viewModel.inputs::openUrl
                )
            }
        },
        detailsShow = uiState.value.selectedSeason != null,
        detailsActionUpClicked = viewModel.inputs::back,
        details = {
            ConstructorSeasonScreenVM(
                actionUpClicked = viewModel.inputs::back,
                windowSizeClass = windowSizeClass,
                paddingValues = paddingValues,
                constructorId = constructorId,
                constructorName = constructorName,
                showHeader = false,
                season = uiState.value.selectedSeason!!
            )
        })
}

@Composable
fun ConstructorOverviewScreen(
    actionUpClicked: () -> Unit,
    windowSizeClass: WindowSizeClass,
    paddingValues: PaddingValues,
    uiState: ConstructorOverviewScreenState,
    constructorName: String,
    seasonClicked: (Int) -> Unit,
    linkClicked: (String) -> Unit,
) {
    LazyColumn(
        contentPadding = paddingValues,
        content = {
            item(key = "header") {
                Header(
                    text = constructorName,
                    action = if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) HeaderAction.BACK else null,
                    actionUpClicked = actionUpClicked
                )
            }
            if (uiState.list.isEmpty()) {
                if (!uiState.networkAvailable) {
                    item(key = "network") {
                        NetworkError()
                    }
                } else if (uiState.isLoading) {
                    item(key = "loading") {
                        SkeletonViewList()
                    }
                }
            }
            items(uiState.list, key = { it.key }) {
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
                    ConstructorOverviewModel.ListHeader -> {
                        HistoryHeader()
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
                    contentDescription = stringResource(id = string.ab_constructor_logo, model.constructorName),
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Row(modifier = Modifier
            .height(IntrinsicSize.Min)
            .padding(top = AppTheme.dimens.xsmall)
        ) {
            model.constructorWikiUrl?.let { wiki ->
                ButtonSecondary(
                    text = stringResource(id = string.details_link_wikipedia),
                    onClick = { wikipediaClicked(wiki) },
//                    icon = R.drawable.ic_details_wikipedia
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
            .semantics(mergeDescendants = true) { }
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
                painter = painterResource(id = drawable.ic_race_points),
                contentDescription = null,
                tint = AppTheme.colors.contentSecondary
            )
        }
        Box(modifier = Modifier.width(positionWidth)) {
            Icon(
                modifier = Modifier.align(Alignment.Center),
                painter = painterResource(id = drawable.ic_race_finishes),
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
                    painter = painterResource(id = tmg.flashback.ui.R.drawable.ic_current_indicator),
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
                                text = stringResource(id = string.constructor_overview_in_progress)
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
    @PreviewParameter(DriverConstructorProvider::class) driverConstructor: DriverEntry
) {
    AppThemePreview {
        ConstructorOverviewScreen(
            actionUpClicked = { },
            windowSizeClass = WindowSizeClass.calculateFromSize(DpSize.Unspecified),
            paddingValues = PaddingValues.Absolute(),
            constructorName = "name",
            linkClicked = { },
            seasonClicked = { },
            uiState = ConstructorOverviewScreenState(
                constructorId = "constructorId",
                constructorName = "name",
                list = listOf(
                    fakeStat,
                    fakeStatWinning,
                    ConstructorOverviewModel.ListHeader,
                    driverConstructor.history1(),
                    driverConstructor.history2(),
                    driverConstructor.history3(),
                )
            )
        )
    }
}

private val fakeStatWinning = ConstructorOverviewModel.Stat(
    isWinning = true,
    icon = drawable.ic_status_front_wing,
    label = string.driver_overview_stat_career_points_finishes,
    value = "12"
)
private val fakeStat = ConstructorOverviewModel.Stat(
    isWinning = false,
    icon = drawable.ic_status_battery,
    label = string.driver_overview_stat_career_points,
    value = "4"
)
private fun DriverEntry.history1() =
    ConstructorOverviewModel.History(
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

private fun DriverEntry.history2() =
    ConstructorOverviewModel.History(
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

private fun DriverEntry.history3() =
    ConstructorOverviewModel.History(
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

private fun DriverEntry.driver() = ConstructorHistorySeasonDriver(
    driver = this,
    points = 1.0,
    wins = 1,
    races = 3,
    podiums = 4,
    finishesInPoints = 4,
    polePosition = 4,
    championshipStanding = 3
)