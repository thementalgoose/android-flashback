package tmg.flashback.configuration.extensions

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
fun <T> String.toJson(clazz: Class<T>): T? {
    if (this.isEmpty()) {
        return null
    }
    val gson = Gson()
    return try {
        gson.fromJson(this, clazz)
    } catch (e: JsonSyntaxException) {
        null
    }
}