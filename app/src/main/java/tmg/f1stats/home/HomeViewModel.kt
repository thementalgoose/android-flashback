package tmg.f1stats.home

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import tmg.f1stats.base.BaseViewModel

//region Inputs

interface HomeViewModelInputs {
    fun clickTab(tabOption: HomeTabOption)
}

//endregion

//region Outputs

interface HomeViewModelOutputs {
}

//endregion

class HomeViewModel : BaseViewModel(), HomeViewModelInputs, HomeViewModelOutputs {

    private var tabSelectedEvent: BehaviorSubject<HomeTabOption> =
        BehaviorSubject.createDefault(HomeTabOption.DRIVERS)

    var inputs: HomeViewModelInputs = this
    var outputs: HomeViewModelOutputs = this

    init {

    }

    //region Inputs

    override fun clickTab(tabOption: HomeTabOption) {
        tabSelectedEvent.onNext(tabOption)
    }

    //endregion
}


enum class HomeTabOption {
    DRIVERS,
    CONSTRUCTORS,
    GALLERY,
    SETTINGS
}