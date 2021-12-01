package tmg.flashback.rss.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.device.managers.NetworkConnectivityManager
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.BroadcastChannel
import tmg.flashback.ads.controller.AdsController
import tmg.flashback.rss.repo.RSSRepository
import tmg.flashback.rss.repo.RssAPI
import tmg.utilities.extensions.then
import tmg.utilities.lifecycle.Event
import java.util.*

//region Inputs

interface RSSViewModelInputs {
    fun refresh()
}

//endregion

//region Outputs

interface RSSViewModelOutputs {
    val list: LiveData<List<RSSItem>>
    val isRefreshing: LiveData<Boolean>
}

//endregion

@Suppress("EXPERIMENTAL_API_USAGE")
class RSSViewModel(
    private val RSSDB: RssAPI,
    private val prefRepository: RSSRepository,
    private val adsController: AdsController,
    private val connectivityManager: NetworkConnectivityManager
): ViewModel(), RSSViewModelInputs,
    RSSViewModelOutputs {

    override val isRefreshing: MutableLiveData<Boolean> = MutableLiveData()

    private var refreshingUUID: String? = null

    private val refreshNews: MutableStateFlow<String> = MutableStateFlow(UUID.randomUUID().toString())
    private val newsList: Flow<List<RSSItem>> = refreshNews
        .asStateFlow()
        .filter {
            it != refreshingUUID
        }
        .then {
            isRefreshing.value = true
        }
        .flatMapLatest {
            refreshingUUID = it
            RSSDB.getNews()
        }
        .map { response ->
            if (response.isNoNetwork || !connectivityManager.isConnected) {
                return@map listOf<RSSItem>(RSSItem.NoNetwork)
            }
            val results = response.result?.map { RSSItem.RSS(it) } ?: emptyList()
            if (results.isEmpty()) {
                if (prefRepository.rssUrls.isEmpty()) {
                    return@map listOf<RSSItem>(
                        RSSItem.SourcesDisabled)
                }
                else {
                    return@map listOf<RSSItem>(RSSItem.InternalError)
                }
            }
            return@map mutableListOf<RSSItem>().apply {
                add(RSSItem.Message(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))))
                if (adsController.advertConfig.onRss) {
                    add(RSSItem.Advert)
                }
                addAll(results)
            }
        }
        .onStart { emitAll(flow { emptyList<RSSItem>() }) }
        .then {
            isRefreshing.value = false
        }

    override val list: LiveData<List<RSSItem>> = newsList
        .shareIn(viewModelScope, SharingStarted.Lazily)
        .asLiveData(viewModelScope.coroutineContext)

    var inputs: RSSViewModelInputs = this
    var outputs: RSSViewModelOutputs = this

    //region Inputs

    override fun refresh() {
        refreshNews.value = UUID.randomUUID().toString()
    }

    //endregion
}