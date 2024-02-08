package tmg.flashback.season.presentation.messaging

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import tmg.flashback.style.text.TextBody1

@Composable
fun News(
    modifier: Modifier = Modifier,
    viewModel: NewsViewModel = hiltViewModel<NewsViewModel>()
) {

    val uiState = viewModel.outputs.uiState.collectAsState()
    when (val state = uiState.value) {
        NewsUiState.Loading -> {
            TextBody1(text = "Loading")
        }
        is NewsUiState.News -> {
            TextBody1(text = "News! ${state.items}")
        }
        NewsUiState.NoNews -> {
            TextBody1(text = "No news")
        }
    }
}