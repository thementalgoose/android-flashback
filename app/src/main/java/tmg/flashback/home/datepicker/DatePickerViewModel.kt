package tmg.flashback.home.datepicker

import tmg.flashback.base.BaseViewModel

//region Inputs

interface DatePickerViewModelInputs {
//    fun initialYear(year: Int)
//    fun selectYear(year: Int)
}

//endregion

//region Outputs

interface DatePickerViewModelOutputs {
//    fun showCalendarWithOptions(): Observable<List<Selected<String>>>
//    fun selectionMade(): Observable<Int>
}

//endregion

class DatePickerViewModel : BaseViewModel(), DatePickerViewModelInputs, DatePickerViewModelOutputs {

//    private val calendarYearEvent: BehaviorSubject<Int> = BehaviorSubject.create()
//    private val selectedYearEvent: PublishSubject<Int> = PublishSubject.create()
//
//    var inputs: DatePickerViewModelInputs = this
//    var outputs: DatePickerViewModelOutputs = this
//
//    init {
//
//    }
//
//    //region Inputs
//
//    override fun initialYear(year: Int) {
//        calendarYearEvent.onNext(year)
//    }
//
//    override fun selectYear(year: Int) {
//        selectedYearEvent.onNext(year)
//    }
//
//    //endregion
//
//    //region Outputs
//
//    override fun showCalendarWithOptions(): Observable<List<Selected<String>>> {
//        return RxUtils.ongoing(supportedYears)
//            .combineWithPair(calendarYearEvent)
//            .map { (listOfYears, selectedYear) ->
//                listOfYears
//                    .map { Selected(it.toString(), isSelected = selectedYear == it) }
//                    .sortedByDescending { it.value }
//            }
//    }
//
//    override fun selectionMade(): Observable<Int> {
//        return selectedYearEvent
//    }
//
//    //endregion
}