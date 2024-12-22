package tmg.flashback.search.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import tmg.flashback.ads.ads.components.AdvertProvider

@Composable
fun SearchScreenList(
    actionUpClicked: () -> Unit,
    isRoot: (Boolean) -> Unit,
    viewModel: SearchViewModel,
    advertProvider: AdvertProvider
) {
    val uiState = viewModel.outputs.uiState.collectAsState()
    val selectedTab = uiState.value.category
}

@Composable
fun SearchScreenList(
    actionUpClicked: () -> Unit,
    isRoot: (Boolean) -> Unit,
    advertProvider: AdvertProvider,
    tabClicked: (SearchScreenStateCategory) -> Unit,
    uiState: SearchScreenState
) {

}