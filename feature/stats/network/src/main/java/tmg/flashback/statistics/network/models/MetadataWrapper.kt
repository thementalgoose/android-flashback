package tmg.flashback.statistics.network.models

data class MetadataWrapper<T>(
    val lastUpdated: Long,
    val data: T
)