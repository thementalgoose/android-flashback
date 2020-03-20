package tmg.f1stats.repo.models

import tmg.f1stats.repo.enums.SyncProgress
import tmg.f1stats.repo.enums.SyncType

data class Sync(
    val id: String? = null,
    val type: SyncType,
    val progress: SyncProgress,
    val errorMsg: String? = null,
    val season: Int,
    val round: Int?,
    val completedAt: String? = null
)