package tmg.flashback.circuits.navigation

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Parcelize
@Serializable
data class ScreenCircuitData(
    val circuitId: String,
    val circuitName: String
): Parcelable {

    companion object NavType: androidx.navigation.NavType<ScreenCircuitData>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): ScreenCircuitData? {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle.getParcelable(key, ScreenCircuitData::class.java)
            } else {
                bundle.getParcelable(key)
            }
        }
        override fun parseValue(value: String): ScreenCircuitData {
            return Json.Default.decodeFromString(serializer(), value)
        }
        override fun put(bundle: Bundle, key: String, value: ScreenCircuitData) {
            bundle.putParcelable(key, value)
        }
    }
}