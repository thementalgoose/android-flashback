package tmg.flashback.regulations.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tmg.flashback.regulations.domain.Item
import tmg.utilities.models.StringHolder

//region Inputs

internal interface FormatOverviewViewModelInputs {
    fun init(season: Int)
}

//endregion

//region Outputs

internal interface FormatOverviewViewModelOutputs {
    val items: LiveData<List<Item>>
}

//endregion

internal class FormatOverviewViewModel: ViewModel(), FormatOverviewViewModelInputs, FormatOverviewViewModelOutputs {

    override val items: MutableLiveData<List<Item>> = MutableLiveData()

    val inputs: FormatOverviewViewModelInputs = this
    val outputs: FormatOverviewViewModelOutputs = this

    override fun init(season: Int) {
        items.value = listOf(
            Item.Stat(
                label = 0,
                value = StringHolder("TEST"),
                icon = 0
            )
        )
    }
}