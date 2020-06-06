package tmg.flashback.home.season

import android.util.Log
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

    var headers: Headers = Headers()
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
        Log.i("Flashback", "Toggle favourite $season")
        Log.i("Flashback", "Favourite set $favouriteSeasons")
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

        Log.i("Flashback", "Favourite buildList set $favouritedSet")

        val list: MutableList<SeasonListItem> = mutableListOf()

        // Current
        list.add(SeasonListItem.Header(HeaderType.CURRENT, null))
        list.add(SeasonListItem.Season(
            season = currentYear,
            isFavourited = favouritedSet.contains(currentYear),
            fixed = "current"
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
                        fixed = "featured"
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
                        fixed = "all"
                    )
                }
                .sortedByDescending { it.season }
            )
        }

        Log.i("Flashback", "List $list")

        return list
    }
}