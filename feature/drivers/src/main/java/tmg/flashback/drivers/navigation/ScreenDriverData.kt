package tmg.flashback.drivers.navigation

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Parcelize
@Serializable
data class ScreenDriverData(
    val driverId: String,
    val driverName: String
): Parcelable {

    companion object NavType: androidx.navigation.NavType<ScreenDriverData>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): ScreenDriverData? {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle.getParcelable(key, ScreenDriverData::class.java)
            } else {
                bundle.getParcelable(key)
            }
        }
        override fun parseValue(value: String): ScreenDriverData {
            return Json.Default.decodeFromString(serializer(), value)
        }
        override fun put(bundle: Bundle, key: String, value: ScreenDriverData) {
            bundle.putParcelable(key, value)
        }
    }
}