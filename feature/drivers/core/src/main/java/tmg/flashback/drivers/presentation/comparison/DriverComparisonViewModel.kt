package tmg.flashback.drivers.presentation.comparison

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import tmg.flashback.device.managers.NetworkConnectivityManager
import tmg.flashback.domain.repo.OverviewRepository
import tmg.flashback.domain.repo.SeasonRepository
import tmg.flashback.formula1.enums.isStatusFinished
import tmg.flashback.formula1.model.Driver
import tmg.flashback.formula1.model.RaceResult
import tmg.flashback.formula1.model.Season
import tmg.flashback.formula1.model.SeasonDriverStandings
import javax.inject.Inject

interface DriverComparisonViewModelInputs {
    fun setup(season: Int)

    fun selectDriverLeft(driverId: String?)
    fun selectDriverRight(driverId: String?)

    fun swapDrivers()

    fun refresh()
}

interface DriverComparisonViewModelOutputs {
    val uiState: StateFlow<DriverComparisonScreenState>
}

@HiltViewModel
class DriverComparisonViewModel @Inject constructor(
    private val seasonRepository: SeasonRepository,
    private val networkConnectivityManager: NetworkConnectivityManager,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): ViewModel(), DriverComparisonViewModelInputs, DriverComparisonViewModelOutputs {

    val inputs: DriverComparisonViewModelInputs = this
    val outputs: DriverComparisonViewModelOutputs = this

    private var season: Season? = null
    private var driverStandings: SeasonDriverStandings? = null
    private var seasonValue: Int? = null

    override val uiState: MutableStateFlow<DriverComparisonScreenState> = MutableStateFlow(
        DriverComparisonScreenState(
            isLoading = true,
            networkAvailable = networkConnectivityManager.isConnected,
            driverList = emptyList(),
        )
    )

    override fun setup(season: Int) {
        if (this.seasonValue != season) {
            this.seasonValue = season
            this.uiState.value = DriverComparisonScreenState(
                isLoading = true,
                networkAvailable = networkConnectivityManager.isConnected,
                driverList = emptyList(),
            )
            refresh()
        }
    }

    override fun selectDriverLeft(driverId: String?) {
        if (driverId != null && uiState.value.driverRight?.id == driverId) {
            return
        }
        populate(
            driverLeft = uiState.value.driverList.firstOrNull { it.id == driverId }
        )
    }

    override fun selectDriverRight(driverId: String?) {
        if (driverId != null && uiState.value.driverLeft?.id == driverId) {
            return
        }
        populate(
            driverRight = uiState.value.driverList.firstOrNull { it.id == driverId }
        )
    }

    override fun swapDrivers() {
        populate(
            driverLeft = uiState.value.driverRight,
            driverRight = uiState.value.driverLeft
        )
    }

    override fun refresh() {
        viewModelScope.launch(ioDispatcher) {
            val seasonValue = seasonValue ?: return@launch
            season = seasonRepository.getSeason(seasonValue).firstOrNull()
            driverStandings = seasonRepository.getDriverStandings(seasonValue).firstOrNull()
            populate()

            uiState.value = uiState.value.copy(
                isLoading = true,
                networkAvailable = networkConnectivityManager.isConnected
            )
            seasonRepository.fetchRaces(seasonValue)

            season = seasonRepository.getSeason(seasonValue).firstOrNull()
            driverStandings = seasonRepository.getDriverStandings(seasonValue).firstOrNull()
            populate()
        }
    }

    private fun populate(
        driverLeft: Driver? = uiState.value.driverLeft,
        driverRight: Driver? = uiState.value.driverRight,
    ) {
        val driverList = (this.season?.drivers ?: emptyList())
            .toList()
            .sortedBy { it.name }

        uiState.value = uiState.value.copy(
            isLoading = false,
            networkAvailable = networkConnectivityManager.isConnected,
            driverList = driverList,
            driverLeft = driverLeft,
            driverRight = driverRight,
            comparison = processStats(season, driverLeft, driverRight)
        )
    }

    private fun processStats(
        season: Season?,
        driverLeft: Driver?,
        driverRight: Driver?
    ): Comparison? {
        if (season == null || driverLeft == null || driverRight == null) {
            return null
        }
        if (season.races.all { it.race.isEmpty() }) {
            return null
        }
        if (season.races.none {
            val driverLeftInRace = it.race.firstOrNull { race -> race.entry.driver.id == driverLeft.id }
            val driverRightInRace = it.race.firstOrNull { race -> race.entry.driver.id == driverRight.id }
            driverLeftInRace != null && driverRightInRace != null
        }) {
            return null
        }

        // Race head to head
        val (driverLeftFinishes, driverRightFinishes) = season
            .racesForDriver(driverLeft, driverRight)
            .map { (left, right) ->
                when {
                    left.finish < right.finish -> Pair(1, 0)
                    else -> Pair(0, 1)
                }
            }
            .collapseInts()

        // Qualifying head to head
        val (driverLeftQualifying, driverRightQualifying) = season
            .racesForDriver(driverLeft, driverRight)
            .map { (left, right) ->
                when {
                    left.qualified == null && right.qualified == null -> Pair(0, 0)
                    left.qualified != null && right.qualified == null -> Pair(1, 0)
                    left.qualified == null && right.qualified != null -> Pair(0, 1)
                    left.qualified!! < right.qualified!! -> Pair(1, 0)
                    left.qualified!! > right.qualified!! -> Pair(0, 1)
                    else -> Pair(1, 1)
                }
            }
            .collapseInts()

        // Points
        val driverLeftPoints = driverStandings
            ?.standings
            ?.firstOrNull { it.driver.id == driverLeft.id }
            ?.points
        val driverRightPoints = driverStandings
            ?.standings
            ?.firstOrNull { it.driver.id == driverRight.id }
            ?.points

        // Points finishes
        val (driverLeftPointsFinishes, driverRightPointsFinishes) = season
            .racesForDriver(driverLeft, driverRight)
            .map { (left, right) ->
                Pair(if (left.points > 0.0) 1 else 0, if (right.points > 0.0) 1 else 0)
            }
            .collapseInts()

        // Podiums
        val (driverLeftPodiums, driverRightPodiums) = season
            .racesForDriver(driverLeft, driverRight)
            .map { (left, right) ->
                Pair(if (left.finish <= 3) 1 else 0, if (right.finish <= 3) 1 else 0)
            }
            .collapseInts()

        // Wins
        val (driverLeftWins, driverRightWins) = season
            .racesForDriver(driverLeft, driverRight)
            .map { (left, right) ->
                Pair(if (left.finish == 1) 1 else 0, if (right.finish == 1) 1 else 0)
            }
            .collapseInts()

        // DNFs
        val (driverLeftDNFs, driverRightDNFs) = season
            .racesForDriver(driverLeft, driverRight)
            .map { (left, right) ->
                Pair(if (!left.status.isStatusFinished()) 1 else 0, if (!right.status.isStatusFinished()) 1 else 0)
            }
            .collapseInts()

        val leftConstructors = season
            .racesForDriver(driverLeft, driverRight)
            .map { (left, _) ->
                left.entry.constructor
            }
            .distinct()
        val rightConstructors = season
            .racesForDriver(driverLeft, driverRight)
            .map { (_, right) ->
                right.entry.constructor
            }
            .distinct()

        return Comparison(
            left = ComparisonValue(
                racesHeadToHead = driverLeftFinishes,
                qualifyingHeadToHead = driverLeftQualifying,
                points = driverLeftPoints,
                pointsFinishes = driverLeftPointsFinishes,
                podiums = driverLeftPodiums,
                wins = driverLeftWins,
                dnfs = driverLeftDNFs
            ),
            leftConstructors = leftConstructors,
            right = ComparisonValue(
                racesHeadToHead = driverRightFinishes,
                qualifyingHeadToHead = driverRightQualifying,
                points = driverRightPoints,
                pointsFinishes = driverRightPointsFinishes,
                podiums = driverRightPodiums,
                wins = driverRightWins,
                dnfs = driverRightDNFs
            ),
            rightConstructors = rightConstructors
        )
    }

    private fun Season.racesForDriver(
        driverLeft: Driver,
        driverRight: Driver
    ): List<Pair<RaceResult, RaceResult>> {
        return this.races
            .filter { it.race.isNotEmpty() }
            .mapNotNull {
                val left = it.race.firstOrNull { it.entry.driver.id == driverLeft.id }
                val right = it.race.firstOrNull { it.entry.driver.id == driverRight.id }

                if (left != null && right != null) {
                    return@mapNotNull (left to right)
                } else {
                    return@mapNotNull null
                }
            }
    }

    private fun List<Pair<Int,Int>>.collapseInts(): Pair<Int, Int> {
        return this.reduce { acc, pair ->
            Pair(acc.first + pair.first, acc.second + pair.second)
        }
    }

    private fun List<Pair<Double,Double>>.collapseDoubles(): Pair<Double, Double> {
        return this.reduce { acc, pair ->
            Pair(acc.first + pair.first, acc.second + pair.second)
        }
    }
}