package tmg.flashback.statistics.network.models

import kotlinx.serialization.Serializable

@Serializable
data class MetadataWrapper<T>(
    val lastUpdated: Long,
    val data: T
)