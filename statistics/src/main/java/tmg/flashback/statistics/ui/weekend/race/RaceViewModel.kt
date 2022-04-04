package tmg.flashback.statistics.ui.weekend.race

import androidx.lifecycle.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import tmg.flashback.statistics.repo.RaceRepository

interface RaceViewModelInputs {
    fun load(season: Int, round: Int)
}

interface RaceViewModelOutputs {
    val list: LiveData<List<RaceModel>>
}

class RaceViewModel(
    private val raceRepository: RaceRepository
): ViewModel(), RaceViewModelInputs, RaceViewModelOutputs {

    val inputs: RaceViewModelInputs = this
    val outputs: RaceViewModelOutputs = this

    private val seasonRound: MutableStateFlow<Pair<Int, Int>?> = MutableStateFlow(null)
    override val list: LiveData<List<RaceModel>> = seasonRound
        .filterNotNull()
        .flatMapLatest { (season, round) -> raceRepository.getRace(season, round) }
        .map { race ->
            val raceResults = race?.race ?: return@map emptyList<RaceModel>()

            val list: MutableList<RaceModel> = mutableListOf<RaceModel>()
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
            return@map raceResults.map {
                RaceModel.Result(it)
            }
        }
        .asLiveData(viewModelScope.coroutineContext)

    override fun load(season: Int, round: Int) {
        seasonRound.value = Pair(season, round)
    }
}