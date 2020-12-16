package tmg.flashback.repo.config

import kotlinx.coroutines.flow.Flow

interface RemoteConfigRepository {

    suspend fun update(andActivate: Boolean = false): Boolean
    suspend fun activate(): Boolean

    val defaultYear: Int
    val banner: String?
}