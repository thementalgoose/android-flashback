package tmg.f1stats.settings.sync

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import tmg.f1stats.base.BaseViewModel
import tmg.f1stats.repo.db.SyncDB
import tmg.f1stats.repo.enums.SyncType
import tmg.f1stats.repo.models.Sync

//region Inputs

interface SettingsSyncViewModelInputs {
    fun addSyncItem(season: Int, type: SyncType)
}

//endregion

//region Outputs

interface SettingsSyncViewModelOutputs {
    fun results(): Observable<List<Sync>>
    fun itemAdded(): Observable<Boolean>
}

//endregion

class SettingsSyncViewModel(
    private val syncDB: SyncDB
): BaseViewModel(), SettingsSyncViewModelInputs, SettingsSyncViewModelOutputs {

    private var syncItem: PublishSubject<Pair<Int, SyncType>> = PublishSubject.create()

    private var savedObservable: Observable<Boolean> = syncItem
        .map { (season, type) ->
            syncDB.addSyncItem(type, season)
            return@map true
        }

    var inputs: SettingsSyncViewModelInputs = this
    var outputs: SettingsSyncViewModelOutputs = this

    init {

    }

    //region Inputs

    override fun addSyncItem(season: Int, type: SyncType) {
        syncItem.onNext(Pair(season, type))
    }

    //endregion

    //region Outputs

    override fun itemAdded(): Observable<Boolean> {
        return savedObservable
    }

    override fun results(): Observable<List<Sync>> {
        return syncDB.allSyncItems()
    }

    //endregion
}