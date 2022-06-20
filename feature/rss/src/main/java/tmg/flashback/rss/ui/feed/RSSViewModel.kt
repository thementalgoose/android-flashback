package tmg.flashback.rss.ui.feed

import androidx.lifecycle.*
import kotlinx.coroutines.flow.*
import org.koin.dsl.koinApplication
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.RssNavigationComponent
import tmg.flashback.ads.repository.AdsRepository
import tmg.flashback.device.managers.NetworkConnectivityManager
import tmg.flashback.rss.repo.RSSRepository
import tmg.flashback.rss.repo.RssAPI
import tmg.flashback.rss.ui.settings.InitialScreen
import tmg.flashback.ui.navigation.ApplicationNavigationComponent
import tmg.utilities.extensions.then
import java.util.*

//region Inputs

interface RSSViewModelInputs {
    fun refresh()
    fun configure()
    fun settings()
    fun clickModel(model: RSSModel.RSS)
}

//endregion

//region Outputs

interface RSSViewModelOutputs {
    val list: LiveData<List<RSSModel>>
    val isRefreshing: LiveData<Boolean>
}

//endregion

@Suppress("EXPERIMENTAL_API_USAGE")
internal class RSSViewModel(
    private val RSSDB: RssAPI,
    private val rssRepository: RSSRepository,
    private val adsRepository: AdsRepository,
    private val rssNavigationComponent: RssNavigationComponent,
    private val applicationNavigationComponent: ApplicationNavigationComponent,
    private val connectivityManager: NetworkConnectivityManager
): ViewModel(), RSSViewModelInputs, RSSViewModelOutputs {

    override val isRefreshing: MutableLiveData<Boolean> = MutableLiveData()

    private var refreshingUUID: String? = null

    private val refreshNews: MutableStateFlow<String> = MutableStateFlow(UUID.randomUUID().toString())
    private val newsList: Flow<List<RSSModel>> = refreshNews
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
                return@map listOf<RSSModel>(RSSModel.NoNetwork)
            }
            val results = response.result?.map { RSSModel.RSS(it) } ?: emptyList()
            if (results.isEmpty()) {
                if (rssRepository.rssUrls.isEmpty()) {
                    return@map listOf<RSSModel>(
                        RSSModel.SourcesDisabled
                    )
                }
                else {
                    return@map listOf<RSSModel>(RSSModel.InternalError)
                }
            }
            return@map mutableListOf<RSSModel>().apply {
                add(
                    RSSModel.Message(
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))
                    )
                )
                if (adsRepository.advertConfig.onRss) {
                    add(RSSModel.Advert)
                }
                addAll(results)
            }
        }
        .onStart { emitAll(flow { emptyList<RSSModel>() }) }
        .then {
            isRefreshing.value = false
        }

    override val list: LiveData<List<RSSModel>> = newsList
        .shareIn(viewModelScope, SharingStarted.Lazily)
        .asLiveData(viewModelScope.coroutineContext)

    var inputs: RSSViewModelInputs = this
    var outputs: RSSViewModelOutputs = this

    //region Inputs

    override fun refresh() {
        refreshNews.value = UUID.randomUUID().toString()
    }

    override fun configure() {
        rssNavigationComponent.configureRSS()
    }

    override fun settings() {
        rssNavigationComponent.settingsRSS()
    }

    override fun clickModel(model: RSSModel.RSS) {
        if (rssRepository.newsOpenInExternalBrowser) {
            applicationNavigationComponent.openUrl(model.item.link)
        } else {
            rssNavigationComponent.web(model.item.title, model.item.link)
        }
    }

    //endregion
}