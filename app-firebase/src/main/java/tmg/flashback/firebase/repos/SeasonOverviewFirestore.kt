package tmg.flashback.firebase.repos

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tmg.flashback.repo.db.stats.SeasonOverviewRepository
import tmg.flashback.repo.models.stats.*
import tmg.flashback.firebase.converters.convert
import tmg.flashback.firebase.FirebaseRepo
import tmg.flashback.firebase.crash.FirebaseCrashManager
import tmg.flashback.firebase.models.FSeason

class SeasonOverviewFirestore(
    crashManager: FirebaseCrashManager
) : FirebaseRepo(crashManager), SeasonOverviewRepository {

    override fun getCircuits(season: Int): Flow<List<CircuitSummary>> {
        crashManager.logInfo("SeasonOverviewFirestore.getCircuits($season)")
        return getSeason(season)
                .map { it?.circuits ?: emptyList() }
    }

    override fun getCircuit(season: Int, round: Int): Flow<CircuitSummary?> {
        crashManager.logInfo("SeasonOverviewFirestore.getCircuit($season, $round)")
        return getSeasonRound(season, round)
                .map { it?.circuit }
    }

    override fun getConstructor(
            season: Int,
            constructorId: String
    ): Flow<Constructor?> {
        crashManager.logInfo("SeasonOverviewFirestore.getConstructor($season, $constructorId)")
        return getSeason(season)
                .map { seasonData -> seasonData?.constructors?.firstOrNull { it.id == constructorId } }
    }

    override fun getDriver(season: Int, driver: String): Flow<Driver?> {
        crashManager.logInfo("SeasonOverviewFirestore.getDriver($season, $driver)")
        return getSeason(season)
                .map { seasonData -> seasonData?.drivers?.firstOrNull { it.id == driver } }
    }

    override fun getAllConstructors(season: Int): Flow<List<Constructor>> {
        crashManager.logInfo("SeasonOverviewFirestore.getAllConstructors($season)")
        return getSeason(season)
                .map { it?.constructors ?: emptyList() }
    }

    override fun getSeasonOverview(season: Int): Flow<Season> {
        crashManager.logInfo("SeasonOverviewFirestore.getSeasonOverview($season)")
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
        crashManager.logInfo("SeasonOverviewFirestore.getSeasonRound($season, $round)")
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