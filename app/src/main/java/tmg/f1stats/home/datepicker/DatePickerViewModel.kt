package tmg.f1stats.home.datepicker

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import tmg.f1stats.base.BaseViewModel
import tmg.f1stats.supportedYears
import tmg.f1stats.utils.RxUtils
import tmg.f1stats.utils.Selected
import tmg.utilities.extensions.combineWithPair

//region Inputs

interface DatePickerViewModelInputs {
    fun initialYear(year: Int)
    fun selectYear(year: Int)
}

//endregion

//region Outputs

interface DatePickerViewModelOutputs {
    fun showCalendarWithOptions(): Observable<List<Selected<String>>>
    fun selectionMade(): Observable<Int>
}

//endregion

class DatePickerViewModel : BaseViewModel(), DatePickerViewModelInputs, DatePickerViewModelOutputs {

    private val calendarYearEvent: BehaviorSubject<Int> = BehaviorSubject.create()
    private val selectedYearEvent: PublishSubject<Int> = PublishSubject.create()

    var inputs: DatePickerViewModelInputs = this
    var outputs: DatePickerViewModelOutputs = this

    init {

    }

    //region Inputs

    override fun initialYear(year: Int) {
        calendarYearEvent.onNext(year)
    }

    override fun selectYear(year: Int) {
        selectedYearEvent.onNext(year)
    }

    //endregion

    //region Outputs

    override fun showCalendarWithOptions(): Observable<List<Selected<String>>> {
        return RxUtils.ongoing(supportedYears)
            .combineWithPair(calendarYearEvent)
            .map { (listOfYears, selectedYear) ->
                listOfYears
                    .map { Selected(it.toString(), isSelected = selectedYear == it) }
                    .sortedByDescending { it.value }
            }
    }

    override fun selectionMade(): Observable<Int> {
        return selectedYearEvent
    }

    //endregion
}