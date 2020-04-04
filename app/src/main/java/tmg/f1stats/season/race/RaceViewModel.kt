package tmg.f1stats.season.race

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import tmg.f1stats.base.BaseViewModel
import tmg.f1stats.repo.db.SeasonOverviewDB
import tmg.f1stats.repo.models.LapTime
import tmg.f1stats.repo.utils.filterNotNull
import tmg.utilities.extensions.combineWithPair

//region Inputs

interface RaceViewModelInputs {
    fun initialise(season: Int, round: Int)
    fun orderBy(seasonRaceAdapterType: RaceAdapterType)
}

//endregion

//region Outputs

interface RaceViewModelOutputs {
    fun items(): Observable<List<RaceModel>>
}

//endregion

class RaceViewModel(
        private val seasonOverviewDB: SeasonOverviewDB
) : BaseViewModel(), RaceViewModelInputs, RaceViewModelOutputs {

    private var viewType: BehaviorSubject<RaceAdapterType> = BehaviorSubject.createDefault(RaceAdapterType.RACE)
    private var seasonRound: BehaviorSubject<Pair<Int, Int>> = BehaviorSubject.create()

    private var listObservable: Observable<List<RaceModel>> = seasonRound
            .switchMap { (season, round) -> seasonOverviewDB.getSeasonRound(season, round) }
            .filterNotNull()
            .map { round ->
                round.drivers.map { driver ->
                    val overview = round.driverOverview(driver.id)
                    RaceModel(
                        driver = driver,
                        q1 = overview.q1,
                        q2 = overview.q2,
                        q3 = overview.q3,
                        raceResult = overview.race.time ?: LapTime(),
                        racePos = overview.race.finish,
                        gridPos = overview.race.grid,
                        racePoints = overview.race.points,
                        status = overview.race.status,
                        fastestLap = overview.race.fastestLap?.rank == 1
                    )
                }
            }
            .combineWithPair(viewType)
            .map { (seasonModels, viewType) ->
                @Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
                return@map when (viewType) {
                    RaceAdapterType.RACE -> seasonModels.sortedBy { it.racePos }
                    RaceAdapterType.QUALIFYING_POS_1 -> seasonModels.sortedBy { it.q1?.position ?: Int.MAX_VALUE }
                    RaceAdapterType.QUALIFYING_POS_2 -> seasonModels.sortedBy { it.q2?.position ?: Int.MAX_VALUE }
                    RaceAdapterType.QUALIFYING_POS_3 -> seasonModels.sortedBy { it.q3?.position ?: Int.MAX_VALUE }
                    RaceAdapterType.QUALIFYING_POS -> seasonModels.sortedBy { it.gridPos }
                }
            }

    var inputs: RaceViewModelInputs = this
    var outputs: RaceViewModelOutputs = this

    init {

    }

    //region Inputs

    override fun initialise(season: Int, round: Int) {
        seasonRound.onNext(Pair(season, round))
    }

    override fun orderBy(seasonRaceAdapterType: RaceAdapterType) {
        viewType.onNext(seasonRaceAdapterType)
    }

    //endregion

    //region Outputs

    override fun items(): Observable<List<RaceModel>> {
        return listObservable
    }

    //endregion

}