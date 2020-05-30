package tmg.flashback.dashboard.season

import org.threeten.bp.LocalDate
import tmg.flashback.dashboard.season.DashboardSeasonViewType.*

sealed class DashboardSeasonAdapterItem(
    val viewType: DashboardSeasonViewType
) {
    data class Header(
        val year: Int,
        val season: Int,
        val raceCompleted: Int,
        val raceScheduled: Int
    ) : DashboardSeasonAdapterItem(HEADER) {
        override fun equals(other: Any?) = other is Header
        override fun hashCode(): Int = 37
    }

    data class Track(
        val season: Int,
        val round: Int,
        val date: LocalDate,
        val circuitId: String,
        val raceName: String,
        val trackName: String,
        val trackNationality: String,
        val trackISO: String
    ) : DashboardSeasonAdapterItem(TRACK) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Track

            if (trackName != other.trackName) return false
            if (trackNationality != other.trackNationality) return false
            if (trackISO != other.trackISO) return false

            return true
        }

        override fun hashCode(): Int {
            var result = trackName.hashCode()
            result = 31 * result + trackNationality.hashCode()
            result = 31 * result + trackISO.hashCode()
            return result
        }
    }
}

enum class DashboardSeasonViewType {
    HEADER,
    TRACK
}