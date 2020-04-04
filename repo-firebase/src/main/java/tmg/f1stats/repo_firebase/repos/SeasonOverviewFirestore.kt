package tmg.f1stats.repo_firebase.repos

import io.reactivex.rxjava3.core.Observable
import tmg.f1stats.repo.Optional
import tmg.f1stats.repo.db.SeasonOverviewDB
import tmg.f1stats.repo.models.*
import tmg.f1stats.repo.utils.filterNotNull
import tmg.f1stats.repo.utils.mapOptionalValue
import tmg.f1stats.repo.utils.mapToOptional
import tmg.f1stats.repo_firebase.converters.convert
import tmg.f1stats.repo_firebase.firebase.getDocument
import tmg.f1stats.repo_firebase.firebase.getDocumentMap
import tmg.f1stats.repo_firebase.models.FSeason

class SeasonOverviewFirestore : SeasonOverviewDB {
    override fun getCircuits(season: Int): Observable<List<Circuit>> {
        return getSeason(season)
            .map { it.circuits }
    }

    override fun getCircuit(season: Int, round: Int): Observable<Optional<Circuit>> {
        return getSeasonRound(season, round)
            .mapOptionalValue { it.circuit }
    }

    override fun getConstructor(
        season: Int,
        constructorId: String
    ): Observable<Optional<Constructor>> {
        return getSeason(season)
            .map { it.constructors }
            .mapToOptional { constructors -> constructors.firstOrNull { it.id == constructorId } }
    }

    override fun getDriver(season: Int, driver: String): Observable<Optional<Driver>> {
        return getSeason(season)
            .map { it.drivers }
            .mapToOptional { drivers -> drivers.firstOrNull { it.id == driver } }
    }

    override fun getAllConstructors(season: Int): Observable<List<Constructor>> {
        return getSeason(season)
            .map { it.constructors }
    }

    override fun getSeasonOverview(season: Int): Observable<List<Round>> {
        return getRounds(season)
    }

    override fun getPreviousWeekend(season: Int): Observable<Optional<Round>> {
        TODO("Not yet implemented")
    }

    override fun getNextWeekend(season: Int): Observable<Optional<Round>> {
        TODO("Not yet implemented")
    }

    override fun getSeasonRound(season: Int, round: Int): Observable<Optional<Round>> {
        return getRounds(season)
            .mapToOptional { rounds -> rounds.firstOrNull { it.round == round } }
    }

    private fun getSeason(season: Int): Observable<Season> {
        return getDocument(FSeason::class.java, "seasons/$season") { model, _ -> model.convert(season) }
            .filterNotNull()
    }

    private fun getRounds(season: Int): Observable<List<Round>> {
        return getSeason(season)
            .map { it.rounds }
    }

}