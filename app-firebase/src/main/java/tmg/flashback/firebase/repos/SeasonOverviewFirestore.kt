package tmg.flashback.firebase.repos

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tmg.flashback.repo.db.CrashManager
import tmg.flashback.repo.db.stats.SeasonOverviewDB
import tmg.flashback.repo.models.stats.*
import tmg.flashback.firebase.converters.convert
import tmg.flashback.firebase.FirebaseRepo
import tmg.flashback.firebase.models.FSeason

@ExperimentalCoroutinesApi
class SeasonOverviewFirestore(
    crashManager: CrashManager
) : FirebaseRepo(crashManager), SeasonOverviewDB {

    override fun getCircuits(season: Int): Flow<List<CircuitSummary>> {
        return getSeason(season)
                .map { it?.circuits ?: emptyList() }
    }

    override fun getCircuit(season: Int, round: Int): Flow<CircuitSummary?> {
        return getSeasonRound(season, round)
                .map { it?.circuit }
    }

    override fun getConstructor(
            season: Int,
            constructorId: String
    ): Flow<Constructor?> {
        return getSeason(season)
                .map { seasonData -> seasonData?.constructors?.firstOrNull { it.id == constructorId } }
    }

    override fun getDriver(season: Int, driver: String): Flow<Driver?> {
        return getSeason(season)
                .map { seasonData -> seasonData?.drivers?.firstOrNull { it.id == driver } }
    }

    override fun getAllConstructors(season: Int): Flow<List<Constructor>> {
        return getSeason(season)
                .map { it?.constructors ?: emptyList() }
    }

    override fun getSeasonOverview(season: Int): Flow<Season> {
        return getSeason(season)
                .map {
                    return@map it ?: Season(
                            season = season,
                            drivers = emptyList(),
                            constructors = emptyList(),
                            rounds = emptyList(),
                            constructorStandings = emptyMap(),
                            driverStandings = emptyMap()
                    )
                }
    }

    override fun getSeasonRound(season: Int, round: Int): Flow<Round?> {
        return getRounds(season)
                .map { rounds -> rounds.firstOrNull { it.round == round } }
    }

    private fun getRounds(season: Int): Flow<List<Round>> {
        return getSeason(season)
                .map { it?.rounds ?: emptyList() }
    }

    private fun getSeasonWithRounds(season: Int): Flow<Pair<Int, List<Round>>> {
        return getSeason(season)
                .map { Pair(season, it?.rounds ?: emptyList()) }
    }

    private fun getSeason(season: Int): Flow<Season?> {
        return document("seasons/$season")
                .getDoc<FSeason>()
                .convertModel { it?.convert(season) }
    }
}