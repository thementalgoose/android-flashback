package tmg.flashback.rss.ui.configure

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import tmg.flashback.rss.R
import tmg.flashback.rss.base.RSSBaseViewModel
import tmg.flashback.rss.prefs.RSSPrefsRepository
import tmg.flashback.rss.repo.enums.SupportedArticleSource

//region Inputs

interface RSSConfigureViewModelInputs {
    fun addQuickItem(supportedArticle: SupportedArticleSource)
    fun removeItem(link: String)
    fun addCustomItem(link: String)
}

//endregion

//region Outputs

interface RSSConfigureViewModelOutputs {
    val list: LiveData<List<RSSConfigureItem>>
}

//endregion

class RSSConfigureViewModel(
        private val prefsRepository: RSSPrefsRepository
) : RSSBaseViewModel(), RSSConfigureViewModelInputs, RSSConfigureViewModelOutputs {

    var inputs: RSSConfigureViewModelInputs = this
    var outputs: RSSConfigureViewModelOutputs = this

    private val rssUrls: MutableSet<String>
        get() = prefsRepository.rssUrls.toMutableSet()

    override val list: MutableLiveData<List<RSSConfigureItem>> = MutableLiveData()

    init {
        loadState()
    }

    //region Inputs

    override fun addQuickItem(supportedArticle: SupportedArticleSource) {
        prefsRepository.rssUrls = rssUrls + supportedArticle.rssLink
        updateList()
    }

    override fun removeItem(link: String) {
        prefsRepository.rssUrls = rssUrls - link
        updateList()

    }

    override fun addCustomItem(link: String) {
        prefsRepository.rssUrls = rssUrls + link
        updateList()
    }

    //endregion

    /**
     * Load the state of the rss urls from shared preferences
     */
    private fun loadState() {
        this.rssUrls.clear()
        val urls = prefsRepository.rssUrls
        this.rssUrls.addAll(urls)
        updateList()
    }

    /**
     * Process the "Added" URL list into a sectioned list to be displayed on the screen
     */
    private fun updateList() {
        val itemList = mutableListOf<RSSConfigureItem>()
        itemList.add(RSSConfigureItem.Header(
            text = R.string.rss_configure_header_items,
            subtitle = R.string.rss_configure_header_items_subtitle
        ))
        if (rssUrls.isNotEmpty()) {
            itemList.addAll(rssUrls
                .sortedBy { it }
                .map {
                    RSSConfigureItem.Item(it, SupportedArticleSource.getByRssFeedURL(it))
                }
            )
        }
        else {
            itemList.add(RSSConfigureItem.NoItems)
        }
        itemList.add(RSSConfigureItem.Header(
                text = R.string.rss_configure_header_add,
                subtitle = R.string.rss_configure_header_add_subtitle
        ))
        itemList.add(RSSConfigureItem.Add)
        itemList.add(RSSConfigureItem.Header(
            text = R.string.rss_configure_header_quick_add,
            subtitle = R.string.rss_configure_header_quick_add_subtitle
        ))
        itemList.addAll(SupportedArticleSource
            .values()
            .filter { !rssUrls.contains(it.rssLink) }
            .sortedBy { it }
            .map {
                RSSConfigureItem.QuickAdd(it)
            }
        )
        list.value = itemList
    }
}
