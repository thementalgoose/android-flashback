package tmg.f1stats.repo_firebase.models

import tmg.f1stats.repo.enums.SyncProgress
import tmg.f1stats.repo.enums.SyncType
import tmg.f1stats.repo.models.Sync

data class FSync(
    val type: String,
    val progress: String,
    val errorMsg: String?,
    val season: Int,
    val round: Int?,
    val completedAt: String?
) {
    constructor() : this("", "", null, -1, -1, null)
}