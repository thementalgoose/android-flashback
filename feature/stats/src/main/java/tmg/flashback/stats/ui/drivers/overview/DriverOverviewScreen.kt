package tmg.flashback.stats.ui.drivers.overview

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.viewModel
import tmg.flashback.formula1.model.DriverConstructor
import tmg.flashback.formula1.utils.getFlagResourceAlpha3
import tmg.flashback.providers.DriverConstructorProvider
import tmg.flashback.stats.R
import tmg.flashback.stats.components.Timeline
import tmg.flashback.stats.ui.shared.DriverImage
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.text.TextBody1
import tmg.flashback.ui.components.messages.Message
import tmg.flashback.ui.utils.isInPreview
import tmg.utilities.extensions.format

private val headerImageSize: Dp = 120.dp

@Composable
fun DriverOverviewScreenVM(
    driverId: String,
    driverName: String,
    actionUpClicked: () -> Unit,
) {
    val viewModel by viewModel<DriverOverviewViewModel>()
    viewModel.inputs.setup(driverId)
}

@Composable
fun DriverOverviewScreen(
    actionUpClicked: () -> Unit,
    list: List<DriverOverviewModel>
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.backgroundPrimary),
        content = {
            items(list, key = { it.key }) {
                when (it) {
                    is DriverOverviewModel.Header -> Header(
                        model = it,
                        actionUpClicked = actionUpClicked
                    )
                    is DriverOverviewModel.Message -> {
                        Message(title = stringResource(id = it.label, it.args))
                    }
                    is DriverOverviewModel.RacedFor -> {
                        History(
                            model = it,
                            clicked = { }
                        )
                    }
                    is DriverOverviewModel.Stat -> {
                        Stat(model = it)
                    }
                    DriverOverviewModel.InternalError -> TODO()
                    DriverOverviewModel.Loading -> TODO()
                    DriverOverviewModel.NetworkError -> TODO()
                }
            }
        }
    )
}

@Composable
private fun Header(
    model: DriverOverviewModel.Header,
    actionUpClicked: () -> Unit,
    modifier: Modifier = Modifier
) { 
    Column(modifier = modifier) {
        tmg.flashback.ui.components.header.Header(
            text = model.driverName,
            icon = painterResource(id = R.drawable.ic_back),
            iconContentDescription = stringResource(id = R.string.ab_back),
            actionUpClicked = actionUpClicked
        )
        Column(modifier = Modifier.padding(
            horizontal = AppTheme.dimensions.paddingMedium
        )) {
            DriverImage(
                photoUrl = model.driverImg,
                size = headerImageSize
            )
            Row(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val resourceId = when (isInPreview()) {
                    true -> R.drawable.gb
                    false -> LocalContext.current.getFlagResourceAlpha3(model.driverNationalityISO)
                }
                Image(
                    modifier = Modifier
                        .size(16.dp)
                        .align(Alignment.CenterVertically),
                    painter = painterResource(id = resourceId),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                )
                Spacer(Modifier.width(AppTheme.dimensions.paddingSmall))
                TextBody1(
                    modifier = Modifier.fillMaxWidth(),
                    text = model.constructors.joinToString { it.name }
                )
            }

            model.driverBirthday.format("dd MMMM yyyy")?.let { birthday ->
                TextBody1(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .fillMaxWidth(),
                    text = stringResource(id = R.string.driver_overview_stat_birthday, birthday)
                )
            }

            Spacer(Modifier.height(AppTheme.dimensions.paddingSmall))
        }
    }
}

@Composable
private fun Stat(
    model: DriverOverviewModel.Stat,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(
                vertical = AppTheme.dimensions.paddingXSmall,
                horizontal = AppTheme.dimensions.paddingMedium
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
        .padding(horizontal = AppTheme.dimensions.paddingMedium)
    ) {
        Timeline(
            timelineColor = AppTheme.colors.contentSecondary,
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
                horizontalAlignment = Alignment.End
            ) {
                model.constructors.forEach { constructor ->
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier
                            .width(IntrinsicSize.Min)
                            .height(IntrinsicSize.Min)
                    ) {
                        TextBody1(
                            text = constructor.name,
                            bold = true,
                            modifier = Modifier
                                .weight(1f)
                                .padding(
                                    end = AppTheme.dimensions.paddingSmall,
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
    @PreviewParameter(DriverConstructorProvider::class) driverConstructor: DriverConstructor
) {
    AppThemePreview {
        DriverOverviewScreen(
            actionUpClicked = { },
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
private fun DriverConstructor.racedFor() = DriverOverviewModel.RacedFor(
    season = 2022,
    type = PipeType.START,
    constructors = listOf(
        this.constructor.copy(id = "1", name = "McLaren")
    ),
    isChampionship = false
)
private fun DriverConstructor.racedFor2() = DriverOverviewModel.RacedFor(
    season = 2021,
    type = PipeType.END,
    constructors = listOf(
        this.constructor.copy(id = "1", name = "McLaren"),
        this.constructor.copy(id = "2", name = "Ferrari")
    ),
    isChampionship = false
)
private fun DriverConstructor.toHeader(): DriverOverviewModel.Header = DriverOverviewModel.Header(
    driverId = this.driver.id,
    driverName = this.driver.name,
    driverNumber = this.driver.number,
    driverImg = this.driver.photoUrl ?: "",
    driverBirthday = this.driver.dateOfBirth,
    driverWikiUrl = this.driver.wikiUrl ?: "",
    driverNationalityISO = this.driver.nationalityISO,
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