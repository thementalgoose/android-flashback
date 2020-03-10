package tmg.f1stats.repo.db

import io.reactivex.rxjava3.core.Observable
import tmg.f1stats.repo.enums.SyncType
import tmg.f1stats.repo.models.Sync

interface SyncDB {
    fun addSyncItem(syncType: SyncType, season: Int, round: Int? = null)
    fun allSyncItems(): Observable<List<Sync>>
}