package tmg.f1stats.home.trackpicker

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import tmg.f1stats.base.BaseViewModel
import tmg.f1stats.repo.db.SeasonOverviewDB
import tmg.f1stats.supportedYears
import tmg.f1stats.utils.RxUtils
import tmg.f1stats.utils.Selected
import tmg.utilities.extensions.combineWithPair

//region Inputs

interface TrackPickerViewModelInputs {
    fun initialSeasonRound(season: Int, round: Int)
    fun clickSeason(season: Int)
    fun clickRound(round: Int)
}

//endregion

//region Outputs

interface TrackPickerViewModelOutputs {
    fun yearList(): Observable<List<Selected<String>>>
    fun trackList(): Observable<List<Selected<TrackModel>>>
    fun selectedSeason(): Observable<Int>
    fun selectedRound(): Observable<Int>
}

//endregion

class TrackPickerViewModel(
    private val seasonDB: SeasonOverviewDB
): BaseViewModel(), TrackPickerViewModelInputs, TrackPickerViewModelOutputs {

    private val initialSeason: BehaviorSubject<Int> = BehaviorSubject.create()
    private val initialRound: BehaviorSubject<Int> = BehaviorSubject.create()

    private val selectedSeason: PublishSubject<Int> = PublishSubject.create()
    private val selectedRound: PublishSubject<Int> = PublishSubject.create()

    private val yearList: Observable<List<Selected<String>>> = RxUtils.ongoing(supportedYears)
        .combineWithPair(initialSeason)
        .map { (years, selectedYear) ->
            years.map {
                Selected(it.toString(), it == selectedYear)
            }
        }
    private val trackList: Observable<List<Selected<TrackModel>>> = initialSeason
        .switchMap { season ->
            seasonDB.getSeasonOverview(season)
        }
        .share()
        .map {
            return@map it.map { race ->
                Selected(TrackModel(race.season, race.round, race.circuit.name, race.circuit.country, race.circuit.countryISO))
            }
        }
        .combineWithPair(initialRound)
        .map { (models, selectedRound) ->
            models.map { Selected(it.value, selectedRound == it.value.round)}
        }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    var inputs: TrackPickerViewModelInputs = this
    var outputs: TrackPickerViewModelOutputs = this

    //region Inputs

    override fun initialSeasonRound(season: Int, round: Int) {
        initialSeason.onNext(season)
        initialRound.onNext(round)
    }

    override fun clickSeason(season: Int) {
        selectedSeason.onNext(season)
    }

    override fun clickRound(round: Int) {
        selectedRound.onNext(round)
    }

    //endregion

    //region Outputs

    override fun yearList(): Observable<List<Selected<String>>> {
        return yearList
    }

    override fun trackList(): Observable<List<Selected<TrackModel>>> {
        return trackList
    }

    override fun selectedSeason(): Observable<Int> {
        return selectedSeason
    }

    override fun selectedRound(): Observable<Int> {
        return selectedRound
    }

    //endregion
}