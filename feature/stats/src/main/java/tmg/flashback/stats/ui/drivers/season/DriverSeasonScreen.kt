package tmg.flashback.stats.ui.drivers.season

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.viewModel
import tmg.flashback.style.AppTheme
import tmg.flashback.style.text.TextBody1
import tmg.flashback.ui.components.errors.NetworkError
import tmg.flashback.ui.components.loading.SkeletonViewList
import tmg.flashback.ui.components.messages.Message

@Composable
fun DriverSeasonScreenVM(
    driverId: String,
    driverName: String,
    season: Int,
    actionUpClicked: () -> Unit
) {
    val viewModel by viewModel<DriverSeasonViewModel>()
    viewModel.inputs.setup(driverId, season)

    val list = viewModel.outputs.list.observeAsState(listOf(DriverSeasonModel.Loading))
    DriverSeasonScreen(
        list = list.value,
        actionUpClicked = actionUpClicked
    )
}

@Composable
fun DriverSeasonScreen(
    list: List<DriverSeasonModel>,
    actionUpClicked: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.backgroundPrimary),
        content = {
            items(list, key = { it.key }) {
                when (it) {
//                    is DriverSeasonModel -> Header(
//                        model = it,
//                        actionUpClicked = actionUpClicked
//                    )
                    is DriverSeasonModel.Message -> {
                        Message(title = stringResource(id = it.label, it.args))
                    }
                    is DriverSeasonModel.RacedFor -> {
//                        History(
//                            model = it,
//                            clicked = { }
//                        )
                    }
                    is DriverSeasonModel.Stat -> {
                        Stat(model = it)
                    }
                    is DriverSeasonModel.Result -> {

                    }
                    DriverSeasonModel.InternalError -> {
                        NetworkError(error = NetworkError.INTERNAL_ERROR)
                    }
                    DriverSeasonModel.Loading -> {
                        SkeletonViewList()
                    }
                    DriverSeasonModel.NetworkError -> {
                        NetworkError(error = NetworkError.NETWORK_ERROR)
                    }
                    DriverSeasonModel.ResultHeader -> {

                    }
                }
            }
        }
    )
}

@Composable
private fun Stat(
    model: DriverSeasonModel.Stat,
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