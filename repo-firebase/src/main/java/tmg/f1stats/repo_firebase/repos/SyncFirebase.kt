package tmg.f1stats.repo_firebase.repos

import io.reactivex.rxjava3.core.Observable
import tmg.f1stats.repo.db.SyncDB
import tmg.f1stats.repo.enums.SyncProgress
import tmg.f1stats.repo.enums.SyncType
import tmg.f1stats.repo.models.Sync

class SyncFirebase: SyncDB {
    override fun addSyncItem(syncType: SyncType, season: Int, round: Int?) {
        val syncItem: Sync = Sync(
            type = syncType,
            progress = SyncProgress.NEW,
            season = season,
            round = round
        )


    }

    override fun allSyncItems(): Observable<List<SyncType>> {
        TODO("Not yet implemented")
    }

}