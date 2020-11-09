package tmg.flashback.shared.pill

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import tmg.flashback.R

sealed class PillItem(
        @DrawableRes val icon: Int,
        @StringRes val label: Int
) {
    data class Wikipedia(
            val link: String
    ): PillItem(
            icon = R.drawable.ic_wikipedia,
            label = R.string.circuit_info_wikipedia
    )

    data class Circuit(
            val circuitId: String? = null
    ): PillItem(
            icon = R.drawable.ic_track_icon,
            label = R.string.circuit_info_circuit
    )

    data class ShowOnMap(
            val lat: Double? = null,
            val lng: Double? = null
    ): PillItem(
            icon = R.drawable.ic_map,
            label = R.string.circuit_info_maps
    )
}