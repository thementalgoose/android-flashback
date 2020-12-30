package tmg.flashback.dashboard.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import tmg.flashback.allYears
import tmg.flashback.base.BaseViewModel
import tmg.flashback.home.season.HeaderType
import tmg.flashback.home.season.SeasonListItem
import tmg.flashback.repo.pref.PrefCustomisationRepository
import tmg.utilities.lifecycle.DataEvent

//region Inputs

interface ListViewModelInputs {
    fun toggleHeader(header: HeaderType, to: Boolean? = null)
    fun toggleFavourite(season: Int)
    fun clickSeason(season: Int)
}

//endregion

//region Outputs

interface ListViewModelOutputs {
    val list: LiveData<List<SeasonListItem>>
    val showSeasonEvent: LiveData<DataEvent<Int>>
}

//endregion

class ListViewModel(
    private val prefRepository: PrefCustomisationRepository
) : BaseViewModel(), ListViewModelInputs, ListViewModelOutputs {

    var headerSectionFavourited: Boolean = prefRepository.showBottomSheetFavourited
    var headerSectionAll: Boolean = prefRepository.showBottomSheetAll

    private val favouriteSeasons = prefRepository.favouriteSeasons.toMutableSet()
    override val list: MutableLiveData<List<SeasonListItem>> = MutableLiveData()
    override val showSeasonEvent: MutableLiveData<DataEvent<Int>> = MutableLiveData()

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
            else -> {}
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
    }

    //endregion

    private fun buildList(
        favouritedSet: Set<Int>,
        headerSectionFavourites: Boolean,
        headerSectionAll: Boolean
    ): List<SeasonListItem> {

        val list: MutableList<SeasonListItem> = mutableListOf()

        // Favourites
        list.add(
            SeasonListItem.Header(
                HeaderType.FAVOURITED,
                headerSectionFavourites
            )
        )
        if (headerSectionFavourites) {
            list.addAll(favouritedSet
                .toList()
                .sortedByDescending { it }
                .map {
                    SeasonListItem.Season(
                        season = it,
                        isFavourited = true,
                        fixed = HeaderType.FAVOURITED
                    )
                }
            )
        }

        // All
        list.add(SeasonListItem.Header(HeaderType.ALL, headerSectionAll))
        if (headerSectionAll) {
            list.addAll(
                allYears
                .map {
                    SeasonListItem.Season(
                        season = it,
                        isFavourited = favouritedSet.contains(it),
                        fixed = HeaderType.ALL
                    )
                }
                .sortedByDescending { it.season }
            )
        }

        return list
    }
}