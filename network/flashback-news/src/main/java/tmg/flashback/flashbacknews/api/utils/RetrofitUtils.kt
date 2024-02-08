package tmg.flashback.flashbacknews.api.utils

import retrofit2.Response
import tmg.flashback.flashbacknews.api.models.MetadataWrapper

internal fun <T> Response<MetadataWrapper<T>>.data(): T? {
    if (this.isSuccessful) {
        return this.body()?.data
    }
    return null
}

