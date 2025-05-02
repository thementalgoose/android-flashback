package tmg.flashback.constructors.navigation

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Parcelize
@Serializable
data class ScreenConstructorData(
    val constructorId: String,
    val constructorName: String
): Parcelable {

    companion object NavType: androidx.navigation.NavType<ScreenConstructorData>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): ScreenConstructorData? {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle.getParcelable(key, ScreenConstructorData::class.java)
            } else {
                bundle.getParcelable(key)
            }
        }
        override fun parseValue(value: String): ScreenConstructorData {
            return Json.Default.decodeFromString(serializer(), value)
        }
        override fun put(bundle: Bundle, key: String, value: ScreenConstructorData) {
            bundle.putParcelable(key, value)
        }
    }
}