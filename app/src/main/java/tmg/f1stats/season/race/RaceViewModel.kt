package tmg.f1stats.season.race

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import tmg.f1stats.base.BaseViewModel
import tmg.f1stats.repo.db.SeasonOverviewDB
import tmg.f1stats.repo.models.LapTime
import tmg.f1stats.repo.models.Round
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
    fun items(): Observable<Pair<RaceAdapterType, List<RaceModel>>>
    fun date(): Observable<LocalDate>
    fun time(): Observable<LocalTime>
}

//endregion

class RaceViewModel(
        private val seasonOverviewDB: SeasonOverviewDB
) : BaseViewModel(), RaceViewModelInputs, RaceViewModelOutputs {

    private var viewType: BehaviorSubject<RaceAdapterType> = BehaviorSubject.createDefault(RaceAdapterType.QUALIFYING_POS)
    private var seasonRound: BehaviorSubject<Pair<Int, Int>> = BehaviorSubject.create()

    private var seasonRoundObservable: Observable<Round> = seasonRound
            .switchMap { (season, round) -> seasonOverviewDB.getSeasonRound(season, round) }
            .filterNotNull()
            .share()

    private var listObservable: Observable<Pair<RaceAdapterType, List<RaceModel>>> = seasonRoundObservable
            .combineWithPair(viewType)
            .subscribeOn(Schedulers.io())
            .map { (round, viewType) ->
                val driverIds: List<String> = round.race
                    .values
                    .sortedBy {
                        when (viewType) {
                            RaceAdapterType.RACE -> it.finish
                            RaceAdapterType.QUALIFYING_POS_1 -> round.driverOverview(it.driver.id).q1?.position ?: Int.MAX_VALUE
                            RaceAdapterType.QUALIFYING_POS_2 -> round.driverOverview(it.driver.id).q2?.position ?: Int.MAX_VALUE
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
                                    driverFirst = getDriverModel(round, driverIds[0]),
                                    driverSecond = getDriverModel(round, driverIds[1]),
                                    driverThird = getDriverModel(round, driverIds[2])
                                )
                            )
                            startIndex = 3
                        }
                        for (i in startIndex until driverIds.size) {
                            list.add(getDriverModel(round, driverIds[i]))
                        }
                    }
                    RaceAdapterType.QUALIFYING_POS_1,
                    RaceAdapterType.QUALIFYING_POS_2,
                    RaceAdapterType.QUALIFYING_POS -> {
                        list.addAll(driverIds.map { getDriverModel(round, it) })
                    }
                }
                return@map Pair(viewType, list)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .map { it }

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

    override fun items(): Observable<Pair<RaceAdapterType, List<RaceModel>>> {
        return listObservable
    }

    override fun date(): Observable<LocalDate> {
        return seasonRoundObservable.map { it.date }
    }

    override fun time(): Observable<LocalTime> {
        return seasonRoundObservable.map { it.time }
    }

    //endregion

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