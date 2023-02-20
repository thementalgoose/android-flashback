package tmg.flashback.stats.ui.constructors.season

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import tmg.flashback.device.managers.NetworkConnectivityManager
import tmg.flashback.formula1.extensions.pointsDisplay
import tmg.flashback.formula1.model.ConstructorHistorySeason
import tmg.flashback.statistics.repo.ConstructorRepository
import tmg.flashback.stats.R
import tmg.flashback.web.usecases.OpenWebpageUseCase
import tmg.utilities.extensions.ordinalAbbreviation
import javax.inject.Inject

interface ConstructorSeasonViewModelInputs {
    fun setup(constructorId: String, season: Int)
    fun openUrl(url: String)
    fun refresh()
}

interface ConstructorSeasonViewModelOutputs {
    val list: LiveData<List<ConstructorSeasonModel>>
    val showLoading: LiveData<Boolean>
}

@HiltViewModel
class ConstructorSeasonViewModel @Inject constructor(
    private val constructorRepository: ConstructorRepository,
    private val networkConnectivityManager: NetworkConnectivityManager,
    private val openWebpageUseCase: OpenWebpageUseCase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): ViewModel(), ConstructorSeasonViewModelInputs, ConstructorSeasonViewModelOutputs {

    val inputs: ConstructorSeasonViewModelInputs = this
    val outputs: ConstructorSeasonViewModelOutputs = this

    override val showLoading: MutableLiveData<Boolean> = MutableLiveData(false)

    private val constructorId: MutableStateFlow<Pair<String, Int>?> = MutableStateFlow(null)
    private val constructorIdWithRequest: Flow<Pair<String, Int>?> = constructorId
        .filterNotNull()
        .flatMapLatest { id ->
            return@flatMapLatest flow {
                if (constructorRepository.getConstructorSeasonCount(id.first) == 0) {
                    showLoading.postValue(true)
                    emit(null)
                    constructorRepository.fetchConstructor(id.first)
                    showLoading.postValue(false)
                    emit(id)
                }
                else {
                    emit(id)
                    showLoading.postValue(true)
                    constructorRepository.fetchConstructor(id.first)
                    showLoading.postValue(false)
                }
            }
        }
        .flowOn(ioDispatcher)

    private val isConnected: Boolean
        get() = networkConnectivityManager.isConnected

    override val list: LiveData<List<ConstructorSeasonModel>> = constructorIdWithRequest
        .flatMapLatest { idAndSeason ->
            if (idAndSeason == null) {
                return@flatMapLatest flow {
                    emit(mutableListOf<ConstructorSeasonModel>(ConstructorSeasonModel.Loading))
                }
            }

            val (id, season) = idAndSeason
            return@flatMapLatest constructorRepository.getConstructorOverview(id)
                .map { history ->
                    val list: MutableList<ConstructorSeasonModel> = mutableListOf()
                    if (history != null) {
                        list.add(
                            ConstructorSeasonModel.Header(
                                constructorName = history.constructor.name,
                                constructorPhotoUrl = history.constructor.photoUrl,
                                constructorColor = history.constructor.color,
                                constructorNationality = history.constructor.nationality,
                                constructorNationalityISO = history.constructor.nationalityISO,
                                constructorWikiUrl = history.constructor.wikiUrl
                            )
                        )
                    }
                    val currentSeason = history?.standings?.firstOrNull { it.season == season }
                    when {
                        currentSeason == null && !isConnected -> list.add(ConstructorSeasonModel.NetworkError)
                        currentSeason == null -> list.add(ConstructorSeasonModel.InternalError)
                        else -> {
                            if (currentSeason.isInProgress) {
                                val lastStanding = history.standings.maxByOrNull { it.season }
                                if (lastStanding != null) {
                                    list.add(ConstructorSeasonModel.Message(R.string.results_accurate_for_round, listOf(lastStanding.season, lastStanding.races)))
                                }
                            }
                            list.addAll(getAllStats(currentSeason))
                            list.addAll(getDriverList(currentSeason))
                        }
                    }
                    return@map list
                }
        }
        .asLiveData(viewModelScope.coroutineContext)

    override fun setup(constructorId: String, season: Int) {
        this.constructorId.value = Pair(constructorId, season)
    }

    override fun openUrl(url: String) {
        openWebpageUseCase.open(url = url, title = "")
    }

    override fun refresh() {
        this.refresh(constructorId.value)
    }
    private fun refresh(constructorId: Pair<String, Int>? = this.constructorId.value) {
        viewModelScope.launch(context = ioDispatcher) {
            constructorId?.let {
                constructorRepository.fetchConstructor(it.first)
                showLoading.postValue(false)
            }
        }
    }

    /**
     * Add career stats for the driver across their career
     */
    private fun getAllStats(history: ConstructorHistorySeason): List<ConstructorSeasonModel> {
        val list: MutableList<ConstructorSeasonModel> = mutableListOf()

        if (history.isInProgress) {
            list.addStat(
                isWinning = false,
                icon = R.drawable.ic_menu_constructors,
                label = R.string.constructor_overview_stat_championship_standing_so_far,
                value = history.championshipStanding?.ordinalAbbreviation ?: ""
            )
        } else {
            list.addStat(
                isWinning = history.championshipStanding == 1,
                icon = R.drawable.ic_menu_constructors,
                label = R.string.constructor_overview_stat_championship_standing,
                value = history.championshipStanding?.ordinalAbbreviation ?: ""
            )
        }

        list.addStat(
            icon = R.drawable.ic_race_grid,
            label = R.string.constructor_overview_stat_races,
            value = history.races.toString()
        )
        list.addStat(
            icon = R.drawable.ic_standings,
            label = R.string.constructor_overview_stat_race_wins,
            value = history.wins.toString()
        )
        list.addStat(
            icon = R.drawable.ic_podium,
            label = R.string.constructor_overview_stat_race_podiums,
            value = history.podiums.toString()
        )
        list.addStat(
            icon = R.drawable.ic_race_points,
            label = R.string.constructor_overview_stat_points,
            value = history.points.pointsDisplay()
        )
        list.addStat(
            icon = R.drawable.ic_finishes_in_points,
            label = R.string.constructor_overview_stat_points_finishes,
            value = history.finishInPoints.toString()
        )
        list.addStat(
            icon = R.drawable.ic_qualifying_pole,
            label = R.string.constructor_overview_stat_qualifying_poles,
            value = history.qualifyingPole.toString()
        )

        return list
    }

    private fun MutableList<ConstructorSeasonModel>.addStat(isWinning: Boolean = false, @DrawableRes icon: Int, @StringRes label: Int, value: String) {
        this.add(
            ConstructorSeasonModel.Stat(
                isWinning = isWinning,
                icon = icon,
                label = label,
                value = value
            ))
    }

    private fun getDriverList(history: ConstructorHistorySeason): List<ConstructorSeasonModel> {
        return history.drivers.values
            .sortedBy { it.championshipStanding ?: Int.MAX_VALUE }
            .map { driver ->
                ConstructorSeasonModel.Driver(driver)
            }
    }
}