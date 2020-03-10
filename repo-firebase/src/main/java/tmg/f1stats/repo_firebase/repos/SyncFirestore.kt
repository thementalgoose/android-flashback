package tmg.f1stats.repo_firebase.repos

import io.reactivex.rxjava3.core.Observable
import tmg.f1stats.repo.db.SyncDB
import tmg.f1stats.repo.enums.SyncProgress
import tmg.f1stats.repo.enums.SyncType
import tmg.f1stats.repo.models.Sync
import tmg.f1stats.repo_firebase.converters.toModel
import tmg.f1stats.repo_firebase.firebase.addDocument
import tmg.f1stats.repo_firebase.firebase.getDocuments
import tmg.f1stats.repo_firebase.models.FSync

class SyncFirestore: SyncDB {
    override fun addSyncItem(syncType: SyncType, season: Int, round: Int?) {
        val syncItem: Sync = Sync(
            type = syncType,
            progress = SyncProgress.NEW,
            season = season,
            round = round
        )

        addDocument("sync", syncItem) { it.toModel() }
    }

    override fun allSyncItems(): Observable<List<Sync>> {
        return getDocuments(FSync::class.java, "seasonOverview") { model, id -> model.toModel() }
    }
}