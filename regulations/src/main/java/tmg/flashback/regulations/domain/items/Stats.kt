package tmg.flashback.regulations.domain.items

import tmg.flashback.regulations.R
import tmg.flashback.regulations.domain.Item
import tmg.utilities.models.StringHolder

internal fun statQualifyingSessionCount(count: Int) = Item.Stat(
    label = R.string.format_stats_qualifying_sessions,
    value = StringHolder(count.toString()),
    icon = R.drawable.ic_stat_regs_session_count
)

internal fun statSessionLengthQ1(mins: Int) = Item.Stat(
    label = R.string.format_stats_session_length_q1,
    value = StringHolder("${mins}mins"),
    icon = R.drawable.ic_stat_regs_q1
)

internal fun statSessionLengthQ2(mins: Int) = Item.Stat(
    label = R.string.format_stats_session_length_q2,
    value = StringHolder("${mins}mins"),
    icon = R.drawable.ic_stat_regs_q2
)

internal fun statSessionLengthQ3(mins: Int) = Item.Stat(
    label = R.string.format_stats_session_length_q3,
    value = StringHolder("${mins}mins"),
    icon = R.drawable.ic_stat_regs_q3
)

internal fun statLabel(label: Int) = Item.Stat(
    label = label,
    value = StringHolder(""),
    icon = R.drawable.ic_stat_regs_explanation
)

internal fun statMaxLaps(laps: Int) = Item.Stat(
    label = R.string.format_stats_max_laps,
    value = StringHolder(laps.toString()),
    icon = R.drawable.ic_stat_regs_lap_count
)

internal fun statMaxRaceDistance(km: Int) = Item.Stat(
    label = R.string.format_stats_race_length,
    value = StringHolder("${km}km"),
    icon = R.drawable.ic_stat_regs_race_length
)

internal fun statMaxFuel(ltrs: Int) = Item.Stat(
    label = R.string.format_stats_fuel_limit,
    value = StringHolder("${ltrs} litres"),
    icon = R.drawable.ic_stat_regs_fuel
)

internal fun statMandatoryPitStop(boolean: Boolean) = Item.Stat(
    label = R.string.format_stats_mandatory_pit_stop,
    value = StringHolder(boolean.toYesNo()),
    icon = R.drawable.ic_stat_regs_pitstop
)

internal fun statFastestLapPoint(point: Int) = Item.Stat(
    label = R.string.format_stats_fastest_lap_point,
    value = StringHolder(point.toString()),
    icon = R.drawable.ic_stat_regs_fastest_lap
)

internal fun statFastestLapPoint(boolean: Boolean) = Item.Stat(
    label = R.string.format_stats_fastest_lap_point,
    value = StringHolder(boolean.toYesNo()),
    icon = R.drawable.ic_stat_regs_fastest_lap
)

private fun Boolean.toYesNo(): Int {
    return when (this) {
        true -> R.string.format_stat_yes
        false -> R.string.format_stat_no
    }
}