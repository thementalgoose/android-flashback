package tmg.flashback.stats.ui.weekend.race

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import tmg.flashback.formula1.constants.Formula1
import tmg.flashback.statistics.repo.RaceRepository
import tmg.flashback.stats.ui.weekend.qualifying.QualifyingModel

interface RaceViewModelInputs {
    fun load(season: Int, round: Int)
}

interface RaceViewModelOutputs {
    val list: LiveData<List<RaceModel>>
}

class RaceViewModel(
    private val raceRepository: RaceRepository,
    private val ioDispatcher: CoroutineDispatcher
): ViewModel(), RaceViewModelInputs, RaceViewModelOutputs {

    val inputs: RaceViewModelInputs = this
    val outputs: RaceViewModelOutputs = this

    private val seasonRound: MutableStateFlow<Pair<Int, Int>?> = MutableStateFlow(null)
    override val list: LiveData<List<RaceModel>> = seasonRound
        .filterNotNull()
        .flatMapLatest { (season, round) -> raceRepository.getRace(season, round) }
        .flowOn(ioDispatcher)
        .map { race ->
            val raceResults = race?.race ?: emptyList()
            if (race == null || race.race.isEmpty()) {
                val list = mutableListOf<RaceModel>().apply {
                    if ((seasonRound.value?.first ?: Formula1.currentSeasonYear) >= Formula1.currentSeasonYear) {
                        add(RaceModel.NotAvailableYet)
                    } else {
                        add(RaceModel.NotAvailable)
                    }
                }
                return@map list
            }

            val list: MutableList<RaceModel> = mutableListOf()
            if (raceResults.size >= 3) {
                val podium = RaceModel.Podium(
                    p1 = raceResults[0],
                    p2 = raceResults[1],
                    p3 = raceResults[2]
                )
                list.add(podium)
                for (x in raceResults.drop(3)) {
                    list.add(RaceModel.Result(x))
                }
            } else {
                list.addAll(raceResults.map { RaceModel.Result(it) })
            }
            return@map list
        }
        .asLiveData(viewModelScope.coroutineContext)

    override fun load(season: Int, round: Int) {
        seasonRound.value = Pair(season, round)
    }
}