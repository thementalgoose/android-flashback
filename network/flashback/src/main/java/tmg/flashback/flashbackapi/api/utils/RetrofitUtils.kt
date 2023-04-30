package tmg.flashback.flashbackapi.api.utils

import retrofit2.Response
import tmg.flashback.flashbackapi.api.models.MetadataWrapper

fun <T> Response<MetadataWrapper<T>>.data(): T? {
    if (this.isSuccessful) {
        return this.body()?.data
    }
    return null
}

val <T> Response<MetadataWrapper<T>>.hasData: Boolean
    get() {
        return this.isSuccessful && this.data() != null
    }

