package tmg.flashback.repo.models

import tmg.flashback.repo.enums.SyncProgress
import tmg.flashback.repo.enums.SyncType

data class Sync(
    val id: String? = null,
    val type: SyncType,
    val progress: SyncProgress,
    val errorMsg: String? = null,
    val season: Int,
    val round: Int?,
    val completedAt: String? = null
)