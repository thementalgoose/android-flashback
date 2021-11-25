package tmg.flashback.statistics.ui.race

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import tmg.flashback.formula1.model.Circuit
import tmg.flashback.formula1.model.RaceInfo

@Parcelize
data class RaceData(
    val season: Int,
    val round: Int,
    val circuitId: String,
    val defaultToRace: Boolean = true,
    val country: String,
    val raceName: String,
    val trackName: String,
    val countryISO: String,
    val date: LocalDate
): Parcelable