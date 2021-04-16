package tmg.flashback.statistics.ui.dashboard.list

import androidx.lifecycle.*
import kotlinx.coroutines.flow.combine
import tmg.flashback.core.controllers.AnalyticsController
import tmg.flashback.core.controllers.FeatureController
import tmg.flashback.core.model.TimeListDisplayType
import tmg.flashback.statistics.R
import tmg.flashback.core.ui.BaseViewModel
import tmg.flashback.statistics.controllers.SeasonController
import tmg.flashback.statistics.controllers.UpNextController
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

    fun clickTimeDisplayType(type: TimeListDisplayType)

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

    private var selectionHeaderFavouited: MutableLiveData<Boolean> =
        MutableLiveData(seasonController.favouritesExpanded)
    private var selectionHeaderAll: MutableLiveData<Boolean> =
        MutableLiveData(seasonController.allExpanded)
    private var currentSeason: MutableLiveData<Int> =
        MutableLiveData(seasonController.defaultSeason)
    private val refreshList: MutableLiveData<Event> = MutableLiveData(Event())

    override val list: LiveData<List<ListItem>> = combine(
        currentSeason.asFlow(),
        selectionHeaderFavouited.asFlow(),
        selectionHeaderAll.asFlow(),
        refreshList.asFlow()
    ) { currentSeason, favouriteExpanded, allExpanded, _ ->

        val list: MutableList<ListItem> = mutableListOf(ListItem.Hero)

        // Up next
        upNextController.getNextEvent()?.let {
            list.add(ListItem.Divider)
            list.add(ListItem.Header(type = HeaderType.UP_NEXT, expanded = null))
            list.add(ListItem.UpNext(it, timeListDisplayType))
        }

        // Extra buttons
        val buttonsList = mutableListOf<ListItem.Button>().apply {
            if (featureController.rssEnabled) {
                add(
                    ListItem.Button(
                        itemId = "rss",
                        label = R.string.dashboard_season_list_extra_rss_title,
                        icon = R.drawable.nav_rss
                    )
                )
            }
            add(
                ListItem.Button(
                    itemId = "settings",
                    label = R.string.dashboard_season_list_extra_settings_title,
                    icon = R.drawable.nav_settings
                )
            )
        }
        if (buttonsList.isNotEmpty()) {
            list.add(ListItem.Divider)
            list.add(ListItem.Header(type = HeaderType.LINKS, expanded = null))
            list.addAll(buttonsList)
        }

        // Season breakdown
        val supportedSeasons = seasonController.supportedSeasons
        val defaultSeason = seasonController.defaultSeason
        val isUserDefinedValueSet = seasonController.isUserDefinedValueSet
        val favouritedSeasons = seasonController.favouriteSeasons

        // Favourites
        list.add(ListItem.Divider)
        list.add(
            ListItem.Header(
                HeaderType.FAVOURITED,
                favouriteExpanded
            )
        )
        if (favouriteExpanded) {
            list.addAll(favouritedSeasons
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
        list.add(ListItem.Header(HeaderType.ALL, allExpanded))
        if (allExpanded) {
            list.addAll(supportedSeasons
                .map {
                    ListItem.Season(
                        season = it,
                        isFavourited = favouritedSeasons.contains(it),
                        fixed = HeaderType.ALL,
                        selected = currentSeason == it,
                        default = defaultSeason == it,
                        showClearDefault = isUserDefinedValueSet
                    )
                }
                .sortedByDescending { it.season }
            )
        }

        return@combine list
    }
        .asLiveData(viewModelScope.coroutineContext)


    private var timeListDisplayType: TimeListDisplayType = upNextController.upNextDisplayType

    override val showSeasonEvent: MutableLiveData<DataEvent<Int>> = MutableLiveData()
    override val openSettings: MutableLiveData<Event> = MutableLiveData()
    override val openRss: MutableLiveData<Event> = MutableLiveData()
    override val defaultSeasonUpdated: MutableLiveData<DataEvent<Int?>> = MutableLiveData()

    var inputs: ListViewModelInputs = this
    var outputs: ListViewModelOutputs = this

    //region Inputs

    override fun refresh() {
        refreshList.postValue(Event())
    }

    override fun clickRss() {
        openRss.value = Event()
    }

    override fun toggleHeader(header: HeaderType, to: Boolean?) {
        when (header) {
            HeaderType.FAVOURITED -> {
                selectionHeaderFavouited.postValue(to ?: (selectionHeaderFavouited.value != true))
            }
            HeaderType.ALL -> {
                selectionHeaderAll.postValue(to ?: (selectionHeaderAll.value != true))
            }
            HeaderType.UP_NEXT -> { /* Do nothing */ }
            HeaderType.LINKS -> { /* Do nothing */ }
        }
    }

    override fun toggleFavourite(season: Int) {
        if (seasonController.isFavourite(season)) {
            seasonController.removeFavourite(season)
        } else {
            seasonController.addFavourite(season)
        }
        refreshList.postValue(Event())
    }

    override fun clickSeason(season: Int) {
        showSeasonEvent.value = DataEvent(season)
        currentSeason.postValue(season)
    }

    override fun clickSetDefaultSeason(season: Int) {
        seasonController.setUserDefaultSeason(season)
        defaultSeasonUpdated.value = DataEvent(season)
        refreshList.postValue(Event())
    }

    override fun clickClearDefaultSeason() {
        seasonController.clearDefault()
        defaultSeasonUpdated.value = DataEvent(null)
        refreshList.postValue(Event())
    }

    override fun clickSettings() {
        openSettings.value = Event()
    }

    override fun clickTimeDisplayType(type: TimeListDisplayType) {
        timeListDisplayType = type
        refreshList.postValue(Event())
    }

    //endregion
}