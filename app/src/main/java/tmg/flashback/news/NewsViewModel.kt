package tmg.flashback.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.base.BaseViewModel
import tmg.flashback.repo.db.PrefsDB
import tmg.flashback.repo.db.news.NewsDB
import tmg.flashback.repo.enums.NewsSource
import tmg.flashback.settings.ConnectivityManager
import tmg.flashback.shared.SyncDataItem
import tmg.flashback.di.async.ScopeProvider
import tmg.utilities.extensions.then

//region Inputs

interface NewsViewModelInputs {
    fun refresh()
}

//endregion

//region Outputs

interface NewsViewModelOutputs {
    val list: LiveData<List<NewsItem>>
    val isRefreshing: MutableLiveData<Boolean>
}

//endregion

@FlowPreview
@ExperimentalCoroutinesApi
class NewsViewModel(
    private val newsDB: NewsDB,
    private val prefDB: PrefsDB,
    private val connectivityManager: ConnectivityManager,
    scopeProvider: ScopeProvider
): BaseViewModel(scopeProvider), NewsViewModelInputs, NewsViewModelOutputs {

    override val isRefreshing: MutableLiveData<Boolean> = MutableLiveData()
    private val refreshNews: ConflatedBroadcastChannel<Boolean> = ConflatedBroadcastChannel()
    private val newsList: Flow<List<NewsItem>> = refreshNews
        .asFlow()
        .then {
            isRefreshing.value = true
        }
        .flatMapLatest { newsDB.getNews() }
        .map { response ->
            if (response.isNoNetwork || !connectivityManager.isConnected) {
                return@map listOf<NewsItem>(NewsItem.ErrorItem(SyncDataItem.NoNetwork))
            }
            val results = response.result?.map { NewsItem.News(it) } ?: emptyList()
            if (results.isEmpty()) {
                if (prefDB.newsSourceExcludeList.size == NewsSource.values().size) {
                    return@map listOf<NewsItem>(NewsItem.ErrorItem(SyncDataItem.AllSourcesDisabled))
                }
                else {
                    return@map listOf<NewsItem>(NewsItem.ErrorItem(SyncDataItem.InternalError))
                }
            }
            return@map listOf<NewsItem>(NewsItem.Message(
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")))
            ) + results
        }
        .onStart { emitAll(flow { emptyList<NewsItem>() }) }
        .then {
            isRefreshing.value = false
        }

    override val list: LiveData<List<NewsItem>> = newsList
        .asLiveData(scope.coroutineContext)

    var inputs: NewsViewModelInputs = this
    var outputs: NewsViewModelOutputs = this

    init {
        refresh()
    }

    //region Inputs

    override fun refresh() {
        isRefreshing.value = true
        refreshNews.offer(true)
    }

    //endregion
}