package tmg.flashback.firebase.repos

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tmg.crash_reporting.controllers.CrashController
import tmg.flashback.data.db.stats.SeasonOverviewRepository
import tmg.flashback.data.models.stats.*
import tmg.flashback.firebase.converters.convert
import tmg.flashback.firebase.FirebaseRepo
import tmg.flashback.firebase.models.FSeason

class SeasonOverviewFirestore(
    crashController: CrashController
) : FirebaseRepo(crashController), SeasonOverviewRepository {

    override fun getCircuits(season: Int): Flow<List<CircuitSummary>> {
        return getSeason(season, "getCircuits")
                .map { it?.circuits ?: emptyList() }
    }

    override fun getCircuit(season: Int, round: Int): Flow<CircuitSummary?> {
        return getRounds(season, "getCircuit $round")
                .map { rounds -> rounds.firstOrNull { it.round == round } }
                .map { it?.circuit }
    }

    override fun getConstructor(
            season: Int,
            constructorId: String
    ): Flow<Constructor?> {
        return getSeason(season, "getConstructor $constructorId")
                .map { seasonData -> seasonData?.constructors?.firstOrNull { it.id == constructorId } }
    }

    override fun getDriver(season: Int, driver: String): Flow<Driver?> {
        return getSeason(season, "getDriver $driver")
                .map { seasonData -> seasonData?.drivers?.firstOrNull { it.id == driver } }
    }

    override fun getAllConstructors(season: Int): Flow<List<Constructor>> {
        return getSeason(season, "getAllConstructors")
                .map { it?.constructors ?: emptyList() }
    }

    override fun getSeasonOverview(season: Int): Flow<Season> {
        return getSeason(season, "getSeasonOverview")
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
        return getRounds(season, "getSeasonRound $round")
                .map { rounds -> rounds.firstOrNull { it.round == round } }
    }

    private fun getRounds(season: Int, context: String): Flow<List<Round>> {
        return getSeason(season, context)
                .map { it?.rounds ?: emptyList() }
    }

    private fun getSeasonWithRounds(season: Int, context: String): Flow<Pair<Int, List<Round>>> {
        return getSeason(season, context)
                .map { Pair(season, it?.rounds ?: emptyList()) }
    }

    private fun getSeason(season: Int, context: String): Flow<Season?> {
        crashController.log("document(seasons/$season) for $context")
        return document("seasons/$season")
                .getDoc<FSeason, Season> { it.convert(season) }
    }
}