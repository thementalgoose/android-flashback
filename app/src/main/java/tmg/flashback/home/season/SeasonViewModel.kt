package tmg.flashback.home.season

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import tmg.flashback.allYears
import tmg.flashback.base.BaseViewModel
import tmg.flashback.currentYear
import tmg.flashback.repo.db.PrefsDB
import tmg.utilities.lifecycle.DataEvent

//region Inputs

interface SeasonViewModelInputs {
    fun toggleHeader(header: HeaderType, to: Boolean? = null)
    fun toggleFavourite(season: Int)
    fun clickSeason(season: Int)
}

//endregion

//region Outputs

interface SeasonViewModelOutputs {
    val list: LiveData<List<SeasonListItem>>
    val showSeasonEvent: MutableLiveData<DataEvent<Int>>
}

//endregion

class SeasonViewModel(
    private val prefDB: PrefsDB
) : BaseViewModel(), SeasonViewModelInputs, SeasonViewModelOutputs {

    var headers: Headers = Headers(
        favourited = prefDB.showBottomSheetFavourited,
        all = prefDB.showBottomSheetAll
    )
    data class Headers( // TODO: Move these to settings
        var favourited: Boolean = true,
        var all: Boolean = true
    )

    private val favouriteSeasons = prefDB.favouriteSeasons.toMutableSet()
    override val list: MutableLiveData<List<SeasonListItem>> = MutableLiveData(buildList(favouriteSeasons, headers))
    override val showSeasonEvent: MutableLiveData<DataEvent<Int>> = MutableLiveData()

    var inputs: SeasonViewModelInputs = this
    var outputs: SeasonViewModelOutputs = this

    //region Inputs

    override fun toggleHeader(header: HeaderType, to: Boolean?) {
        when (header) {
            HeaderType.FAVOURITED -> headers.favourited = to ?: !headers.favourited
            HeaderType.ALL -> headers.all = to ?: !headers.all
            else -> {}
        }
        list.value = buildList(favouriteSeasons, headers)
    }

    override fun toggleFavourite(season: Int) {
        if (favouriteSeasons.contains(season)) {
            favouriteSeasons.remove(season)
        }
        else {
            favouriteSeasons.add(season)
        }
        prefDB.favouriteSeasons = favouriteSeasons
        list.value = buildList(favouriteSeasons, headers)
    }

    override fun clickSeason(season: Int) {
        showSeasonEvent.value = DataEvent(season)
    }

    //endregion

    private fun buildList(
        favouritedSet: Set<Int>,
        expandedState: Headers
    ): List<SeasonListItem> {

        val list: MutableList<SeasonListItem> = mutableListOf(SeasonListItem.Top)

        // Current
        list.add(SeasonListItem.Header(HeaderType.CURRENT, null))
        list.add(SeasonListItem.Season(
            season = currentYear,
            isFavourited = favouritedSet.contains(currentYear),
            fixed = HeaderType.CURRENT
        ))

        // Favourites
        list.add(
            SeasonListItem.Header(
                HeaderType.FAVOURITED,
                expandedState.favourited
            )
        )
        if (expandedState.favourited) {
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
        list.add(SeasonListItem.Header(HeaderType.ALL, expandedState.all))
        if (expandedState.all) {
            list.addAll(allYears
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