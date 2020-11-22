package tmg.flashback.rss.configure

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import tmg.flashback.R
import tmg.flashback.base.BaseViewModel
import tmg.flashback.di.async.ScopeProvider
import tmg.flashback.repo.db.PrefsDB
import tmg.flashback.repo.enums.SupportedArticleSource
import tmg.utilities.lifecycle.Event

//region Inputs

interface RSSConfigureViewModelInputs {
    fun addQuickItem(supportedArticle: SupportedArticleSource)
    fun removeItem(link: String)
    fun addCustomItem(link: String)
    fun clickSave()
}

//endregion

//region Outputs

interface RSSConfigureViewModelOutputs {
    val list: LiveData<List<RSSConfigureItem>>
    val listSaved: LiveData<Event>
}

//endregion


class RSSConfigureViewModel(
    private val prefsDB: PrefsDB,
    scopeProvider: ScopeProvider
): BaseViewModel(scopeProvider), RSSConfigureViewModelInputs, RSSConfigureViewModelOutputs {

    var inputs: RSSConfigureViewModelInputs = this
    var outputs: RSSConfigureViewModelOutputs = this

    private val added: MutableList<String> = mutableListOf()

    override val list: MutableLiveData<List<RSSConfigureItem>> = MutableLiveData()
    override val listSaved: MutableLiveData<Event> = MutableLiveData()

    init {
        loadState()
    }

    //region Inputs

    override fun addQuickItem(supportedArticle: SupportedArticleSource) {

    }

    override fun removeItem(link: String) {

    }

    override fun addCustomItem(link: String) {

    }

    override fun clickSave() {
        listSaved.postValue(Event())
    }

    //endregion

    /**
     * Load the state of the rss urls from shared preferences
     */
    private fun loadState() {
        this.added.clear()
        val urls = prefsDB.rssUrls
        this.added.addAll(urls)
        processList()
    }

    /**
     * Process the "Added" URL list into a sectioned list to be displayed on the screen
     */
    private fun processList() {
        val itemList = mutableListOf<RSSConfigureItem>()
        itemList.add(RSSConfigureItem.Header(R.string.rss_configure_header_items))
        itemList.addAll(added.map {
            RSSConfigureItem.Item(it, SupportedArticleSource.getByRssFeedURL(it))
        })
        itemList.add(RSSConfigureItem.Header(R.string.rss_configure_header_quick_add))
        itemList.addAll(SupportedArticleSource
            .values()
            .filter { !added.contains(it.rssLink) }
            .map {
                RSSConfigureItem.QuickAdd(it)
            }
        )
        list.value = itemList
    }
}
