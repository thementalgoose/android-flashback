package tmg.f1stats.repo_firebase.repos

import io.reactivex.rxjava3.core.Observable
import tmg.f1stats.repo.Optional
import tmg.f1stats.repo.db.SeasonOverviewDB
import tmg.f1stats.repo.models.Circuit
import tmg.f1stats.repo.models.Constructor
import tmg.f1stats.repo.models.DriverOnWeekend
import tmg.f1stats.repo.models.Round
import tmg.f1stats.repo.utils.mapOptionalValue
import tmg.f1stats.repo_firebase.firebase.getDocumentMap
import tmg.f1stats.repo_firebase.models.FSeason

class SeasonOverviewFirestore: SeasonOverviewDB {
    override fun getCircuits(season: Int): Observable<List<Circuit>> {
        return getSeason(season)
            .map { seasonValue ->
                seasonValue.map {
                    it.circuit
                }
            }
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
            .map { seasonValue ->
                Optional(seasonValue.map { it.constructors }
                    .flatten()
                    .firstOrNull { it.constructorId == constructorId })
            }
    }

    override fun getDriver(season: Int, driver: String): Observable<Optional<DriverOnWeekend>> {
        return getSeason(season)
            .map { seasonValue ->
                Optional(seasonValue.map { it.drivers }
                    .flatten()
                    .firstOrNull { it.driverId == driver })
            }
    }

    override fun getAllConstructors(season: Int): Observable<List<Constructor>> {
        return getSeason(season)
            .map { seasonValue ->
                seasonValue.map { it.constructors }
                    .flatten()
                    .distinctBy { it.constructorId }
            }
    }

    override fun getSeasonOverview(season: Int): Observable<List<Round>> {
        return getSeason(season)
    }

    override fun getLastWeekend(season: Int): Observable<Optional<Round>> {
        TODO("Not yet implemented")
    }

    override fun getNextWeekend(season: Int): Observable<Optional<Round>> {
        TODO("Not yet implemented")
    }

    override fun getSeasonRound(season: Int, round: Int): Observable<Optional<Round>> {
        return getSeason(season)
            .map { seasonRound -> Optional(seasonRound.firstOrNull { it.round == round }) }
    }

    private fun getSeason(season: Int): Observable<List<Round>> {
        return getDocumentMap(FSeason::class.java, "seasonOverview/$season") { model ->
            model.toModel()
        }.map { seasonList ->
            seasonList.sortedBy { it.round }
        }
    }


}