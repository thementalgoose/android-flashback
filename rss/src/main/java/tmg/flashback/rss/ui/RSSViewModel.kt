package tmg.flashback.rss.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.repo.NetworkConnectivityManager
import tmg.flashback.rss.base.RSSBaseViewModel
import tmg.flashback.rss.prefs.RSSPrefsRepository
import tmg.flashback.rss.repo.RSSRepository
import tmg.utilities.extensions.then

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
        private val RSSDB: RSSRepository,
        private val prefRepository: RSSPrefsRepository,
        private val connectivityManager: NetworkConnectivityManager
): RSSBaseViewModel(), RSSViewModelInputs,
    RSSViewModelOutputs {

    override val isRefreshing: MutableLiveData<Boolean> = MutableLiveData()
    private val refreshNews: ConflatedBroadcastChannel<Boolean> = ConflatedBroadcastChannel()
    private val newsList: Flow<List<RSSItem>> = refreshNews
        .asFlow()
        .then {
            isRefreshing.value = true
        }
        .flatMapLatest { RSSDB.getNews() }
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
            return@map listOf<RSSItem>(
                RSSItem.Message(
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")))
            ) + results
        }
        .onStart { emitAll(flow { emptyList<RSSItem>() }) }
        .then {
            isRefreshing.value = false
        }

    override val list: LiveData<List<RSSItem>> = newsList
        .asLiveData(viewModelScope.coroutineContext)

    var inputs: RSSViewModelInputs = this
    var outputs: RSSViewModelOutputs = this

    init {
        refresh()
    }

    //region Inputs

    override fun refresh() {
        refreshNews.offer(true)
    }

    //endregion
}