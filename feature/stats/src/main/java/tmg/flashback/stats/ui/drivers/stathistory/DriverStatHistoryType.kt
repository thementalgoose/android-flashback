package tmg.flashback.stats.ui.drivers.stathistory

import androidx.annotation.StringRes

enum class DriverStatHistoryType(
    @StringRes
    val label: Int
) {
    CHAMPIONSHIPS(R.string.stat_history_championships),
    WINS(R.string.stat_history_wins),
    POLES(R.string.stat_history_poles);
}