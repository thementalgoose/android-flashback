package tmg.flashback.statistics.ui.shared.pill

import androidx.annotation.DrawableRes
import tmg.flashback.statistics.R
import tmg.utilities.models.StringHolder

sealed class PillItem(
    @DrawableRes val icon: Int? = null,
    val label: StringHolder,
    val highlighted: Boolean = false
) {
    data class Wikipedia(
        val link: String
    ) : PillItem(
        icon = R.drawable.ic_wikipedia,
        label = StringHolder(R.string.circuit_info_wikipedia)
    )

    data class Tyres(
        val year: Int
    ) : PillItem(
        icon = R.drawable.ic_tyre,
        label = StringHolder(R.string.circuit_info_tyres)
    )

    data class Youtube(
        val link: String
    ) : PillItem(
        icon = R.drawable.ic_youtube,
        label = StringHolder(R.string.circuit_info_youtube)
    )

    data class Circuit(
        val circuitId: String,
        val circuitName: String
    ) : PillItem(
        icon = R.drawable.ic_track_icon,
        label = StringHolder(R.string.circuit_info_circuit)
    )

    data class ShowOnMap(
        val lat: Double? = null,
        val lng: Double? = null
    ) : PillItem(
        icon = R.drawable.ic_map,
        label = StringHolder(R.string.circuit_info_maps)
    )

    data class Label(
        private val string: String,
        private val highlight: Boolean = false
    ) : PillItem(
        icon = null,
        label = StringHolder(string),
        highlighted = highlight
    )

    data class LabelIcon(
        @DrawableRes
        private val _icon: Int,
        private val string: String,
        private val highlight: Boolean = false
    ) : PillItem(
        icon = _icon,
        label = StringHolder(string),
        highlighted = highlight
    )
}