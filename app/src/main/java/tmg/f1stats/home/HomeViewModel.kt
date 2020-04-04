package tmg.f1stats.home

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import org.koin.core.definition.indexKey
import org.threeten.bp.Year
import tmg.f1stats.base.BaseViewModel
import tmg.f1stats.minimumSupportedYear
import tmg.f1stats.repo.db.PrefsDB
import tmg.f1stats.supportedYears
import tmg.f1stats.utils.RxUtils
import tmg.f1stats.utils.Selected
import tmg.utilities.extensions.combineWithPair
import tmg.utilities.extensions.takeWhen
import tmg.utilities.extensions.withLatest
import tmg.utilities.utils.ongoing

//region Inputs

interface HomeViewModelInputs {
    fun clickTab(tabOption: HomeTabOption)
    fun clickCalendar()
    fun yearChangedToo(year: Int)
}

//endregion

//region Outputs

interface HomeViewModelOutputs {

    fun showSeason(): Observable<Int>
    fun showCalendarPicker(): Observable<Int>
}

//endregion

class HomeViewModel(
    prefs: PrefsDB
) : BaseViewModel(), HomeViewModelInputs, HomeViewModelOutputs {

    private var tabSelectedEvent: BehaviorSubject<HomeTabOption> =
        BehaviorSubject.createDefault(HomeTabOption.DRIVERS)

    private val showCalendarEvent: PublishSubject<Boolean> = PublishSubject.create()
    private val selectedYearEvent: BehaviorSubject<Int> = BehaviorSubject.createDefault(prefs.selectedYear)

    var inputs: HomeViewModelInputs = this
    var outputs: HomeViewModelOutputs = this

    init {

    }

    //region Inputs

    override fun clickTab(tabOption: HomeTabOption) {
        tabSelectedEvent.onNext(tabOption)
    }

    override fun clickCalendar() {
        showCalendarEvent.onNext(true)
    }

    override fun yearChangedToo(year: Int) {
        selectedYearEvent.onNext(year)
    }

    //endregion

    //region Outputs

    override fun showSeason(): Observable<Int> {
        return selectedYearEvent
    }

    override fun showCalendarPicker(): Observable<Int> {
        return selectedYearEvent
            .takeWhen(showCalendarEvent)
    }

    //endregion
}


enum class HomeTabOption {
    DRIVERS,
    CONSTRUCTORS,
    GALLERY,
    SETTINGS
}