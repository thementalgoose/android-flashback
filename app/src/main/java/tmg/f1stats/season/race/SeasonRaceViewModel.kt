package tmg.f1stats.season.race

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import tmg.f1stats.base.BaseViewModel
import tmg.f1stats.repo.db.SeasonOverviewDB
import tmg.f1stats.repo.enums.RaceStatus
import tmg.f1stats.repo.models.LapTime
import tmg.utilities.extensions.combineWithPair
import tmg.utilities.extensions.filterMap

//region Inputs

interface SeasonRaceViewModelInputs {
    fun initialise(season: Int, round: Int)
    fun orderBy(seasonRaceAdapterType: SeasonRaceAdapterType)
}

//endregion

//region Outputs

interface SeasonRaceViewModelOutputs {
    fun drivers(): Observable<List<SeasonRaceModel>>
    fun viewType(): Observable<SeasonRaceAdapterType>
}

//endregion

class SeasonRaceViewModel(
    private val seasonOverviewDB: SeasonOverviewDB
) : BaseViewModel(),
    SeasonRaceViewModelInputs,
    SeasonRaceViewModelOutputs {

    private var viewType: BehaviorSubject<SeasonRaceAdapterType> = BehaviorSubject.createDefault(SeasonRaceAdapterType.QUALIFYING_POS)
    private var seasonRound: BehaviorSubject<Pair<Int, Int>> = BehaviorSubject.create()

    private var seasonRaceModelObservable: Observable<List<SeasonRaceModel>> = seasonRound
        .switchMap { (season, round) ->
            seasonOverviewDB.getSeasonRound(season, round)
        }
        .filterMap { it.value }
        .map { round ->
            round.drivers.map {
                val q1: Pair<LapTime, Int> = round.q1Results.getQualyResult(it.driverId) ?: Pair(LapTime(), -1)
                val q2: Pair<LapTime, Int>? = round.q2Results.getQualyResult(it.driverId)
                val q3: Pair<LapTime, Int>? = round.q3Results.getQualyResult(it.driverId)
                val race = round.raceResults.getRaceResult(it.driverId)
                SeasonRaceModel(
                    driver = it,
                    q1 = q1.first,
                    q1Pos = q1.second,
                    q2 = q2?.first,
                    q2Pos = q2?.second,
                    q3 = q3?.first,
                    q3Pos = q3?.second,
                    raceResult = race?.time ?: LapTime(),
                    racePos = race?.finishPosition ?: -1,
                    gridPos = race?.gridPosition ?: -1,
                    status = race?.status ?: RaceStatus.RETIRED
                )
            }
        }
        .combineWithPair(viewType)
        .map { (list, viewType) ->
            when (viewType) {
                SeasonRaceAdapterType.RACE -> list.sortedBy { it.racePos }
                SeasonRaceAdapterType.QUALIFYING_POS_1 -> list.sortedBy { it.q1Pos }
                SeasonRaceAdapterType.QUALIFYING_POS_2 -> list.sortedBy { it.q2Pos ?: it.q1Pos }
                SeasonRaceAdapterType.QUALIFYING_POS_3 -> list.sortedBy { it.q3Pos ?: it.q2Pos ?: it.q1Pos }
                SeasonRaceAdapterType.QUALIFYING_POS -> list.sortedBy { it.qualiGridPos }
                else -> list
            }
        }

    var inputs: SeasonRaceViewModelInputs = this
    var outputs: SeasonRaceViewModelOutputs = this

    init {

    }

    //region Inputs

    override fun initialise(season: Int, round: Int) {
        seasonRound.onNext(Pair(season, round))
    }

    override fun orderBy(seasonRaceAdapterType: SeasonRaceAdapterType) {
        viewType.onNext(seasonRaceAdapterType)
    }

    //endregion

    //region Outputs

    override fun drivers(): Observable<List<SeasonRaceModel>> {
        return seasonRaceModelObservable
    }

    override fun viewType(): Observable<SeasonRaceAdapterType> {
        return viewType
    }

    //endregion

    private fun List<RaceResult>.getRaceResult(driverId: String): RaceResult? {
        return this
            .firstOrNull { it.driver.driverId == driverId }
    }

    private fun List<QualifyingResult>.getQualyResult(driverId: String): Pair<LapTime, Int>? {
        return this
            .firstOrNull { it.driver.driverId == driverId }
            ?.let { result ->
                Pair(result.time, this.sortedBy { it.time.totalMillis }.indexOfFirst { it.driver.driverId == driverId } + 1)
            }
    }
}