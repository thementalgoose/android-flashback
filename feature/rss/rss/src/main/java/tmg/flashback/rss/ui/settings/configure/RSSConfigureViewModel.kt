package tmg.flashback.rss.ui.settings.configure

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tmg.flashback.rss.R
import tmg.flashback.rss.controllers.RSSFeedController
import tmg.flashback.rss.repo.RSSRepository
import tmg.flashback.rss.repo.model.SupportedArticleSource
import tmg.utilities.lifecycle.DataEvent

//region Inputs

interface RSSConfigureViewModelInputs {
    fun addQuickItem(supportedArticle: SupportedArticleSource)
    fun visitWebsite(supportedArticle: SupportedArticleSource)
    fun removeItem(link: String)
    fun addCustomItem(link: String)
}

//endregion

//region Outputs

interface RSSConfigureViewModelOutputs {
    val list: LiveData<List<RSSConfigureItem>>
    val openWebsite: LiveData<DataEvent<SupportedArticleSource>>
}

//endregion

class RSSConfigureViewModel(
    private val repository: RSSRepository,
    private val rssFeedController: RSSFeedController
) : ViewModel(), RSSConfigureViewModelInputs, RSSConfigureViewModelOutputs {

    var inputs: RSSConfigureViewModelInputs = this
    var outputs: RSSConfigureViewModelOutputs = this

    private val rssUrls: MutableSet<String>
        get() = repository.rssUrls.toMutableSet()

    override val list: MutableLiveData<List<RSSConfigureItem>> = MutableLiveData()
    override val openWebsite: MutableLiveData<DataEvent<SupportedArticleSource>> = MutableLiveData()

    init {
        loadState()
    }

    //region Inputs

    override fun addQuickItem(supportedArticle: SupportedArticleSource) {
        repository.rssUrls = rssUrls + supportedArticle.rssLink
        updateList()
    }

    override fun removeItem(link: String) {
        repository.rssUrls = rssUrls - link
        updateList()
    }

    override fun addCustomItem(link: String) {
        repository.rssUrls = rssUrls + link
        updateList()
    }

    override fun visitWebsite(supportedArticle: SupportedArticleSource) {
        openWebsite.value = DataEvent(supportedArticle)
    }

    //endregion

    /**
     * Load the state of the rss urls from shared preferences
     */
    private fun loadState() {
        this.rssUrls.clear()
        val urls = repository.rssUrls
        this.rssUrls.addAll(urls)
        updateList()
    }

    /**
     * Process the "Added" URL list into a sectioned list to be displayed on the screen
     */
    private fun updateList() {
        val itemList = mutableListOf<RSSConfigureItem>()
        itemList.add(
            RSSConfigureItem.Header(
            text = R.string.rss_configure_header_items,
            subtitle = R.string.rss_configure_header_items_subtitle
        ))
        if (rssUrls.isNotEmpty()) {
            itemList.addAll(rssUrls
                .sortedBy {
                    it.replace("https://www.", "")
                            .replace("http://www.", "")
                            .replace("https://", "")
                            .replace("http://", "")
                }
                .map {
                    RSSConfigureItem.Item(it, rssFeedController.getSupportedSourceByRssUrl(it))
                }
            )
        }
        else {
            itemList.add(RSSConfigureItem.NoItems)
        }

        if (rssFeedController.showAddCustomFeeds) {
            itemList.add(
                RSSConfigureItem.Header(
                    text = R.string.rss_configure_header_add,
                    subtitle = R.string.rss_configure_header_add_subtitle
            ))
            itemList.add(RSSConfigureItem.Add)
        }
        itemList.add(
            RSSConfigureItem.Header(
            text = R.string.rss_configure_header_quick_add,
            subtitle = R.string.rss_configure_header_quick_add_subtitle
        ))
        itemList.addAll(rssFeedController
            .sources
            .filter { !rssUrls.contains(it.rssLink) }
            .sortedBy {
                it.rssLink.replace("https://www.", "")
                        .replace("http://www.", "")
                        .replace("https://", "")
                        .replace("http://", "")
            }
            .map {
                RSSConfigureItem.QuickAdd(it)
            }
        )
        list.value = itemList
    }
}
