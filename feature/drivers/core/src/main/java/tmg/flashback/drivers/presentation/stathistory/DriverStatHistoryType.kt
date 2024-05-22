package tmg.flashback.drivers.presentation.stathistory

import androidx.annotation.StringRes
import tmg.flashback.drivers.R
import tmg.flashback.strings.R.string
import tmg.flashback.drivers.contract.model.DriverStatHistoryType

val DriverStatHistoryType.label: Int
    @StringRes
    get() = when (this) {
        DriverStatHistoryType.CHAMPIONSHIPS -> string.stat_history_championships
        DriverStatHistoryType.WINS -> string.stat_history_wins
        DriverStatHistoryType.POLES -> string.stat_history_poles
        DriverStatHistoryType.PODIUMS -> string.stat_history_podiums
    }

val DriverStatHistoryType.analyticsKey: String
    get() = when (this) {
        DriverStatHistoryType.CHAMPIONSHIPS -> "Championships"
        DriverStatHistoryType.WINS -> "Wins"
        DriverStatHistoryType.POLES -> "Poles"
        DriverStatHistoryType.PODIUMS -> "Podiums"
    }