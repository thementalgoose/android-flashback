package tmg.flashback.stats.ui.drivers.stathistory

import androidx.annotation.StringRes
import tmg.flashback.stats.R

enum class DriverStatHistoryType(
    @StringRes
    val label: Int,
    val analyticsKey: String
) {
    CHAMPIONSHIPS(R.string.stat_history_championships, "Championships"),
    WINS(R.string.stat_history_wins, "Wins"),
    POLES(R.string.stat_history_poles, "Poles");
}