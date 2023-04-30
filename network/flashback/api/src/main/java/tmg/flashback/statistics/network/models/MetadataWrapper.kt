package tmg.flashback.statistics.network.models

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class MetadataWrapper<T>(
    val lastUpdated: Long,
    val data: T
)