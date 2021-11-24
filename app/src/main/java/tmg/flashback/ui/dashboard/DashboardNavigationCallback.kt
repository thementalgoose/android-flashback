package tmg.flashback.ui.dashboard

interface DashboardNavigationCallback {
    fun openSeasonList()
    fun seasonSelected(season: Int)
    fun closeSeasonList()
}