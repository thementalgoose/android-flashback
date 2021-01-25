package tmg.flashback.ui.dashboard.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import tmg.flashback.R
import tmg.flashback.controllers.FeatureController
import tmg.flashback.ui.base.BaseViewModel
import tmg.flashback.controllers.SeasonController
import tmg.flashback.controllers.UpNextController
import tmg.utilities.lifecycle.DataEvent
import tmg.utilities.lifecycle.Event

//region Inputs

interface ListViewModelInputs {
    fun refresh()

    fun clickRss()

    fun toggleHeader(header: HeaderType, to: Boolean? = null)
    fun toggleFavourite(season: Int)
    fun clickSeason(season: Int)

    fun clickClearDefaultSeason()
    fun clickSetDefaultSeason(season: Int)

    fun clickSettings()
}

//endregion

//region Outputs

interface ListViewModelOutputs {
    val list: LiveData<List<ListItem>>
    val showSeasonEvent: LiveData<DataEvent<Int>>

    val defaultSeasonUpdated: LiveData<DataEvent<Int?>>

    val openSettings: LiveData<Event>
    val openRss: LiveData<Event>
}

//endregion

class ListViewModel(
        private val seasonController: SeasonController,
        private val upNextController: UpNextController,
        private val featureController: FeatureController
) : BaseViewModel(), ListViewModelInputs, ListViewModelOutputs {

    var headerSectionFavourited: Boolean = seasonController.favouritesExpanded
    var headerSectionAll: Boolean = seasonController.allExpanded

    private var currentSeason: Int = seasonController.defaultSeason
    private val favouriteSeasons = seasonController.favouriteSeasons.toMutableSet()

    override val list: MutableLiveData<List<ListItem>> = MutableLiveData()
    override val showSeasonEvent: MutableLiveData<DataEvent<Int>> = MutableLiveData()
    override val openSettings: MutableLiveData<Event> = MutableLiveData()
    override val openRss: MutableLiveData<Event> = MutableLiveData()
    override val defaultSeasonUpdated: MutableLiveData<DataEvent<Int?>> = MutableLiveData()

    var inputs: ListViewModelInputs = this
    var outputs: ListViewModelOutputs = this

    init {
        list.value = buildList(favouriteSeasons, headerSectionFavourited, headerSectionAll)
    }

    //region Inputs

    override fun refresh() {
        list.value = buildList(favouriteSeasons, headerSectionFavourited, headerSectionAll)
    }

    override fun clickRss() {
        openRss.value = Event()
    }

    override fun toggleHeader(header: HeaderType, to: Boolean?) {
        when (header) {
            HeaderType.FAVOURITED -> headerSectionFavourited = to ?: !headerSectionFavourited
            HeaderType.ALL -> headerSectionAll = to ?: !headerSectionAll
            HeaderType.UP_NEXT -> { /* Do nothing */ }
            HeaderType.EXTRA -> { /* Do nothing */ }
        }
        refresh()
    }

    override fun toggleFavourite(season: Int) {
        if (favouriteSeasons.contains(season)) {
            favouriteSeasons.remove(season)
            seasonController.removeFavourite(season)
        } else {
            favouriteSeasons.add(season)
            seasonController.addFavourite(season)
        }
        refresh()
    }

    override fun clickSeason(season: Int) {
        showSeasonEvent.value = DataEvent(season)
        currentSeason = season
        refresh()
    }

    override fun clickSetDefaultSeason(season: Int) {
        seasonController.setUserDefaultSeason(season)
        defaultSeasonUpdated.value = DataEvent(season)
        refresh()
    }

    override fun clickClearDefaultSeason() {
        seasonController.clearDefault()
        defaultSeasonUpdated.value = DataEvent(null)
        refresh()
    }

    override fun clickSettings() {
        openSettings.value = Event()
    }

    //endregion

    private fun buildList(
        favouritedSet: Set<Int>,
        headerSectionFavourites: Boolean,
        headerSectionAll: Boolean
    ): List<ListItem> {

        val list: MutableList<ListItem> = mutableListOf(ListItem.Hero)

        // Up next
        upNextController.getNextEvent()?.let {
            list.add(ListItem.Divider)
            list.add(ListItem.Header(type = HeaderType.UP_NEXT, expanded = null))
            list.add(ListItem.UpNext(it))
        }

        // RSS
        if (featureController.rssEnabled) {
            list.add(ListItem.Divider)
            list.add(ListItem.Header(type = HeaderType.EXTRA, expanded = null))
            list.add(ListItem.Button(
                    itemId = "rss",
                    label = R.string.dashboard_season_list_extra_rss_title,
                    icon = R.drawable.nav_rss
            ))
        }

        val supportedSeasons = seasonController.supportedSeasons
        val defaultSeason = seasonController.defaultSeason
        val isUserDefinedValueSet = seasonController.isUserDefinedValueSet

        // Favourites
        list.add(ListItem.Divider)
        list.add(
            ListItem.Header(
                HeaderType.FAVOURITED,
                headerSectionFavourites
            )
        )
        if (headerSectionFavourites) {
            list.addAll(favouritedSet
                .toList()
                .filter { supportedSeasons.contains(it) }
                .sortedByDescending { it }
                .map {
                    ListItem.Season(
                        season = it,
                        isFavourited = true,
                        fixed = HeaderType.FAVOURITED,
                        selected = currentSeason == it,
                        default = defaultSeason == it,
                        showClearDefault = isUserDefinedValueSet
                    )
                }
            )
        }

        // All
        list.add(ListItem.Header(HeaderType.ALL, headerSectionAll))
        if (headerSectionAll) {
            list.addAll(supportedSeasons
                .map {
                    ListItem.Season(
                        season = it,
                        isFavourited = favouritedSet.contains(it),
                        fixed = HeaderType.ALL,
                        selected = currentSeason == it,
                        default = defaultSeason == it,
                        showClearDefault = isUserDefinedValueSet
                    )
                }
                .sortedByDescending { it.season }
            )
        }

        return list
    }
}