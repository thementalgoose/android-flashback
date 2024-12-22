package tmg.flashback.search.presentation

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import tmg.flashback.ads.ads.components.AdvertProvider

@Composable
fun SearchScreenVM(
    actionUpClicked: () -> Unit,
    windowSizeClass: WindowSizeClass,
    isRoot: (Boolean) -> Unit,
    viewModel: SearchViewModel = hiltViewModel(),
    advertProvider: AdvertProvider
) {
    if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded) {
        SearchScreenList(
            actionUpClicked = actionUpClicked,
            isRoot = isRoot,
            viewModel = viewModel
        )
    } else {
        SearchScreenTab(
            actionUpClicked = actionUpClicked,
            isRoot = isRoot,
            viewModel = viewModel
        )
    }
}