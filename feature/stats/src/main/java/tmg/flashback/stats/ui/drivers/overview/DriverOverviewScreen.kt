package tmg.flashback.stats.ui.drivers.overview

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import org.threeten.bp.LocalDate
import tmg.flashback.formula1.model.Constructor
import tmg.flashback.formula1.model.DriverConstructor
import tmg.flashback.formula1.utils.getFlagResourceAlpha3
import tmg.flashback.providers.DriverConstructorProvider
import tmg.flashback.stats.R
import tmg.flashback.stats.ui.shared.DriverImage
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.text.TextBody1
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
                    is DriverOverviewModel.Message -> TODO()
                    is DriverOverviewModel.RacedFor -> TODO()
                    is DriverOverviewModel.Stat -> TODO()
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
                driverConstructor.toHeader()
            )
        )
    }
}

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
    )
)