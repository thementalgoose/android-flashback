package tmg.flashback.rss.ui.configure

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import tmg.flashback.rss.repo.RSSRepository
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
    val showDescriptionEnabled: LiveData<Boolean>
    val rssSources: LiveData<List<RSSSource>>
    val showAddCustom: LiveData<Boolean>
}

@HiltViewModel
class ConfigureRSSViewModel @Inject constructor(
    private val repository: RSSRepository,
    private val allSupportedSourcesUseCase: AllSupportedSourcesUseCase,
    private val getSupportedSourcesUseCase: GetSupportedSourceUseCase,
    private val openWebpageUseCase: OpenWebpageUseCase,
): ViewModel(), ConfigureRSSViewModelInputs, ConfigureRSSViewModelOutputs {

    val inputs: ConfigureRSSViewModelInputs = this
    val outputs: ConfigureRSSViewModelOutputs = this

    private val rssUrls: MutableSet<String>
        get() = repository.rssUrls.toMutableSet()

    override val showAddCustom: MutableLiveData<Boolean> = MutableLiveData<Boolean>(repository.addCustom)
    override val rssSources: MutableLiveData<List<RSSSource>> = MutableLiveData(emptyList())
    override val showDescriptionEnabled: MutableLiveData<Boolean> = MutableLiveData<Boolean>(repository.rssShowDescription)

    init {
        updateList()
    }

    override fun clickShowDescription(state: Boolean) {
        repository.rssShowDescription = state
        showDescriptionEnabled.value = repository.rssShowDescription
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