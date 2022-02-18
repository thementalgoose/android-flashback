package tmg.flashback.regulations.domain.items

import tmg.flashback.regulations.R
import tmg.flashback.regulations.domain.Item
import tmg.utilities.models.StringHolder

internal fun statMaxRaceDistance(km: Int) = Item.Stat(
    label = R.string.format_stats_race_length,
    value = StringHolder("${km}km"),
    icon = R.drawable.ic_stat_regs_race_length
)

internal fun statMaxFuel(ltrs: Int) = Item.Stat(
    label = R.string.format_stats_fuel_limit,
    value = StringHolder("${ltrs} litres"),
    icon = R.drawable.ic_stat_regs_race_length
)

internal fun statFastestLapPoint(point: Int) = Item.Stat(
    label = R.string.format_stats_fastest_lap_point,
    value = StringHolder(point.toString()),
    icon = R.drawable.ic_stat_regs_fastest_lap
)

internal fun statFastestLapPoint(boolean: Boolean) = Item.Stat(
    label = R.string.format_stats_fastest_lap_point,
    value = StringHolder(when (boolean) {
        true -> R.string.format_stat_yes
        false -> R.string.format_stat_no
    }),
    icon = R.drawable.ic_stat_regs_fastest_lap
)