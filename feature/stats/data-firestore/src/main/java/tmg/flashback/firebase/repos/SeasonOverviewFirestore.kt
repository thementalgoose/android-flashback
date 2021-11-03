package tmg.flashback.firebase.repos

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tmg.crash_reporting.controllers.CrashController
import tmg.flashback.data.db.stats.SeasonOverviewRepository
import tmg.flashback.firebase.FirebaseRepo
import tmg.flashback.firebase.mappers.SeasonOverviewMapper
import tmg.flashback.firebase.models.FSeason
import tmg.flashback.formula1.model.Circuit
import tmg.flashback.formula1.model.Constructor

class SeasonOverviewFirestore(
        crashController: CrashController,
        private val seasonOverviewMapper: SeasonOverviewMapper
) : FirebaseRepo(crashController), SeasonOverviewRepository {

    override fun getCircuits(season: Int): Flow<List<Circuit>> {
        return getSeason(season, "getCircuits")
                .map { it?.circuits ?: emptyList() }
    }

    override fun getCircuit(season: Int, round: Int): Flow<Circuit?> {
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

    override fun getDriver(season: Int, driver: String): Flow<tmg.flashback.formula1.model.DriverWithEmbeddedConstructor?> {
        return getSeason(season, "getDriver $driver")
                .map { seasonData -> seasonData?.driverWithEmbeddedConstructors?.firstOrNull { it.id == driver } }
    }

    override fun getAllConstructors(season: Int): Flow<List<Constructor>> {
        return getSeason(season, "getAllConstructors")
                .map { it?.constructors ?: emptyList() }
    }

    override fun getSeasonOverview(season: Int): Flow<tmg.flashback.formula1.model.Season> {
        return getSeason(season, "getSeasonOverview")
                .map {
                    return@map it ?: tmg.flashback.formula1.model.Season(
                        season = season,
                        driverWithEmbeddedConstructors = emptyList(),
                        constructors = emptyList(),
                        races = emptyList(),
                        constructorStandings = emptyList(),
                        driverStandings = emptyList()
                    )
                }
    }

    override fun getSeasonRound(season: Int, round: Int): Flow<tmg.flashback.formula1.model.Race?> {
        return getRounds(season, "getSeasonRound $round")
                .map { rounds -> rounds.firstOrNull { it.round == round } }
    }

    private fun getRounds(season: Int, context: String): Flow<List<tmg.flashback.formula1.model.Race>> {
        return getSeason(season, context)
                .map { it?.races ?: emptyList() }
    }

    private fun getSeasonWithRounds(season: Int, context: String): Flow<Pair<Int, List<tmg.flashback.formula1.model.Race>>> {
        return getSeason(season, context)
                .map { Pair(season, it?.races ?: emptyList()) }
    }

    private fun getSeason(season: Int, context: String): Flow<tmg.flashback.formula1.model.Season?> {
        crashController.log("document(seasons/$season) for $context")
        return document("seasons/$season")
                .getDoc<FSeason, tmg.flashback.formula1.model.Season> {
                    seasonOverviewMapper.mapSeason(it, season)
                }
    }
}