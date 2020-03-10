package tmg.f1stats.repo_firebase.repos

import io.reactivex.rxjava3.core.Observable
import tmg.f1stats.repo.Optional
import tmg.f1stats.repo.db.SeasonOverviewDB
import tmg.f1stats.repo.models.Circuit
import tmg.f1stats.repo.models.Constructor
import tmg.f1stats.repo.models.DriverOnWeekend
import tmg.f1stats.repo.models.SeasonRound
import tmg.f1stats.repo.utils.mapOptionalValue
import tmg.f1stats.repo_firebase.converters.toModel
import tmg.f1stats.repo_firebase.firebase.getDocument
import tmg.f1stats.repo_firebase.firebase.getDocumentMap
import tmg.f1stats.repo_firebase.models.FSeasonOverview

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

    override fun getSeasonOverview(season: Int): Observable<List<SeasonRound>> {
        return getSeason(season)
    }

    override fun getLastWeekend(season: Int): Observable<Optional<SeasonRound>> {
        TODO("Not yet implemented")
    }

    override fun getNextWeekend(season: Int): Observable<Optional<SeasonRound>> {
        TODO("Not yet implemented")
    }

    override fun getSeasonRound(season: Int, round: Int): Observable<Optional<SeasonRound>> {
        return getSeason(season)
            .map { seasonRound -> Optional(seasonRound.firstOrNull { it.round == round }) }
    }

    private fun getSeason(season: Int): Observable<List<SeasonRound>> {
        return getDocumentMap(FSeasonOverview::class.java, "season/$season") { model ->
            model.toModel()
        }
    }


}