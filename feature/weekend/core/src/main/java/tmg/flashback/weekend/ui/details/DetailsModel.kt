package tmg.flashback.weekend.ui.details

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import org.threeten.bp.LocalDate
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

    data class Link(
        @StringRes
        val label: Int,
        @DrawableRes
        val icon: Int,
        val url: String,
    ) {
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