package tmg.flashback.flashbacknews.api.models

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
internal data class MetadataWrapper<T>(
    val lastUpdated: Long,
    val data: T
)