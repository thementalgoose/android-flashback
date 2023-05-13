package tmg.flashback.drivers.contract.model

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Parcelize
@Serializable
data class ScreenDriverSeasonData(
    val driverId: String,
    val driverName: String,
    val season: Int
): Parcelable {

    companion object NavType: androidx.navigation.NavType<ScreenDriverSeasonData>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): ScreenDriverSeasonData? {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle.getParcelable(key, ScreenDriverSeasonData::class.java)
            } else {
                bundle.getParcelable(key)
            }
        }
        override fun parseValue(value: String): ScreenDriverSeasonData {
            return Json.decodeFromString(serializer(), value)
        }
        override fun put(bundle: Bundle, key: String, value: ScreenDriverSeasonData) {
            bundle.putParcelable(key, value)
        }
    }
}