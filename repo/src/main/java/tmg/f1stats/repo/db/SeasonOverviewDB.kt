package tmg.f1stats.repo.db

import io.reactivex.rxjava3.core.Observable
import tmg.f1stats.repo.Optional
import tmg.f1stats.repo.models.Circuit
import tmg.f1stats.repo.models.Constructor
import tmg.f1stats.repo.models.SeasonRound

interface SeasonOverviewDB {
    fun getCircuit(season: Int): Observable<Circuit>
    fun getAllConstructors(season: Int): Observable<List<Constructor>>
    fun getSeasonOverview(season: Int): Observable<List<SeasonRound>>
    fun getLastWeekend(season: Int): Observable<Optional<SeasonRound>> // null = first weekend
    fun getNextWeekend(season: Int): Observable<Optional<SeasonRound>> // null = last weekend
    fun getWeekend(season: Int, round: Int): Observable<SeasonRound>
}