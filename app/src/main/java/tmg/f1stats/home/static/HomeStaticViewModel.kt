package tmg.f1stats.home.static

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import tmg.f1stats.base.BaseViewModel
import tmg.f1stats.home.trackpicker.TrackModel
import tmg.f1stats.repo.db.SeasonOverviewDB
import tmg.f1stats.repo.utils.filterNotNull
import tmg.f1stats.utils.SeasonRound
import tmg.utilities.extensions.takeWhen
import tmg.utilities.extensions.takeWhenEither
import tmg.utilities.extensions.withLatest

//region Inputs

interface HomeStaticViewModelInputs {
    fun select(year: Int, round: Int?)
    fun clickTrackList()
}

//endregion

//region Outputs

interface HomeStaticViewModelOutputs {
    fun openTrackList(): Observable<SeasonRound>
    fun loadSeasonRound(): Observable<SeasonRound>
    fun circuitInfo(): Observable<TrackModel>
}

//endregion

class HomeStaticViewModel(
    val seasonDB: SeasonOverviewDB
): BaseViewModel(), HomeStaticViewModelInputs, HomeStaticViewModelOutputs {

    private val trackListEvent: PublishSubject<Boolean> = PublishSubject.create()
    private val selectedRound: BehaviorSubject<SeasonRound> = BehaviorSubject.createDefault(Pair(2019, 2)) // Season, Round

    private val circuitInfo: Observable<TrackModel> = selectedRound
        .switchMap { (season, round) -> seasonDB.getSeasonRound(season, round) }
        .filterNotNull()
        .map { TrackModel(
            round = it.round,
            circuitName = it.circuit.name,
            country = it.circuit.country,
            countryKey = it.circuit.countryISO
        )}

    var inputs: HomeStaticViewModelInputs = this
    var outputs: HomeStaticViewModelOutputs = this

    //region Inputs

    override fun clickTrackList() {
        trackListEvent.onNext(true)
    }

    override fun select(year: Int, round: Int?) {
        selectedRound.onNext(Pair(year, round ?: 1))
    }

    //endregion

    //region Outputs

    override fun openTrackList(): Observable<SeasonRound> {
        return selectedRound
            .takeWhenEither(trackListEvent)
    }

    override fun loadSeasonRound(): Observable<SeasonRound> {
        return selectedRound
    }

    override fun circuitInfo(): Observable<TrackModel> {
        return circuitInfo
    }

    //endregion
}