package tmg.flashback.search.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import tmg.flashback.ads.ads.repository.AdsRepository
import javax.inject.Inject

interface SearchViewModelInputs {
    fun searchTermUpdated(input: String)
    fun searchTermClear()
    fun selectType(type: SearchScreenStateCategory)
}

interface SearchViewModelOutputs {
    val uiState: StateFlow<SearchScreenState>
}

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val adsRepository: AdsRepository,
): ViewModel(), SearchViewModelInputs, SearchViewModelOutputs {

    val inputs: SearchViewModelInputs = this
    val outputs: SearchViewModelOutputs = this

    private val isAdsEnabled: Boolean by lazy {
        adsRepository.areAdvertsEnabled && adsRepository.advertConfig.onSearch
    }
    private val _uiState: MutableStateFlow<SearchScreenState> = MutableStateFlow(
        SearchScreenState(
            category = SearchScreenStateCategory.DRIVERS,
            showAdvert = isAdsEnabled,
        )
    )
    override val uiState: StateFlow<SearchScreenState> = _uiState

    override fun selectType(type: SearchScreenStateCategory) {
        _uiState.value = _uiState.value.copy(
            category = type
        )
    }

    override fun searchTermUpdated(input: String) {
        _uiState.value = _uiState.value.copy(
            searchTerm = input,
        )
    }

    override fun searchTermClear() =
        searchTermUpdated("")
}