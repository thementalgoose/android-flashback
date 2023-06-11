package tmg.flashback.drivers.ui.overview

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import tmg.flashback.analytics.constants.AnalyticsConstants.analyticsDriverId
import tmg.flashback.drivers.R
import tmg.flashback.drivers.contract.model.DriverStatHistoryType
import tmg.flashback.formula1.model.DriverEntry
import tmg.flashback.providers.DriverConstructorProvider
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.buttons.ButtonSecondary
import tmg.flashback.style.text.TextBody1
import tmg.flashback.ui.components.analytics.ScreenView
import tmg.flashback.ui.components.drivers.DriverImage
import tmg.flashback.ui.components.errors.NetworkError
import tmg.flashback.ui.components.flag.Flag
import tmg.flashback.ui.components.loading.SkeletonViewList
import tmg.flashback.ui.components.messages.Message
import tmg.flashback.ui.components.navigation.PipeType
import tmg.flashback.ui.components.swiperefresh.SwipeRefresh
import tmg.flashback.ui.components.timeline.Timeline
import tmg.utilities.extensions.format

private val headerImageSize: Dp = 120.dp

@Composable
fun DriverOverviewScreenVM(
    driverId: String,
    driverName: String,
    actionUpClicked: () -> Unit,
    viewModel: DriverOverviewViewModel = hiltViewModel()
) {
    ScreenView(screenName = "Driver Overview", args = mapOf(
        analyticsDriverId to driverId
    ))

    viewModel.inputs.setup(driverId, driverName)

    val list = viewModel.outputs.list.collectAsState(emptyList())
    val isLoading = viewModel.outputs.showLoading.collectAsState(false)
    SwipeRefresh(
        isLoading = isLoading.value,
        onRefresh = viewModel.inputs::refresh
    ) {
        DriverOverviewScreen(
            list = list.value,
            driverName = driverName,
            linkClicked = viewModel.inputs::openUrl,
            racedForClicked = {
                viewModel.inputs.openSeason(it.season)
            },
            statHistoryClicked = viewModel.inputs::openStatHistory,
            actionUpClicked = actionUpClicked,
        )
    }
}

@Composable
fun DriverOverviewScreen(
    actionUpClicked: () -> Unit,
    linkClicked: (String) -> Unit,
    driverName: String,
    statHistoryClicked: (DriverStatHistoryType) -> Unit,
    racedForClicked: (DriverOverviewModel.RacedFor) -> Unit,
    list: List<DriverOverviewModel>
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.backgroundPrimary),
        content = {
            item("header") {
                tmg.flashback.ui.components.header.Header(
                    text = driverName,
                    icon = painterResource(id = R.drawable.ic_back),
                    iconContentDescription = stringResource(id = R.string.ab_back),
                    actionUpClicked = actionUpClicked
                )
            }
            items(list, key = { it.key }) {
                when (it) {
                    is DriverOverviewModel.Header -> Header(
                        model = it,
                        linkClicked = linkClicked
                    )
                    is DriverOverviewModel.Message -> {
                        Message(title = stringResource(id = it.label, *it.args.toTypedArray()))
                    }
                    is DriverOverviewModel.RacedFor -> {
                        History(
                            model = it,
                            clicked = racedForClicked
                        )
                    }
                    is DriverOverviewModel.Stat -> {
                        Stat(
                            model = it,
                            statHistoryClicked = statHistoryClicked
                        )
                    }
                    DriverOverviewModel.InternalError -> {
                        NetworkError(error = NetworkError.INTERNAL_ERROR)
                    }
                    DriverOverviewModel.Loading -> {
                        SkeletonViewList()
                    }
                    DriverOverviewModel.NetworkError -> {
                        NetworkError(error = NetworkError.NETWORK_ERROR)
                    }
                }
            }
        }
    )
}

