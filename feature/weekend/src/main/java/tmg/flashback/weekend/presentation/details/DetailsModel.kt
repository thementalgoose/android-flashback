package tmg.flashback.weekend.presentation.details

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import java.time.LocalDate
import tmg.flashback.formula1.model.Circuit
import tmg.flashback.formula1.model.Schedule
import tmg.utilities.models.StringHolder

sealed class DetailsModel(
    val id: String
) {

    data class ScheduleWeekend(
        val days: List<Pair<LocalDate, List<Pair<Schedule, Boolean>>>>,
        val temperatureMetric: Boolean,
        val windspeedMetric: Boolean
    ): DetailsModel(
        id = "weekend"
    ) {
        companion object
    }

    data class Links(
        val links: List<Link>
    ): DetailsModel(
        id = "links"
    )

    sealed class Link(
        @StringRes
        val label: Int,
        @DrawableRes
        val icon: Int,
    ) {
        class Url(
            @StringRes
            label: Int,
            @DrawableRes
            icon: Int,
            val url: String,
        ): Link(label, icon) {
            override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (javaClass != other?.javaClass) return false

                other as Url

                return url == other.url
            }

            override fun hashCode(): Int {
                return url.hashCode()
            }
        }

        class Location(
            @StringRes
            label: Int,
            @DrawableRes
            icon: Int,
            val lat: Double,
            val lng: Double,
            val name: String
        ): Link(label, icon) {
            override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (javaClass != other?.javaClass) return false

                other as Location

                if (lat != other.lat) return false
                if (lng != other.lng) return false
                return name == other.name
            }

            override fun hashCode(): Int {
                var result = lat.hashCode()
                result = 31 * result + lng.hashCode()
                result = 31 * result + name.hashCode()
                return result
            }
        }

        companion object
    }

    data class Track(
        val circuit: Circuit,
        val raceName: String,
        val season: Int,
        val laps: String?
    ): DetailsModel(
        id = "track"
    ) {
        companion object
    }

    data class Label(
        val label: StringHolder,
        @DrawableRes
        val icon: Int,
    ): DetailsModel(
        id = "label-$label"
    ) {
        companion object
    }
}