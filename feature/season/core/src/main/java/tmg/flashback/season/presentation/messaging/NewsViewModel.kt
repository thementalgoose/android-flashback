package tmg.flashback.season.presentation.messaging

import androidx.compose.ui.util.fastFlatMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import tmg.flashback.device.managers.NetworkConnectivityManager
import tmg.flashback.flashbacknews.api.usecases.GetNewsUseCase
import tmg.flashback.season.BuildConfig
import tmg.flashback.web.usecases.OpenWebpageUseCase
import tmg.utilities.extensions.toLocalDate
import javax.inject.Inject

interface NewsViewModelInputs {
    fun refresh(background: Boolean)
    fun itemClicked(url: String)
}

interface NewsViewModelOutputs {
    val uiState: StateFlow<NewsUiState>
}

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val getNewsUseCase: GetNewsUseCase,
    private val openWebpageUseCase: OpenWebpageUseCase,
    private val ioDispatcher: CoroutineDispatcher,
    private val networkConnectivityManager: NetworkConnectivityManager
): ViewModel(), NewsViewModelInputs, NewsViewModelOutputs {

    val inputs: NewsViewModelInputs = this
    val outputs: NewsViewModelOutputs = this

    override val uiState: MutableStateFlow<NewsUiState> = MutableStateFlow(if (networkConnectivityManager.isConnected) NewsUiState.Loading else NewsUiState.NoNews)

    init {
        refresh(background = false)
    }

    override fun refresh(background: Boolean) {
        if (networkConnectivityManager.isConnected) {
            viewModelScope.launch(ioDispatcher) {
                if (!background) {
                    uiState.value = NewsUiState.Loading
                }
                val news = getNewsUseCase.getNews()
                uiState.value = when {
                    background && news.isNullOrEmpty() -> uiState.value
                    !background && news.isNullOrEmpty() -> NewsUiState.NoNews
                    else -> {
                        val items = news.orEmpty()
                            .sortedByDescending { it.dateAdded }
                            .groupBy { it.dateAdded.toLocalDate("yyyy-MM-dd") ?: LocalDate.now() }
                            .toList()
                        NewsUiState.News(items)
                    }
                }
            }
        }
    }

    override fun itemClicked(url: String) {
        openWebpageUseCase.open(url, "")
    }
}

sealed class NewsUiState {
    data object Loading: NewsUiState()
    data object NoNews: NewsUiState()
    data class News(
        val items: List<Pair<LocalDate, List<tmg.flashback.flashbacknews.api.models.news.News>>>
    ): NewsUiState()
}