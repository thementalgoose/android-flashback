package tmg.flashback.weekend.ui.qualifying

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import tmg.flashback.domain.repo.RaceRepository
import tmg.flashback.drivers.contract.DriverSeason
import tmg.flashback.drivers.contract.with
import tmg.flashback.formula1.constants.Formula1.currentSeasonYear
import tmg.flashback.formula1.model.Driver
import tmg.flashback.formula1.model.QualifyingType
import tmg.flashback.formula1.model.Race
import tmg.flashback.navigation.Screen
import javax.inject.Inject

interface QualifyingViewModelInputs {
    fun load(season: Int, round: Int)
    fun clickDriver(result: Driver)
}

interface QualifyingViewModelOutputs {
    val list: StateFlow<List<QualifyingModel>>
    val headersToShow: StateFlow<QualifyingHeader>
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
    override val list: StateFlow<List<QualifyingModel>> = seasonRound
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
                race.has(QualifyingType.Q3) -> headersToShow.value = QualifyingHeader(
                    first = true,
                    second = true,
                    third = true
                )
                race.has(QualifyingType.Q2) -> headersToShow.value = QualifyingHeader(
                    first = true,
                    second = true,
                    third = false
                )
                race.has(QualifyingType.Q1) -> headersToShow.value = QualifyingHeader(
                    first = true,
                    second = false,
                    third = false
                )
            }

            return@map when {
                race.has(QualifyingType.Q3) -> race.getQ1Q2Q3QualifyingList(QualifyingType.Q3)
                race.has(QualifyingType.Q2) -> race.getQ1Q2QualifyingList()
                race.has(QualifyingType.Q1) -> race.getQ1QualifyingList()
                else -> listOf(QualifyingModel.NotAvailable)
            }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    override val headersToShow: MutableStateFlow<QualifyingHeader> = MutableStateFlow(QualifyingHeader(true, true, true))

    override fun load(season: Int, round: Int) {
        seasonRound.value = Pair(season, round)
    }

    private fun Race.getQ1Q2Q3QualifyingList(forType: QualifyingType): List<QualifyingModel> {
        val list = when (forType) {
            QualifyingType.Q1, QualifyingType.Q2, QualifyingType.Q3 -> this.qualifying.firstOrNull { it.label == forType } ?: return emptyList()
            else -> return emptyList()
        }

        return list.results.map {
            val overview = driverOverview(it.entry.driver.id)
            return@map QualifyingModel.Q1Q2Q3(
                driver = it.entry,
                finalQualifyingPosition = overview?.qualified,
                q1 = overview?.q1,
                q2 = overview?.q2,
                q3 = overview?.q3,
                grid = overview?.race?.grid,
                sprintRaceGrid = when (this.raceInfo.season) {
                     2021, 2022 -> overview?.sprintRace?.grid
                     else -> null
                }
            )
        }
    }

    private fun Race.getQ1Q2QualifyingList(): List<QualifyingModel> {
        return qualifying.firstOrNull()
            ?.results
            ?.map {
                val overview = driverOverview(it.entry.driver.id)
                return@map QualifyingModel.Q1Q2(
                    driver = it.entry,
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
                val overview = driverOverview(it.entry.driver.id)
                return@map QualifyingModel.Q1(
                    driver = it.entry,
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