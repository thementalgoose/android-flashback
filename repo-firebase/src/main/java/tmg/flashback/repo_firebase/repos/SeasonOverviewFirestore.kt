package tmg.flashback.repo_firebase.repos

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tmg.flashback.repo.db.CrashReporter
import tmg.flashback.repo.db.SeasonOverviewDB
import tmg.flashback.repo.models.*
import tmg.flashback.repo_firebase.converters.convert
import tmg.flashback.repo_firebase.firebase.FirebaseRepo
import tmg.flashback.repo_firebase.firebase.getDocument
import tmg.flashback.repo_firebase.models.FSeason
import tmg.flashback.repo_firebase.version

class SeasonOverviewFirestore(
    crashReporter: CrashReporter
): FirebaseRepo(crashReporter), SeasonOverviewDB {

    override suspend fun getCircuits(season: Int): Flow<List<Circuit>> {
        return getSeason(season)
            .map { it?.circuits ?: emptyList() }
    }

    override suspend fun getCircuit(season: Int, round: Int): Flow<Circuit?> {
        return getSeasonRound(season, round)
            .map { it?.circuit }
    }

    override suspend fun getConstructor(
        season: Int,
        constructorId: String
    ): Flow<Constructor?> {
        return getSeason(season)
            .map { seasonData -> seasonData?.constructors?.firstOrNull { it.id == constructorId } }
    }

    override suspend fun getDriver(season: Int, driver: String): Flow<Driver?> {
        return getSeason(season)
            .map { seasonData -> seasonData?.drivers?.firstOrNull { it.id == driver }}
    }

    override suspend fun getAllConstructors(season: Int): Flow<List<Constructor>> {
        return getSeason(season)
            .map { it?.constructors ?: emptyList() }
    }

    override suspend fun getSeasonOverview(season: Int): Flow<Pair<Int, List<Round>>> {
        return getSeasonWithRounds(season)
    }

    override suspend fun getPreviousWeekend(season: Int): Flow<Round?> {
        TODO("Not yet implemented")
    }

    override suspend fun getNextWeekend(season: Int): Flow<Round?> {
        TODO("Not yet implemented")
    }

    override suspend fun getSeasonRound(season: Int, round: Int): Flow<Round?> {
        return getRounds(season)
            .map { rounds -> rounds.firstOrNull { it.round == round }}
    }

    private suspend fun getRounds(season: Int): Flow<List<Round>> {
        return getSeason(season)
            .map { it?.rounds ?: emptyList() }
    }

    private suspend fun getSeasonWithRounds(season: Int): Flow<Pair<Int, List<Round>>> {
        return getSeason(season)
            .map { Pair(season, it?.rounds ?: emptyList()) }
    }

    private suspend fun getSeason(season: Int): Flow<Season?> {
        return document("version/$version/seasons/$season")
            .getDoc<FSeason>()
            .convertModel { it?.convert(season) }
    }
}