@Composable
private fun Header(
    model: DriverOverviewModel.Header,
    linkClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) { 
    Column(modifier = modifier.padding(
        horizontal = AppTheme.dimens.medium
    )) {
        DriverImage(
            photoUrl = model.driverImg,
            code = model.driverCode,
            number = model.driverNumber,
            size = headerImageSize
        )
        Row(
            modifier = Modifier
                .padding(top = 4.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Flag(
                iso = model.driverNationalityISO,
                nationality = model.driverNationality,
                modifier = Modifier
                    .size(16.dp)
                    .align(Alignment.CenterVertically),
            )
            Spacer(Modifier.width(AppTheme.dimens.small))

            val birthday = model.driverBirthday
                .format("dd MMMM yyyy")
                ?.let { birthday -> stringResource(id = R.string.driver_overview_stat_birthday, birthday) } ?: ""
            TextBody1(
                modifier = Modifier
                    .padding(vertical = AppTheme.dimens.xsmall)
                    .fillMaxWidth(),
                text = "${model.driverNationality} - $birthday"
            )
        }
        if (model.driverWikiUrl.isNotEmpty()) {
            ButtonSecondary(
                text = stringResource(id = R.string.details_link_wikipedia),
                onClick = { linkClicked(model.driverWikiUrl) },
//                icon = R.drawable.ic_details_wikipedia
            )
            Spacer(Modifier.height(AppTheme.dimens.xsmall))
        }
    }
}

@Composable
private fun Stat(
    model: DriverOverviewModel.Stat,
    statHistoryClicked: (DriverStatHistoryType) -> Unit,
    modifier: Modifier = Modifier
) {
    val initialModifier = when (model.driverStatHistoryType != null) {
        true -> modifier.clickable(onClick = {
            statHistoryClicked(model.driverStatHistoryType)
        })
        false -> modifier
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = initialModifier
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
private fun History(
    model: DriverOverviewModel.RacedFor,
    clicked: (DriverOverviewModel.RacedFor) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier
        .clickable(onClick = { clicked(model) })
        .padding(horizontal = AppTheme.dimens.medium)
    ) {
        Timeline(
            timelineColor = AppTheme.colors.contentSecondary,
            overrideDotColor = when {
                model.isChampionship -> AppTheme.colors.f1Championship
                else -> null
            },
            isEnabled = true,
            showTop = model.type.showTop,
            showBottom = model.type.showBottom
        ) {
            TextBody1(
                text = model.season.toString(),
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            )
            Column(
                modifier = Modifier.padding(vertical = 2.dp),
                horizontalAlignment = Alignment.End
            ) {
                model.constructors.forEach { constructor ->
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier
                            .height(IntrinsicSize.Min)
                    ) {
                        TextBody1(
                            text = constructor.name,
                            bold = true,
                            modifier = Modifier
                                .padding(
                                    end = AppTheme.dimens.small,
                                    top = 2.dp,
                                    bottom = 2.dp
                                )
                        )
                        Box(modifier = Modifier
                            .width(8.dp)
                            .fillMaxHeight()
                            .background(constructor.colour)
                        )
                    }
                }
            }
        }
    }
}

@PreviewTheme
@Composable
private fun Preview(
    @PreviewParameter(DriverConstructorProvider::class) driverConstructor: DriverEntry
) {
    AppThemePreview {
        DriverOverviewScreen(
            actionUpClicked = { },
            driverName = "firstName lastName",
            racedForClicked = { },
            linkClicked = { },
            statHistoryClicked = { },
            list = listOf(
                driverConstructor.toHeader(),
                fakeStat,
                fakeStatWinning,
                driverConstructor.racedFor(),
                driverConstructor.racedFor2()
            )
        )
    }
}

private val fakeStatWinning = DriverOverviewModel.Stat(
    isWinning = true,
    icon = R.drawable.ic_status_front_wing,
    label = R.string.driver_overview_stat_career_points_finishes,
    value = "12"
)
private val fakeStat = DriverOverviewModel.Stat(
    isWinning = false,
    icon = R.drawable.ic_status_battery,
    label = R.string.driver_overview_stat_career_points,
    value = "4"
)
private fun DriverEntry.racedFor() = DriverOverviewModel.RacedFor(
    season = 2022,
    type = PipeType.START,
    constructors = listOf(
        this.constructor.copy(id = "1", name = "Toro Rosso")
    ),
    isChampionship = false
)
private fun DriverEntry.racedFor2() = DriverOverviewModel.RacedFor(
    season = 2021,
    type = PipeType.END,
    constructors = listOf(
        this.constructor.copy(id = "1", name = "McLaren"),
        this.constructor.copy(id = "2", name = "Ferrari")
    ),
    isChampionship = false
)
private fun DriverEntry.toHeader(): DriverOverviewModel.Header = DriverOverviewModel.Header(
    driverId = this.driver.id,
    driverName = this.driver.name,
    driverNumber = this.driver.number,
    driverImg = this.driver.photoUrl ?: "",
    driverCode = this.driver.code,
    driverBirthday = this.driver.dateOfBirth,
    driverWikiUrl = this.driver.wikiUrl ?: "",
    driverNationalityISO = this.driver.nationalityISO,
    driverNationality = this.driver.nationality,
    constructors = listOf(
        this.constructor,
        this.constructor.copy(id = "constructor2", name = "Alpine"),
        this.constructor.copy(id = "constructor3", name = "Toro Rosso"),
        this.constructor.copy(id = "constructor4", name = "McLaren F1"),
        this.constructor.copy(id = "constructor5", name = "Williams"),
        this.constructor.copy(id = "constructor6", name = "Red Bull"),
        this.constructor.copy(id = "constructor7", name = "Ferrari"),
        this.constructor.copy(id = "constructor8", name = "Mercedes"),
    )
)