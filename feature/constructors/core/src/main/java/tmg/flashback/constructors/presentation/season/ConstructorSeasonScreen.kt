package tmg.flashback.constructors.presentation.season

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import tmg.flashback.googleanalytics.constants.AnalyticsConstants.analyticsConstructorId
import tmg.flashback.googleanalytics.constants.AnalyticsConstants.analyticsSeason
import tmg.flashback.constructors.R
import tmg.flashback.formula1.extensions.pointsDisplay
import tmg.flashback.formula1.model.ConstructorHistorySeasonDriver
import tmg.flashback.formula1.model.DriverEntry
import tmg.flashback.providers.DriverConstructorProvider
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.buttons.ButtonSecondary
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextBody2
import tmg.flashback.style.text.TextTitle
import tmg.flashback.googleanalytics.presentation.ScreenView
import tmg.flashback.ui.components.drivers.DriverIcon
import tmg.flashback.ui.components.errors.NetworkError
import tmg.flashback.ui.components.flag.Flag
import tmg.flashback.ui.components.header.Header
import tmg.flashback.ui.components.header.HeaderAction
import tmg.flashback.ui.components.loading.SkeletonViewList
import tmg.flashback.ui.components.messages.Message
import tmg.flashback.ui.components.swiperefresh.SwipeRefresh
import tmg.flashback.ui.foldables.isWidthExpanded
import tmg.utilities.extensions.ordinalAbbreviation

@Composable
fun ConstructorSeasonScreenVM(
    actionUpClicked: () -> Unit,
    windowSizeClass: WindowSizeClass,
    constructorId: String,
    constructorName: String,
    season: Int,
    showHeader: Boolean = true,
    viewModel: ConstructorSeasonViewModel = hiltViewModel()
) {
    ScreenView(screenName = "Constructor Season", args = mapOf(
        analyticsConstructorId to constructorId,
        analyticsSeason to season.toString()
    ))

    viewModel.inputs.setup(constructorId, season)

    val list = viewModel.outputs.list.collectAsState(emptyList())
    val isLoading = viewModel.outputs.showLoading.collectAsState(false)
    SwipeRefresh(
        isLoading = isLoading.value,
        onRefresh = viewModel.inputs::refresh
    ) {
        ConstructorSeasonScreen(
            actionUpClicked = actionUpClicked,
            windowSizeClass = windowSizeClass,
            list = list.value,
            constructorName = constructorName,
            season = season,
            showHeader = showHeader,
            driverClicked = viewModel.inputs::driverClicked,
            linkClicked = viewModel.inputs::openUrl
        )
    }
}

@Composable
fun ConstructorSeasonScreen(
    actionUpClicked: () -> Unit,
    windowSizeClass: WindowSizeClass,
    list: List<ConstructorSeasonModel>,
    constructorName: String,
    season: Int,
    showHeader: Boolean,
    driverClicked: (ConstructorSeasonModel.Driver, Int) -> Unit,
    linkClicked: (String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.backgroundPrimary),
        content = {
            item(key = "header") {
                Header(
                    text = "${constructorName}\n${season}",
                    action = when (windowSizeClass.isWidthExpanded) {
                        false -> HeaderAction.BACK
                        true -> null
                    },
                    actionUpClicked = actionUpClicked
                )
            }
            items(list, key = { it.key }) {
                when (it) {
                    is ConstructorSeasonModel.Header -> {
                        if (showHeader) {
                            HeaderTop(
                                model = it,
                                wikipediaClicked = linkClicked
                            )
                        }
                    }
                    is ConstructorSeasonModel.Driver -> {
                        DriverSummary(
                            model = it,
                            driverClicked = driverClicked,
                            season = season,
                        )
                    }
                    is ConstructorSeasonModel.Message -> {
                        Message(title = stringResource(id = it.label, *it.args.toTypedArray()))
                    }
                    is ConstructorSeasonModel.Stat -> {
                        Stat(model = it)
                    }
                    ConstructorSeasonModel.InternalError -> {
                        NetworkError(error = NetworkError.INTERNAL_ERROR)
                    }
                    ConstructorSeasonModel.Loading -> {
                        SkeletonViewList()
                    }
                    ConstructorSeasonModel.NetworkError -> {
                        NetworkError(error = NetworkError.NETWORK_ERROR)
                    }
                }
            }
        }
    )
}


