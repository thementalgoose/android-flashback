package tmg.flashback.dashboard.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import tmg.flashback.allYears
import tmg.flashback.base.BaseViewModel
import tmg.flashback.repo.config.RemoteConfigRepository
import tmg.flashback.repo.pref.PrefCustomisationRepository
import tmg.utilities.lifecycle.DataEvent
import tmg.utilities.lifecycle.Event

//region Inputs

interface ListViewModelInputs {
    fun toggleHeader(header: HeaderType, to: Boolean? = null)
    fun toggleFavourite(season: Int)
    fun clickSeason(season: Int)

    fun clickSettings()
}

//endregion

//region Outputs

interface ListViewModelOutputs {
    val list: LiveData<List<ListItem>>
    val showSeasonEvent: LiveData<DataEvent<Int>>

    val openSettings: LiveData<Event>
}

//endregion

class ListViewModel(
    private val prefRepository: PrefCustomisationRepository,
    private val remoteConfigRepository: RemoteConfigRepository
) : BaseViewModel(), ListViewModelInputs, ListViewModelOutputs {

    private var headerSectionFavourited: Boolean = prefRepository.showBottomSheetFavourited
    private var headerSectionAll: Boolean = prefRepository.showBottomSheetAll

    private var currentSeason: Int = remoteConfigRepository.defaultYear
    private val favouriteSeasons = prefRepository.favouriteSeasons.toMutableSet()

    override val list: MutableLiveData<List<ListItem>> = MutableLiveData()
    override val showSeasonEvent: MutableLiveData<DataEvent<Int>> = MutableLiveData()
    override val openSettings: MutableLiveData<Event> = MutableLiveData()

    var inputs: ListViewModelInputs = this
    var outputs: ListViewModelOutputs = this

    init {
        list.value = buildList(favouriteSeasons, headerSectionFavourited, headerSectionAll)
    }

    //region Inputs

    override fun toggleHeader(header: HeaderType, to: Boolean?) {
        when (header) {
            HeaderType.FAVOURITED -> headerSectionFavourited = to ?: !headerSectionFavourited
            HeaderType.ALL -> headerSectionAll = to ?: !headerSectionAll
        }
        list.value = buildList(favouriteSeasons, headerSectionFavourited, headerSectionAll)
    }

    override fun toggleFavourite(season: Int) {
        if (favouriteSeasons.contains(season)) {
            favouriteSeasons.remove(season)
        }
        else {
            favouriteSeasons.add(season)
        }
        prefRepository.favouriteSeasons = favouriteSeasons
        list.value = buildList(favouriteSeasons, headerSectionFavourited, headerSectionAll)
    }

    override fun clickSeason(season: Int) {
        showSeasonEvent.value = DataEvent(season)
        currentSeason = season
        list.value = buildList(favouriteSeasons, headerSectionFavourited, headerSectionAll)
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

        // Favourites
        list.add(
            ListItem.Header(
                HeaderType.FAVOURITED,
                headerSectionFavourites
            )
        )
        if (headerSectionFavourites) {
            list.addAll(favouritedSet
                .toList()
                .sortedByDescending { it }
                .map {
                    ListItem.Season(
                        season = it,
                        isFavourited = true,
                        fixed = HeaderType.FAVOURITED
                    )
                }
            )
        }

        // All
        list.add(ListItem.Header(HeaderType.ALL, headerSectionAll))
        if (headerSectionAll) {
            list.addAll(
                allYears
                .map {
                    ListItem.Season(
                        season = it,
                        isFavourited = favouritedSet.contains(it),
                        fixed = HeaderType.ALL,
                        selected = currentSeason == it
                    )
                }
                .sortedByDescending { it.season }
            )
        }

        return list
    }
}