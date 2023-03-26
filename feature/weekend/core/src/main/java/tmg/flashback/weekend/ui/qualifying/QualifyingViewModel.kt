package tmg.flashback.weekend.ui.qualifying

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import tmg.flashback.drivers.contract.DriverSeason
import tmg.flashback.drivers.contract.with
import tmg.flashback.formula1.constants.Formula1.currentSeasonYear
import tmg.flashback.formula1.model.Driver
import tmg.flashback.formula1.model.Race
import tmg.flashback.formula1.model.RaceQualifyingType
import tmg.flashback.navigation.Screen
import tmg.flashback.statistics.repo.RaceRepository
import javax.inject.Inject

interface QualifyingViewModelInputs {
    fun load(season: Int, round: Int)
    fun clickDriver(result: Driver)
}

interface QualifyingViewModelOutputs {
    val list: LiveData<List<QualifyingModel>>
    val headersToShow: LiveData<QualifyingHeader>
}

typealias QualifyingHeader = Triple<Boolean, Boolean, Boolean>

@HiltViewModel
class QualifyingViewModel @Inject constructor(
    private val raceRepository: RaceRepository,
    private val navigator: tmg.flashback.navigation.Navigator,
    private val ioDispatcher: CoroutineDispatcher
): ViewModel(), QualifyingViewModelInputs, QualifyingViewModelOutputs {

    val inputs: QualifyingViewModelInputs = this
    val outputs: QualifyingViewModelOutputs = this

    private val seasonRound: MutableStateFlow<Pair<Int, Int>?> = MutableStateFlow(null)
    override val list: LiveData<List<QualifyingModel>> = seasonRound
        .filterNotNull()
        .flatMapLatest { (season, round) -> raceRepository.getRace(season, round) }
        .flowOn(ioDispatcher)
        .map { race ->
            if (race == null || race.qualifying.isEmpty()) {
                val list = mutableListOf<QualifyingModel>().apply {
                    if ((seasonRound.value?.first ?: currentSeasonYear) >= currentSeasonYear) {
                        add(QualifyingModel.NotAvailableYet)
                    } else {
                        add(QualifyingModel.NotAvailable)
                    }
                }
                return@map list
            }

            when {
                race.has(RaceQualifyingType.Q3) -> headersToShow.postValue(
                    QualifyingHeader(
                    first = true,
                    second = true,
                    third = true
                )
                )
                race.has(RaceQualifyingType.Q2) -> headersToShow.postValue(
                    QualifyingHeader(
                    first = true,
                    second = true,
                    third = false
                )
                )
                race.has(RaceQualifyingType.Q1) -> headersToShow.postValue(
                    QualifyingHeader(
                    first = true,
                    second = false,
                    third = false
                )
                )
            }

            return@map when {
                race.has(RaceQualifyingType.Q3) -> race.getQ1Q2Q3QualifyingList(RaceQualifyingType.Q3)
                race.has(RaceQualifyingType.Q2) -> race.getQ1Q2QualifyingList()
                race.has(RaceQualifyingType.Q1) -> race.getQ1QualifyingList()
                else -> listOf(QualifyingModel.NotAvailable)
            }
        }
        .asLiveData(viewModelScope.coroutineContext)

    override val headersToShow: MutableLiveData<QualifyingHeader> = MutableLiveData()

    override fun load(season: Int, round: Int) {
        seasonRound.value = Pair(season, round)
    }

    private fun Race.getQ1Q2Q3QualifyingList(forType: RaceQualifyingType): List<QualifyingModel> {
        val list = when (forType) {
            RaceQualifyingType.Q1, RaceQualifyingType.Q2, RaceQualifyingType.Q3 -> this.qualifying.firstOrNull { it.label == forType } ?: return emptyList()
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

    override fun clickDriver(result: Driver) {
        val season = seasonRound.value?.first ?: return
        navigator.navigate(
            Screen.DriverSeason.with(
            driverId = result.id,
            driverName = result.name,
            season = season
        ))
    }

}