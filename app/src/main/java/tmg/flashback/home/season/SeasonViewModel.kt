package tmg.flashback.home.season

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import tmg.flashback.allYears
import tmg.flashback.base.BaseViewModel
import tmg.flashback.currentYear
import tmg.flashback.repo.db.PrefsDB
import tmg.flashback.di.async.ScopeProvider
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
    private val prefDB: PrefsDB,
    scopeProvider: ScopeProvider
) : BaseViewModel(scopeProvider), SeasonViewModelInputs, SeasonViewModelOutputs {

    var headerSectionFavourited: Boolean = prefDB.showBottomSheetFavourited
    var headerSectionAll: Boolean = prefDB.showBottomSheetAll

    private val favouriteSeasons = prefDB.favouriteSeasons.toMutableSet()
    override val list: MutableLiveData<List<SeasonListItem>> = MutableLiveData()
    override val showSeasonEvent: MutableLiveData<DataEvent<Int>> = MutableLiveData()

    var inputs: SeasonViewModelInputs = this
    var outputs: SeasonViewModelOutputs = this

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
        prefDB.favouriteSeasons = favouriteSeasons
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