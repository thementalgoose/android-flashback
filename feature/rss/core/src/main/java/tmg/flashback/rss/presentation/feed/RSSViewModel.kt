package tmg.flashback.rss.presentation.feed

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.ads.ads.repository.AdsRepository
import tmg.flashback.device.managers.NetworkConnectivityManager
import tmg.flashback.device.managers.TimeManager
import tmg.flashback.navigation.Navigator
import tmg.flashback.rss.network.RssService
import tmg.flashback.rss.repo.RssRepository
import tmg.flashback.rss.repo.model.Article
import javax.inject.Inject

//region Inputs

interface RSSViewModelInputs {
    fun refresh()
    fun configure()
    fun back()
    fun clickArticle(article: Article)
}

//endregion

//region Outputs

interface RSSViewModelOutputs {
    val uiState: StateFlow<RSSViewModel.UiState>
    val isLoading: StateFlow<Boolean>
}

//endregion

@Suppress("EXPERIMENTAL_API_USAGE")
@HiltViewModel
class RSSViewModel @Inject constructor(
    private val rssService: RssService,
    private val rssRepository: RssRepository,
    private val adsRepository: AdsRepository,
    private val navigator: Navigator,
    private val connectivityManager: NetworkConnectivityManager,
    private val timeManager: TimeManager
): ViewModel(), RSSViewModelInputs, RSSViewModelOutputs {


    override val uiState: MutableStateFlow<UiState>
    override val isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)

    init {
        val initialState = when {
            !connectivityManager.isConnected -> UiState.NoNetwork
            rssRepository.rssUrls.isEmpty() -> UiState.SourcesDisabled
            else -> UiState.Data()
        }
        uiState = MutableStateFlow(initialState)
        if (initialState is UiState.Data) {
            refresh()
        }
    }

    // adsRepository.advertConfig.onRss

    var inputs: RSSViewModelInputs = this
    var outputs: RSSViewModelOutputs = this

    //region Inputs

    override fun refresh() {
        viewModelScope.launch {
            uiState.value = when {
                !connectivityManager.isConnected -> UiState.NoNetwork
                rssRepository.rssUrls.isEmpty() -> UiState.SourcesDisabled
                else -> {
                    isLoading.value = true
                    val response = rssService.getNews()
                    isLoading.value = false
                    if (response.isNoNetwork || response.result == null) {
                        UiState.NoNetwork
                    } else {
                        createOrUpdate {
                            this.copy(
                                lastUpdated = timeManager.now.format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                                showAdvert = adsRepository.advertConfig.onRss,
                                rssItems = response.result,
                            )
                        }
                    }
                }
            }
        }
    }

    override fun configure() {
        uiState.value = createOrUpdate {
            this.copy(opened = UiStateOpened.ConfigureSources)
        }
    }

    override fun back() {
        if (uiState.value is UiState.Data) {
            uiState.value = createOrUpdate { this.copy(opened = null) }
        }
    }

    override fun clickArticle(article: Article) {
        uiState.value = createOrUpdate {
            this.copy(
                opened = UiStateOpened.WebArticle(article)
            )
        }
    }

    private fun createOrUpdate(callback: UiState.Data.() -> UiState.Data): UiState.Data {
        return when (val x = uiState.value) {
            is UiState.Data -> callback(x)
            else -> callback(UiState.Data())
        }
    }

    //endregion

    sealed class UiState {
        data class Data(
            val lastUpdated: String? = null,
            val showAdvert: Boolean = false,
            val rssItems: List<Article> = emptyList(),
            val opened: UiStateOpened? = null,
        ): UiState()

        data object SourcesDisabled: UiState()
        data object NoNetwork: UiState()
    }

    sealed class UiStateOpened {
        data class WebArticle(val article: Article): UiStateOpened()
        data object ConfigureSources: UiStateOpened()
    }
}