@Composable
private fun HeaderTop(
    model: ConstructorSeasonModel.Header,
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
                ButtonSecondary(
                    text = stringResource(id = R.string.details_link_wikipedia),
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
    model: ConstructorSeasonModel.Stat,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .semantics(mergeDescendants = true) { }
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
private fun DriverSummary(
    model: ConstructorSeasonModel.Driver,
    driverClicked: (ConstructorSeasonModel.Driver, Int) -> Unit,
    season: Int,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier
        .clickable {
            driverClicked(model, season)
        }
        .padding(
            top = AppTheme.dimens.large,
            bottom = AppTheme.dimens.small,
            start = AppTheme.dimens.medium,
            end = AppTheme.dimens.medium
        )
    ) {
        DriverIcon(
            photoUrl = model.data.driver.driver.photoUrl,
            number = model.data.driver.driver.number,
            constructorColor = model.data.driver.constructor.colour,
            code = model.data.driver.driver.code,
            size = 96.dp
        )
        Column(
            modifier = Modifier.padding(start = AppTheme.dimens.small)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                TextTitle(
                    text = model.data.driver.driver.name,
                    bold = true
                )
                Spacer(Modifier.width(4.dp))
                Flag(iso = model.data.driver.driver.nationalityISO)
            }
            Spacer(Modifier.height(8.dp))
            SummaryCell(
                label = R.string.constructor_overview_stat_championship_standing,
                value = model.data.championshipStanding?.ordinalAbbreviation ?: ""
            )
            SummaryCell(
                label = R.string.constructor_overview_stat_race_wins,
                value = model.data.wins.toString()
            )
            SummaryCell(
                label = R.string.constructor_overview_stat_race_podiums,
                value = model.data.podiums.toString()
            )
            SummaryCell(
                label = R.string.constructor_overview_stat_qualifying_poles,
                value = model.data.polePosition.toString()
            )
            SummaryCell(
                label = R.string.constructor_overview_stat_points,
                value = model.data.points.pointsDisplay()
            )
            SummaryCell(
                label = R.string.constructor_overview_stat_points_finishes,
                value = model.data.finishesInPoints.toString()
            )
        }
    }
}

@Composable
private fun SummaryCell(
    @StringRes label: Int,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(vertical = AppTheme.dimens.xxsmall),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextBody2(text = stringResource(id = label))
        Spacer(Modifier.weight(1f))
        TextBody1(text = value, bold = true)
    }
}


@PreviewTheme
@Composable
private fun Preview(
    @PreviewParameter(DriverConstructorProvider::class) driverConstructor: DriverEntry
) {
    AppThemePreview {
        ConstructorSeasonScreen(
            actionUpClicked = { },
            constructorName = "name",
            season = 2020,
            linkClicked = { },
            driverClicked = { _, _ -> },
            windowSizeClass = WindowSizeClass.calculateFromSize(DpSize.Unspecified),
            showHeader = true,
            list = listOf(
                fakeStat,
                fakeStatWinning,
                driverConstructor.driverSummary(),
            )
        )
    }
}

private val fakeStatWinning = ConstructorSeasonModel.Stat(
    isWinning = true,
    icon = R.drawable.ic_status_front_wing,
    label = R.string.driver_overview_stat_career_points_finishes,
    value = "12"
)
private val fakeStat = ConstructorSeasonModel.Stat(
    isWinning = false,
    icon = R.drawable.ic_status_battery,
    label = R.string.driver_overview_stat_career_points,
    value = "4"
)

private fun DriverEntry.driverSummary() =
    ConstructorSeasonModel.Driver(
        this.driver()
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