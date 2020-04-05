package tmg.f1stats.home.static

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import tmg.f1stats.base.BaseViewModel
import tmg.f1stats.home.trackpicker.TrackModel
import tmg.f1stats.repo.db.HistoryDB
import tmg.f1stats.repo.db.SeasonOverviewDB
import tmg.f1stats.repo.models.History
import tmg.f1stats.repo.utils.filterNotNull
import tmg.f1stats.season.race.RaceAdapterType
import tmg.f1stats.utils.SeasonRound
import tmg.f1stats.utils.Selected
import tmg.utilities.extensions.combineWithPair
import tmg.utilities.extensions.combineWithTriple

//region Inputs

interface HomeStaticViewModelInputs {
    fun select(season: Int, round: Int?)
    fun browse(season: Int)
    fun clickTrackList()
    fun closeTrackList()
}

//endregion

//region Outputs

interface HomeStaticViewModelOutputs {
    fun openTrackList(): Observable<Boolean>
    fun switchYearList(): Observable<List<Selected<String>>>
    fun switchTrackList(): Observable<List<Selected<TrackModel>>>
    fun loadSeasonRound(): Observable<SeasonRound>
    fun circuitInfo(): Observable<TrackModel>
}

//endregion

class HomeStaticViewModel(
    private val seasonDB: SeasonOverviewDB,
    private val historyDB: HistoryDB
): BaseViewModel(), HomeStaticViewModelInputs, HomeStaticViewModelOutputs {

    private val trackListEvent: PublishSubject<Boolean> = PublishSubject.create()
    private val selectedRound: BehaviorSubject<SeasonRound> = BehaviorSubject.createDefault(Pair(2019, 1)) // Season, Round
    private val browsingSeason: BehaviorSubject<Int> = BehaviorSubject.createDefault(2019) // Season

    private val allHistory: Observable<List<History>> = historyDB
        .allHistory()
        .share()
    private val circuitInfo: Observable<TrackModel> = selectedRound
        .switchMap { (season, round) -> seasonDB.getSeasonRound(season, round) }
        .share()
        .filterNotNull()
        .map { TrackModel(
            season = it.season,
            round = it.round,
            circuitName = it.circuit.name,
            country = it.circuit.country,
            countryKey = it.circuit.countryISO
        )}
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    var inputs: HomeStaticViewModelInputs = this
    var outputs: HomeStaticViewModelOutputs = this

    //region Inputs

    override fun clickTrackList() {
        trackListEvent.onNext(true)
    }

    override fun closeTrackList() {
        trackListEvent.onNext(false)
    }

    override fun select(season: Int, round: Int?) {
        selectedRound.onNext(Pair(season, round ?: 1))
        browsingSeason.onNext(season)
    }

    override fun browse(season: Int) {
        browsingSeason.onNext(season)
    }

    //endregion

    //region Outputs

    override fun openTrackList(): Observable<Boolean> {
        return trackListEvent
    }

    override fun loadSeasonRound(): Observable<SeasonRound> {
        return selectedRound
    }

    override fun circuitInfo(): Observable<TrackModel> {
        return circuitInfo
    }

    override fun switchTrackList(): Observable<List<Selected<TrackModel>>> {
        return allHistory
            .combineWithTriple(selectedRound, browsingSeason)
            .map { (history, selectedRound, season) ->
                history.firstOrNull { it.season == season }
                    ?.round
                    ?.map {
                        Selected(TrackModel(season, it.round, it.raceName, it.country, it.countryISO), isSelected = it.round == selectedRound.second && it.season == selectedRound.first)
                    }
                    ?.sortedBy { it.value.round }
            }
    }

    override fun switchYearList(): Observable<List<Selected<String>>> {
        return allHistory
            .combineWithPair(browsingSeason)
            .map { (history, season) ->
                history
                    .map { Selected(it.season.toString(), isSelected = it.season == season) }
                    .sortedByDescending { it.value }
            }
    }

    //endregion
}