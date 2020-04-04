package tmg.f1stats.home.swiping

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import tmg.f1stats.base.BaseViewModel
import tmg.f1stats.repo.db.PrefsDB
import tmg.utilities.extensions.combineWithPair
import tmg.utilities.extensions.takeWhen

sealed class Screen {
    class Drivers(val season: Int) : Screen()
    class Constructor(val season: Int) : Screen()
    object Settings : Screen()
}

//region Inputs

interface HomeSwipingViewModelInputs {
    fun clickTab(tabOption: HomeTabOption)
    fun clickCalendar()
    fun yearChangedToo(year: Int)
}

//endregion

//region Outputs

interface HomeSwipingViewModelOutputs {

    fun showScreen(): Observable<Screen>
    fun showCalendarPicker(): Observable<Int>
}

//endregion

class HomeSwipingViewModel(
    prefs: PrefsDB
) : BaseViewModel(), HomeSwipingViewModelInputs,
    HomeSwipingViewModelOutputs {

    private var tabSelectedEvent: BehaviorSubject<HomeTabOption> =
        BehaviorSubject.createDefault(HomeTabOption.DRIVERS)

    private val showCalendarEvent: PublishSubject<Boolean> = PublishSubject.create()
    private val selectedYearEvent: BehaviorSubject<Int> = BehaviorSubject.createDefault(prefs.selectedYear)

    var inputs: HomeSwipingViewModelInputs = this
    var outputs: HomeSwipingViewModelOutputs = this

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

    override fun showScreen(): Observable<Screen> {
        return tabSelectedEvent
            .combineWithPair(selectedYearEvent)
            .map { (tab, year) ->
                @Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
                return@map when (tab) {
                    HomeTabOption.DRIVERS -> Screen.Drivers(
                        year
                    )
                    HomeTabOption.CONSTRUCTORS -> Screen.Constructor(
                        year
                    )
                    HomeTabOption.SETTINGS -> Screen.Settings
                }
            }
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
    SETTINGS
}