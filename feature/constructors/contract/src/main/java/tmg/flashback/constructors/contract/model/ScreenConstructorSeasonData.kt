package tmg.flashback.constructors.contract.model

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Parcelize
@Serializable
data class ScreenConstructorSeasonData(
    val constructorId: String,
    val constructorName: String,
    val season: Int
): Parcelable {

    companion object NavType: androidx.navigation.NavType<ScreenConstructorSeasonData>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): ScreenConstructorSeasonData? {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle.getParcelable(key, ScreenConstructorSeasonData::class.java)
            } else {
                bundle.getParcelable(key)
            }
        }
        override fun parseValue(value: String): ScreenConstructorSeasonData {
            return Json.decodeFromString(serializer(), value)
        }
        override fun put(bundle: Bundle, key: String, value: ScreenConstructorSeasonData) {
            bundle.putParcelable(key, value)
        }
    }
}