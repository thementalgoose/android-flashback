package tmg.flashback.firebase.extensions

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException

inline fun <reified T> String.toJson(): T? {
    if (this.isEmpty()) {
        return null
    }
    val gson = Gson()
    return try {
        gson.fromJson(this, T::class.java)
    } catch (e: JsonSyntaxException) {
        null
    }
}