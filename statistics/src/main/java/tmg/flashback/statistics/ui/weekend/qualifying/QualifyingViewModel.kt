package tmg.flashback.statistics.ui.weekend.qualifying

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import tmg.flashback.formula1.model.Race
import tmg.flashback.formula1.model.RaceQualifyingType
import tmg.flashback.formula1.model.RaceQualifyingType.*
import tmg.flashback.statistics.repo.RaceRepository
import tmg.flashback.statistics.ui.race.RaceItem

interface QualifyingViewModelInputs {
    fun load(season: Int, round: Int)
}

interface QualifyingViewModelOutputs {
    val list: LiveData<List<QualifyingModel>>
}

class QualifyingViewModel(
    private val raceRepository: RaceRepository
): ViewModel(), QualifyingViewModelInputs, QualifyingViewModelOutputs {

    val inputs: QualifyingViewModelInputs = this
    val outputs: QualifyingViewModelOutputs = this

    private val seasonRound: MutableStateFlow<Pair<Int, Int>?> = MutableStateFlow(null)
    override val list: LiveData<List<QualifyingModel>> = seasonRound
        .filterNotNull()
        .flatMapLatest { (season, round) -> raceRepository.getRace(season, round) }
        .map {
            val race = it ?: return@map emptyList<QualifyingModel>()

            return@map when {
                race.has(Q3) -> race.getQ1Q2Q3QualifyingList(Q3)
                race.has(Q2) -> race.getQ1Q2QualifyingList()
                race.has(Q1) -> race.getQ1QualifyingList()
                else -> emptyList()
            }
        }
        .asLiveData(viewModelScope.coroutineContext)

    override fun load(season: Int, round: Int) {
        seasonRound.value = Pair(season, round)
    }

    private fun Race.getQ1Q2Q3QualifyingList(forType: RaceQualifyingType): List<QualifyingModel> {
        val list = when (forType) {
            Q1, Q2, Q3 -> this.qualifying.firstOrNull { it.label == forType } ?: return emptyList()
            else -> return emptyList()
        }

        return list.results.map {
            val overview = driverOverview(it.driver.driver.id)
            return@map QualifyingModel.Q1Q2Q3(
                driver = it.driver,
                finalQualifyingPosition = overview?.qualified,
                q1 = overview?.q1,
                q2 = overview?.q2,
                q3 = overview?.q3
            )
        }
    }

    private fun Race.getQ1Q2QualifyingList(): List<QualifyingModel> {
        return qualifying.firstOrNull()
            ?.results
            ?.map {
                val overview = driverOverview(it.driver.driver.id)
                return@map QualifyingModel.Q1Q2(
                    driver = it.driver,
                    finalQualifyingPosition = overview?.qualified,
                    q1 = overview?.q1,
                    q2 = overview?.q2
                )
            }
            ?.sortedBy { it.qualified }
            ?: emptyList()
    }

    private fun Race.getQ1QualifyingList(): List<QualifyingModel> {
        return qualifying.firstOrNull()
            ?.results
            ?.map {
                val overview = driverOverview(it.driver.driver.id)
                return@map QualifyingModel.Q1(
                    driver = it.driver,
                    finalQualifyingPosition = overview?.qualified,
                    q1 = overview?.q1,
                )
            }
            ?.sortedBy { it.qualified }
            ?: emptyList()
    }

}