package tmg.f1stats.season.race

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import tmg.f1stats.base.BaseViewModel
import tmg.f1stats.repo.db.SeasonOverviewDB
import tmg.f1stats.repo.models.LapTime
import tmg.f1stats.repo.models.Round
import tmg.f1stats.utils.DataEvent

//region Inputs

interface RaceViewModelInputs {
    fun initialise(season: Int, round: Int)
    fun orderBy(seasonRaceAdapterType: RaceAdapterType)
}

//endregion

//region Outputs

interface RaceViewModelOutputs {
    val items: MutableLiveData<Pair<RaceAdapterType, List<RaceModel>>>
    val date: MutableLiveData<LocalDate>
    val time: MutableLiveData<LocalTime>

    val loading: MutableLiveData<DataEvent<Boolean>>
}

//endregion

class RaceViewModel(
        private val seasonOverviewDB: SeasonOverviewDB
) : BaseViewModel(), RaceViewModelInputs, RaceViewModelOutputs {

    private var viewType: RaceAdapterType = RaceAdapterType.RACE
    private var season: Int = -1
    private var round: Int = -1

    private var roundData: Round? = null

    override val items: MutableLiveData<Pair<RaceAdapterType, List<RaceModel>>> = MutableLiveData()
    override val date: MutableLiveData<LocalDate> = MutableLiveData()
    override val time: MutableLiveData<LocalTime> = MutableLiveData()

    override val loading: MutableLiveData<DataEvent<Boolean>> = MutableLiveData()

    var inputs: RaceViewModelInputs = this
    var outputs: RaceViewModelOutputs = this

    //region Inputs

    override fun initialise(season: Int, round: Int) {

        this.season = -1
        this.round = -1

        loading.value = DataEvent(true)

        viewModelScope.launch(Dispatchers.IO) {
            roundData = seasonOverviewDB.getSeasonRound(season, round)
            this@RaceViewModel.season = season
            this@RaceViewModel.round = round
            roundData?.let {
                updateModel(it, viewType)
            }
        }
    }

    override fun orderBy(seasonRaceAdapterType: RaceAdapterType) {

        viewType = seasonRaceAdapterType
        if (season != -1 && round != -1) {
            viewModelScope.launch(Dispatchers.IO) {
                roundData?.let {
                    updateModel(it, seasonRaceAdapterType)
                }
            }
        }
    }

    //endregion

    private fun updateModel(roundData: Round, withType: RaceAdapterType) {
        date.postValue(roundData.date)
        time.postValue(roundData.time)

        val driverIds: List<String> = roundData
            .race
            .values
            .sortedBy {
                when (viewType) {
                    RaceAdapterType.RACE -> it.finish
                    RaceAdapterType.QUALIFYING_POS_1 -> roundData.driverOverview(it.driver.id).q1?.position ?: Int.MAX_VALUE
                    RaceAdapterType.QUALIFYING_POS_2 -> roundData.driverOverview(it.driver.id).q2?.position ?: Int.MAX_VALUE
                    RaceAdapterType.QUALIFYING_POS -> it.qualified
                }
            }
            .map { it.driver.id }
        val list: MutableList<RaceModel> = mutableListOf()
        when (viewType) {
            RaceAdapterType.RACE -> {
                var startIndex = 0
                if (driverIds.size >= 3) {
                    list.add(
                        RaceModel.Podium(
                            driverFirst = getDriverModel(roundData, driverIds[0]),
                            driverSecond = getDriverModel(roundData, driverIds[1]),
                            driverThird = getDriverModel(roundData, driverIds[2])
                        )
                    )
                    startIndex = 3
                }
                for (i in startIndex until driverIds.size) {
                    list.add(getDriverModel(roundData, driverIds[i]))
                }
            }
            RaceAdapterType.QUALIFYING_POS_1,
            RaceAdapterType.QUALIFYING_POS_2,
            RaceAdapterType.QUALIFYING_POS -> {
                list.add(RaceModel.QualifyingHeader)
                list.addAll(driverIds.map { getDriverModel(roundData, it) })
            }
        }

        items.postValue(Pair(withType, list))
        loading.postValue(DataEvent(false))
    }

    private fun getDriverModel(round: Round, driverId: String): RaceModel.Single {
        val overview = round.driverOverview(driverId)
        return RaceModel.Single(
            season = round.season,
            round = round.round,
            driver = round.drivers.first { it.id == driverId },
            q1 = overview.q1,
            q2 = overview.q2,
            q3 = overview.q3,
            raceResult = overview.race.time ?: LapTime(),
            racePos = overview.race.finish,
            gridPos = overview.race.grid,
            qualified = overview.race.qualified,
            racePoints = overview.race.points,
            status = overview.race.status,
            fastestLap = overview.race.fastestLap?.rank == 1
        )
    }
}