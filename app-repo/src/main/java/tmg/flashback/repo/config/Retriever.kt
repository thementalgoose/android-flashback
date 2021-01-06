package tmg.flashback.repo.config

data class Retriever<T>(
        val item: T,
        val retrieverType: RetrieverType
)