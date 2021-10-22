package tmg.flashback.statistics.ui.search.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tmg.core.ui.bottomsheet.BottomSheetItem
import tmg.flashback.statistics.ui.search.SearchCategory
import tmg.utilities.lifecycle.DataEvent
import tmg.utilities.lifecycle.Event
import tmg.utilities.models.Selected
import tmg.utilities.models.StringHolder

//region Inputs

interface CategoryViewModelInputs {
    fun initList(category: SearchCategory?)
    fun clickCategory(category: SearchCategory)
}

//endregion

//region Outputs

interface CategoryViewModelOutputs {
    val categories: LiveData<List<Selected<BottomSheetItem>>>
    val categorySelected: LiveData<DataEvent<SearchCategory>>
}

//endregion

class CategoryViewModel: ViewModel(), CategoryViewModelInputs, CategoryViewModelOutputs {

    var inputs: CategoryViewModelInputs = this
    var outputs: CategoryViewModelOutputs = this

    private var category: SearchCategory? = null

    override val categories: MutableLiveData<List<Selected<BottomSheetItem>>> = MutableLiveData()
    override val categorySelected: MutableLiveData<DataEvent<SearchCategory>> = MutableLiveData()

    //region Inputs

    override fun initList(category: SearchCategory?) {
        this.category = category
        categories.value = SearchCategory.values()
            .map {
                Selected(BottomSheetItem(it.ordinal, it.icon, StringHolder(it.label)), it == category)
            }
    }

    override fun clickCategory(category: SearchCategory) {
        this.category = category
        categorySelected.value = DataEvent(category)
    }

    //endregion
}
