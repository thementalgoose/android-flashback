package tmg.flashback.stats.ui.drivers.comparison

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import tmg.flashback.device.managers.NetworkConnectivityManager
import tmg.flashback.formula1.enums.isStatusFinished
import tmg.flashback.formula1.enums.raceStatusFinish
import tmg.flashback.formula1.extensions.compareDrivers
import tmg.flashback.formula1.model.Driver
import tmg.flashback.formula1.model.DriverConstructor
import tmg.flashback.formula1.model.DriverConstructors
import tmg.flashback.formula1.model.Race
import tmg.flashback.formula1.model.Season
import tmg.flashback.statistics.repo.RaceRepository
import tmg.flashback.statistics.repo.SeasonRepository
import tmg.flashback.stats.R
import tmg.flashback.style.AppTheme
import javax.inject.Inject
import kotlin.math.max
import kotlin.math.min

private typealias DriverId = String

internal interface DriverComparisonViewModelInputs {
    fun setup(driverId1: String, driverId2: String, season: Int)
}

internal interface DriverComparisonViewModelOutputs {
    val showLoading: LiveData<Boolean>
    val list: LiveData<List<DriverComparisonModel>>
}

@HiltViewModel
internal class DriverComparisonViewModel @Inject constructor(
    private val raceRepository: RaceRepository,
    private val networkConnectivityManager: NetworkConnectivityManager,
    private val seasonRepository: SeasonRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): ViewModel(), DriverComparisonViewModelInputs, DriverComparisonViewModelOutputs {

    val inputs: DriverComparisonViewModelInputs = this
    val outputs: DriverComparisonViewModelOutputs = this

    override val showLoading: MutableLiveData<Boolean> = MutableLiveData()

    private val isConnected: Boolean
        get() = networkConnectivityManager.isConnected

    private val seasonAndDrivers: MutableStateFlow<Triple<Int, DriverId, DriverId>?> = MutableStateFlow(null)
    private val seasonAndDriverWithRequest = seasonAndDrivers
        .filterNotNull()
        .flatMapLatest { triplet ->
            val (season, _, _) = triplet
            return@flatMapLatest flow {
                if (raceRepository.hasntPreviouslySynced(season)) {
                    showLoading.postValue(true)
                    emit(null)
                    seasonRepository.fetchRaces(season)
                    showLoading.postValue(false)
                    emit(triplet)
                } else {
                    emit(triplet)
                    showLoading.postValue(true)
                    seasonRepository.fetchRaces(season)
                    showLoading.postValue(false)
                }
            }
        }
        .flowOn(ioDispatcher)

    override fun setup(driverId1: String, driverId2: String, season: Int) {
        seasonAndDrivers.value = Triple(season, driverId1, driverId2)
    }

    override val list: LiveData<List<DriverComparisonModel>> = seasonAndDriverWithRequest
        .flatMapLatest {
            if (it == null) {
                return@flatMapLatest flow {
                    emit(mutableListOf(DriverComparisonModel.Loading))
                }
            }

            val (year, driverId1, driverId2) = it

            return@flatMapLatest seasonRepository.getSeason(year)
                .map { season ->
                    val list: MutableList<DriverComparisonModel> = mutableListOf()
                    val driver1: Driver? = season?.drivers?.firstOrNull { it.id == driverId1 }
                    val driver2: Driver? = season?.drivers?.firstOrNull { it.id == driverId2 }
                    if (season != null) {
                        list.add(
                            DriverComparisonModel.Header(
                                year = season.season,
                                driver1 = driver1,
                                driver2 = driver2
                            )
                        )
                    }
                    when {
                        season == null && !isConnected -> list.add(DriverComparisonModel.NetworkError)
                        season == null || driver1 == null || driver2 == null -> list.add(DriverComparisonModel.ConfigurationError)
                        else -> {

                        }
                    }
                    return@map list
                }
        }
        .asLiveData(viewModelScope.coroutineContext)

    private fun getAllStats(season: Season, driver1: Driver, driver2: Driver): List<DriverComparisonModel> {
        val comparison = season.compareDrivers(driver1.id, driver2.id) ?: return emptyList()
        val list: MutableList<DriverComparisonModel> = mutableListOf()

        list.add(DriverComparisonModel.ComparisonPoint(
            label = R.string.driver_comparison_best_finish_position,
            driver1 = ComparisonResult(

            )
        ))

        return list
    }
}

