package tmg.f1stats.repo.db

import io.reactivex.rxjava3.core.Observable
import tmg.f1stats.repo.Optional
import tmg.f1stats.repo.models.Circuit
import tmg.f1stats.repo.models.Constructor
import tmg.f1stats.repo.models.DriverOnWeekend
import tmg.f1stats.repo.models.SeasonRound

interface SeasonOverviewDB {
    fun getCircuits(season: Int): Observable<List<Circuit>>
    fun getCircuit(season: Int, round: Int): Observable<Optional<Circuit>>
    fun getConstructor(season: Int, constructorId: String): Observable<Optional<Constructor>>
    fun getDriver(season: Int, driver: String): Observable<Optional<DriverOnWeekend>>
    fun getAllConstructors(season: Int): Observable<List<Constructor>>
    fun getSeasonOverview(season: Int): Observable<List<SeasonRound>>
    fun getLastWeekend(season: Int): Observable<Optional<SeasonRound>> // null = first weekend
    fun getNextWeekend(season: Int): Observable<Optional<SeasonRound>> // null = last weekend
    fun getSeasonRound(season: Int, round: Int): Observable<Optional<SeasonRound>>
}