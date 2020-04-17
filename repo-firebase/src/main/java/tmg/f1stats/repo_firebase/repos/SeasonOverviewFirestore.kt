package tmg.f1stats.repo_firebase.repos

import tmg.f1stats.repo.db.SeasonOverviewDB
import tmg.f1stats.repo.models.*
import tmg.f1stats.repo_firebase.converters.convert
import tmg.f1stats.repo_firebase.firebase.getDocument
import tmg.f1stats.repo_firebase.models.FSeason

class SeasonOverviewFirestore : SeasonOverviewDB {
    override suspend fun getCircuits(season: Int): List<Circuit> {
        return getSeason(season)?.circuits ?: emptyList()
    }

    override suspend fun getCircuit(season: Int, round: Int): Circuit? {
        return getSeasonRound(season, round)?.circuit
    }

    override suspend fun getConstructor(
        season: Int,
        constructorId: String
    ): Constructor? {
        return getSeason(season)
            ?.constructors
            ?.firstOrNull { it.id == constructorId }
    }

    override suspend fun getDriver(season: Int, driver: String): Driver? {
        return getSeason(season)
            ?.drivers
            ?.firstOrNull { it.id == driver }
    }

    override suspend fun getAllConstructors(season: Int): List<Constructor> {
        return getSeason(season)?.constructors ?: emptyList()
    }

    override suspend fun getSeasonOverview(season: Int): List<Round> {
        return getRounds(season)
    }

    override suspend fun getPreviousWeekend(season: Int): Round? {
        TODO("Not yet implemented")
    }

    override suspend fun getNextWeekend(season: Int): Round? {
        TODO("Not yet implemented")
    }

    override suspend fun getSeasonRound(season: Int, round: Int): Round? {
        return getRounds(season)
            .firstOrNull { it.round == round }
    }

    private suspend fun getRounds(season: Int): List<Round> {
        return getSeason(season)?.rounds ?: emptyList()
    }

    private suspend fun getSeason(season: Int): Season? {
        return getDocument(FSeason::class.java, "seasons/$season") { model, _ ->
            model.convert(
                season
            )
        }
    }
}