package tmg.flashback.season.presentation.messaging

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import tmg.flashback.flashbacknews.api.usecases.GetNewsUseCase
import javax.inject.Inject

interface NewsViewModelInputs {
    fun refresh(background: Boolean)
}

interface NewsViewModelOutputs {
    val uiState: StateFlow<NewsUiState>
}

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val getNewsUseCase: GetNewsUseCase,
    private val ioDispatcher: CoroutineDispatcher
): ViewModel(), NewsViewModelInputs, NewsViewModelOutputs {

    val inputs: NewsViewModelInputs = this
    val outputs: NewsViewModelOutputs = this

    override val uiState: MutableStateFlow<NewsUiState> = MutableStateFlow(NewsUiState.Loading)

    init {
        refresh(background = false)
    }

    override fun refresh(background: Boolean) {
        viewModelScope.launch(ioDispatcher) {
            if (!background) {
                uiState.value = NewsUiState.Loading
            }
            val news = getNewsUseCase.getNews()
            uiState.value = when {
                background && news.isNullOrEmpty() -> uiState.value
                !background && news.isNullOrEmpty() -> NewsUiState.NoNews
                else -> NewsUiState.News(news ?: emptyList())
            }
        }
    }
}

sealed class NewsUiState {
    data object Loading: NewsUiState()
    data object NoNews: NewsUiState()
    data class News(
        val items: List<tmg.flashback.flashbacknews.api.models.news.News>
    ): NewsUiState()
}