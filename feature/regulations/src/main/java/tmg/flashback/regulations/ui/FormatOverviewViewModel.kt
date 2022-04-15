package tmg.flashback.regulations.ui

import androidx.lifecycle.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import tmg.flashback.regulations.domain.Item
import tmg.flashback.regulations.domain.Season
import tmg.flashback.regulations.domain.Section
import tmg.utilities.models.StringHolder

//region Inputs

internal interface FormatOverviewViewModelInputs {
    fun init(season: Int)

    fun setSection(label: Int, newState: Boolean)
}

//endregion

//region Outputs

internal interface FormatOverviewViewModelOutputs {
    val items: LiveData<List<Item>>
}

//endregion

internal class FormatOverviewViewModel: ViewModel(), FormatOverviewViewModelInputs, FormatOverviewViewModelOutputs {

    private val expandedSections: MutableStateFlow<Set<Int>?> = MutableStateFlow(null)
    private val season: MutableStateFlow<Season?> = MutableStateFlow(null)

    override val items: LiveData<List<Item>> = season.filterNotNull()
        .map { season ->
            val list = mutableListOf<Item>()
            season.sections.forEach { sect ->
                    list.add(Item.Collapsible(sect.label, true))
                    list.addAll(sect.items)
            }
            return@map list
        }
        .asLiveData(viewModelScope.coroutineContext)
//    override val items: LiveData<List<Item>> = combine(expandedSections.filterNotNull(), season.filterNotNull()) { sections, season -> Pair(sections, season) }
//        .map { (expandedSection, season) ->
//            val list = mutableListOf<Item>()
//            season.sections.forEach { sect ->
//                if (expandedSection.contains(sect.label)) {
//                    list.add(Item.Collapsible(sect.label, true))
//                    list.addAll(sect.items)
//                } else {
//                    list.add(Item.Collapsible(sect.label, false))
//                }
//            }
//            return@map list
//        }
//        .asLiveData(viewModelScope.coroutineContext)

    val inputs: FormatOverviewViewModelInputs = this
    val outputs: FormatOverviewViewModelOutputs = this

    override fun init(season: Int) {
        val section = Season.values().firstOrNull { it.season == season } ?: return
        this.expandedSections.value = section.sections.lastOrNull()?.let { setOf(it.label) } ?: emptySet()
        this.season.value = section
    }

    override fun setSection(label: Int, newState: Boolean) {
        if (newState) {
            this.expandedSections.value = setOf(label)
        } else {
            this.expandedSections.value = emptySet()
        }
    }
}