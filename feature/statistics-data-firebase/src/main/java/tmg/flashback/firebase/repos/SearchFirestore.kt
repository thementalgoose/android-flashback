package tmg.flashback.firebase.repos

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import tmg.crash_reporting.controllers.CrashController
import tmg.flashback.data.db.stats.SearchRepository
import tmg.flashback.data.models.stats.SearchCircuit
import tmg.flashback.data.models.stats.SearchConstructor
import tmg.flashback.data.models.stats.SearchDriver
import tmg.flashback.data.models.stats.SearchRace
import tmg.flashback.firebase.FirebaseRepo
import tmg.flashback.firebase.mappers.SearchMapper
import tmg.flashback.firebase.models.FSearchDriver

class SearchFirestore(
    crashController: CrashController,
    private val searchMapper: SearchMapper
): FirebaseRepo(crashController), SearchRepository {

    override fun allDrivers(): Flow<List<SearchDriver>> {
        crashController.log("document(search/drivers)")
        return document("search/drivers")
            .getDoc<FSearchDriver, List<SearchDriver>> {
                (it.drivers ?: emptyMap())
                    .mapNotNull { (driverId, model) ->
                        if (model == null) return@mapNotNull null
                        return@mapNotNull searchMapper.mapSearchDriver(model, driverId)
                    }
            }
            .mapNotNull { it }
    }

    override fun allConstructors(): Flow<List<SearchConstructor>> {
        TODO("Not yet implemented")
    }

    override fun allCircuits(): Flow<List<SearchCircuit>> {
        TODO("Not yet implemented")
    }

    override fun allRaces(): Flow<List<SearchRace>> {
        TODO("Not yet implemented")
    }

}