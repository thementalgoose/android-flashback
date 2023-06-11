package tmg.flashback.rss.ui.configure

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import tmg.flashback.rss.repo.RssRepository
import tmg.flashback.rss.repo.model.SupportedArticleSource
import tmg.flashback.rss.usecases.AllSupportedSourcesUseCase
import tmg.flashback.rss.usecases.GetSupportedSourceUseCase
import tmg.flashback.web.usecases.OpenWebpageUseCase
import javax.inject.Inject

interface ConfigureRSSViewModelInputs {
    fun clickShowDescription(state: Boolean)
    fun addItem(rssLink: String, isChecked: Boolean)
    fun visitWebsite(article: SupportedArticleSource)
}

interface ConfigureRSSViewModelOutputs {
    val showDescriptionEnabled: StateFlow<Boolean>
    val rssSources: StateFlow<List<RSSSource>>
    val showAddCustom: StateFlow<Boolean>
}

@HiltViewModel
class ConfigureRSSViewModel @Inject constructor(
    private val repository: RssRepository,
    private val allSupportedSourcesUseCase: AllSupportedSourcesUseCase,
    private val getSupportedSourcesUseCase: GetSupportedSourceUseCase,
    private val openWebpageUseCase: OpenWebpageUseCase,
): ViewModel(), ConfigureRSSViewModelInputs, ConfigureRSSViewModelOutputs {

    val inputs: ConfigureRSSViewModelInputs = this
    val outputs: ConfigureRSSViewModelOutputs = this

    private val rssUrls: MutableSet<String>
        get() = repository.rssUrls.toMutableSet()

    override val showAddCustom: MutableStateFlow<Boolean> = MutableStateFlow<Boolean>(repository.addCustom)
    override val rssSources: MutableStateFlow<List<RSSSource>> = MutableStateFlow(emptyList())
    override val showDescriptionEnabled: MutableStateFlow<Boolean> = MutableStateFlow<Boolean>(repository.rssShowDescription)

    init {
        updateList()
    }

    override fun clickShowDescription(state: Boolean) {
        repository.rssShowDescription = state
        showDescriptionEnabled.value = state
    }

    override fun visitWebsite(article: SupportedArticleSource) {
        openWebpageUseCase.open(article.contactLink, title = "")
    }

    override fun addItem(rssLink: String, isChecked: Boolean) {
        if (isChecked) {
            repository.rssUrls = rssUrls + rssLink
        } else {
            repository.rssUrls = rssUrls - rssLink
        }
        updateList()
    }

    private fun updateList() {
        rssSources.value = mutableListOf<RSSSource>().apply {
            addAll(rssUrls
                .filter { rssLink ->
                    allSupportedSourcesUseCase.getSources().all { it.rssLink != rssLink }
                }
                .map {
                    RSSSource(
                        url = it,
                        supportedArticleSource = getSupportedSourcesUseCase.getByRssLink(it),
                        isChecked = true
                    )
                }
            )
            addAll(allSupportedSourcesUseCase
                .getSources()
                .sortedBy {
                    it.rssLink.replace("https://www.", "")
                        .replace("http://www.", "")
                        .replace("https://", "")
                        .replace("http://", "")
                }
                .map {
                    RSSSource(
                        url = it.rssLink,
                        supportedArticleSource = it,
                        isChecked = rssUrls.contains(it.rssLink)
                    )
                })
        }
    }
}