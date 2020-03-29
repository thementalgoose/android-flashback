package tmg.f1stats.repo.db

import io.reactivex.rxjava3.core.Observable
import tmg.f1stats.repo.Optional
import tmg.f1stats.repo.models.Round

interface SeasonOverviewDB {
    fun getCircuits(season: Int): Observable<List<Circuit>>
    fun getCircuit(season: Int, round: Int): Observable<Optional<Circuit>>
    fun getConstructor(season: Int, constructorId: String): Observable<Optional<Constructor>>
    fun getDriver(season: Int, driver: String): Observable<Optional<DriverOnWeekend>>
    fun getAllConstructors(season: Int): Observable<List<Constructor>>
    fun getSeasonOverview(season: Int): Observable<List<Round>>
    fun getLastWeekend(season: Int): Observable<Optional<Round>> // null = first weekend
    fun getNextWeekend(season: Int): Observable<Optional<Round>> // null = last weekend
    fun getSeasonRound(season: Int, round: Int): Observable<Optional<Round>>
}