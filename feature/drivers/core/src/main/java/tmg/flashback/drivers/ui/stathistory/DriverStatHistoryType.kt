package tmg.flashback.drivers.ui.stathistory

import androidx.annotation.StringRes
import tmg.flashback.drivers.R
import tmg.flashback.drivers.contract.model.DriverStatHistoryType

val DriverStatHistoryType.label: Int
    @get:StringRes
    get() = when (this) {
        DriverStatHistoryType.CHAMPIONSHIPS -> R.string.stat_history_championships
        DriverStatHistoryType.WINS -> R.string.stat_history_wins
        DriverStatHistoryType.POLES -> R.string.stat_history_poles
        DriverStatHistoryType.PODIUMS -> R.string.stat_history_podiums
    }

val DriverStatHistoryType.analyticsKey: String
    get() = when (this) {
        DriverStatHistoryType.CHAMPIONSHIPS -> "Championships"
        DriverStatHistoryType.WINS -> "Wins"
        DriverStatHistoryType.POLES -> "Poles"
        DriverStatHistoryType.PODIUMS -> "Podiums"
    }